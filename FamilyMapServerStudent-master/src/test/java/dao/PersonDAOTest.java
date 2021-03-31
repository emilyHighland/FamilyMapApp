package dao;

import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDao;
import Model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    private Database db;
    private PersonDao pDao;
    private Person person1;
    private Person person2;
    private final ArrayList<Person> EMPTY_ARRAY = new ArrayList<>();

    @BeforeEach
    public void setUp() throws DataAccessException {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new person with random data
        person1 = new Person("Gale123A","Gale","Gale","Highland",
                "f","Daddio123","Mommio123","Love123");
        person2 = new Person("cheese123","Gale","Rachel","High",
                "f","Dad123","Mom123","1Love");
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the PersonDAO so it can access the database
        pDao = new PersonDao(conn);
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
        pDao.insert(person1);
        //So lets use a find method to get the person that we just put in back out
        Person compareTest = pDao.find(person1.getPersonID());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(person1, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        pDao.insert(person1);
        //but our sql table is set up so that "personID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> pDao.insert(person1));
    }

    @Test
    public void findPass() throws DataAccessException {
        pDao.insert(person1);
        Person compareTest = pDao.find(person1.getPersonID());
        assertNotNull(compareTest);
        assertEquals(person1, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        pDao.insert(person1);
        Person compareTest = pDao.find(person1.getPersonID());
        assertNotNull(compareTest);
        pDao.clearPersons();
        assertNull(pDao.find(person1.getPersonID()));
    }

    @Test
    public void findEventsWhere_Pass() throws DataAccessException {
        ArrayList<Person> personsArray = new ArrayList<>();
        personsArray.add(person1);
        personsArray.add(person2);
        // insert persons with same associated username
        pDao.insert(person1);
        pDao.insert(person2);
        // make sure persons were inserted
        ArrayList<Person> testArray = pDao.findPersonsWhere_("Gale");
        assertNotNull(testArray);
        // make sure we got all persons with same associated username
        assertEquals(personsArray,testArray);
    }

    @Test
    public void findEventsWhere_Fail() throws DataAccessException {
        // person3 does NOT have the same associated username as person1 and person2
        Person person3 = new Person("lisa123","NOT_Gale","Lisa","Wyo","f",
                "papa", "gamma","lover123");
        pDao.insert(person1);
        pDao.insert(person2);
        pDao.insert(person3);
        // testArray is the list of persons we find in the db with the same associated username
        ArrayList<Person> testArray = pDao.findPersonsWhere_("Gale");
        // make sure we did not find persons with different associated usernames
        assertFalse(testArray.contains(person3));
    }

    @Test
    public void deleteEventsWhere_Pass() throws DataAccessException {
        pDao.insert(person1);
        pDao.insert(person2);
        // make sure persons were inserted
        ArrayList<Person> testArray = pDao.findPersonsWhere_("Gale");
        assertNotNull(testArray);
        pDao.deletePersonsWhere_("Gale");
        // make sure persons with username "Gale" were deleted
        testArray = pDao.findPersonsWhere_("Gale");
        assertEquals(EMPTY_ARRAY,testArray);
    }

    @Test
    public void deleteEventsWhere_Fail() throws DataAccessException {
        // person3 does NOT have the same associated username as person1 and person2
        Person person3 = new Person("lisa123","NOT_Gale","Lisa","Wyo","f",
                "papa", "gamma","lover123");
        pDao.insert(person1);
        pDao.insert(person2);
        pDao.insert(person3);
        // make sure persons were inserted into db
        ArrayList<Person> usernameGaleArray = pDao.findPersonsWhere_("Gale");
        ArrayList<Person> usernameNOTGaleArray = pDao.findPersonsWhere_("NOT_Gale");
        assertNotNull(usernameGaleArray);
        assertNotNull(usernameNOTGaleArray);
        //delete only "Gale" username persons
        pDao.deletePersonsWhere_("Gale");
        // make sure we didn't delete persons with a different username
        assertFalse(usernameGaleArray.contains(person3));
        ArrayList<Person> testNOTGaleArray = pDao.findPersonsWhere_("NOT_Gale");
        assertNotNull(testNOTGaleArray);
        assertTrue(testNOTGaleArray.contains(person3));
        // try to delete a person that's not there
        pDao.deletePersonsWhere_("gooooooo");
    }

    @Test
    public void clearTest() throws DataAccessException {
        pDao.insert(person1);
        // make sure person was inserted into db
        Person compareTest = pDao.find(person1.getPersonID());
        assertNotNull(compareTest);
        pDao.clearPersons();
        // make sure persons table was cleared
        assertNull(pDao.find(person1.getPersonID()));
    }

    @Test
    public void clearFail() throws DataAccessException{
        // clear an empty persons table
        pDao.clearPersons();
    }
}
