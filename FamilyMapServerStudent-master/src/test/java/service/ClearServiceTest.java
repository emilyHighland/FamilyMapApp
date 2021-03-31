package service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDao;
import Model.User;
import Service.ClearService;
import Service.Result.ClearResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ClearServiceTest {
    ClearService clearService = new ClearService();
    ClearResult resultTest;
    Database db = new Database();
    User testUser;

    @BeforeEach
    public void setup() throws DataAccessException{
        db.openConnection();
        // insert user information to test if clearService actually clears the data
        testUser = new User("Gale", "i<3puppies", "gale@gmail.com",
                "Gale", "Highland", "f", "Gale123A");
        UserDao uDao = new UserDao(db.getConnection());
        uDao.insert(testUser);
        db.closeConnection(true);
    }

    @Test
    // test if clear service actually clears the data
    public void clearPass() throws DataAccessException{
        // test clear service (has own open/close connection)
        resultTest = clearService.clear();
        // find should return null if db cleared
        UserDao uDao2 = new UserDao(db.getConnection());
        assertNull(uDao2.find(testUser.getUsername()));
        db.closeConnection(true);
    }

    @Test
    // test if clear service returns correct object
    public void clearEqualsTest() throws DataAccessException{
        resultTest = clearService.clear();
        // objects should be equal
        ClearResult compareTest = new ClearResult(true,"Clear succeeded.");
        assertEquals(compareTest,resultTest);
    }
}
