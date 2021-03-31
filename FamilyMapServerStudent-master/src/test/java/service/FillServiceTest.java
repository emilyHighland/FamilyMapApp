package service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDao;
import Model.User;
import Service.FillService;
import Service.Result.FillResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    FillService fillService = new FillService();
    Database db = new Database();
    User user1;
    int numGenerations = 2;
    int numPersons = 7;
    int numEvents = numPersons*3 - 2;

    @BeforeEach
    public void setup() throws DataAccessException{
        Connection conn = db.getConnection();
        db.clearTables();
        db.closeConnection(true);
        user1 = new User("Gale","i<3puppies","gale@gmail.com","Gale",
                "Highland","f","Gale123A");
    }

    @Test
    public void fillPass() throws DataAccessException {
        // insert user into db
        UserDao uDao = new UserDao(db.getConnection());
        uDao.insert(user1);
        User testUser = uDao.find("Gale");
        db.closeConnection(true);
        // make sure user was actually inserted into db
        assertNotNull(testUser);

        // make sure fillService returns success: true
        FillResult fillResult = fillService.fill("Gale",numGenerations);
        assertTrue(fillResult.isSuccess());
        String expectedMessage = "Successfully added " + numPersons + " persons and " + numEvents + " events to the database.";
        String actualMessage = fillResult.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    // check for invalid username or generations parameter
    public void fillFail() throws DataAccessException {
        // insert user into db
        UserDao uDao = new UserDao(db.getConnection());
        uDao.insert(user1);
        User testUser = uDao.find("Gale");
        db.closeConnection(true);
        // make sure user was actually inserted into db
        assertNotNull(testUser);

        // make sure fillService returns success: false with correct error message
        FillResult fillResult = fillService.fill("chill",numGenerations);
        assertFalse(fillResult.isSuccess());
        String expectedMessage = "Invalid username";
        String actualMessage = fillResult.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
