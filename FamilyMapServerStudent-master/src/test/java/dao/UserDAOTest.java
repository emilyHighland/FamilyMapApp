package dao;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDao;
import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Database db;
    private User testUser;
    private UserDao uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new user with random data
        testUser = new User("Gale", "i<3puppies", "gale@gmail.com",
                "Gale", "Highland", "f", "Gale123A");
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the UserDAO so it can access the database
        uDao = new UserDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        uDao.insert(testUser);
        //So lets use a find method to get the user that we just put in back out
        User compareTest = uDao.find(testUser.getUsername());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(testUser, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        uDao.insert(testUser);
        //but our sql table is set up so that "username" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> uDao.insert(testUser));
    }

    @Test
    public void findPass() throws DataAccessException {
        uDao.insert(testUser);
        User compareTest = uDao.find(testUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(testUser, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        uDao.insert(testUser);
        User compareTest = uDao.find(testUser.getUsername());
        assertNotNull(compareTest);
        uDao.clearUsers();
        assertNull(uDao.find(testUser.getUsername()));
    }

//    @Test
//    public void deletePass() throws DataAccessException{
//        uDao.insert(testUser);
//        User compareTest = uDao.find(testUser.getUsername());
//        assertNotNull(compareTest);
//        uDao.deleteUser(testUser.getUsername());
//        assertNull(uDao.find(testUser.getUsername()));
//    }

    @Test
    public void clearTest() throws DataAccessException {
        uDao.insert(testUser);
        User compareTest = uDao.find(testUser.getUsername());
        assertNotNull(compareTest);
        uDao.clearUsers();
        assertNull(uDao.find(testUser.getUsername()));
    }
}
