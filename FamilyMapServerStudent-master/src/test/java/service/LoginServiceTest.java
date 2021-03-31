package service;

import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDao;
import Model.User;
import Service.LoginService;
import Service.Request.LoginRequest;
import Service.Result.LoginResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
    LoginService loginService = new LoginService();
    Database db = new Database();
    User testUser;

    @BeforeEach
    public void setup() throws DataAccessException {
        db.getConnection();
        db.clearTables();
        db.closeConnection(true);

        // user must already be registered
        testUser = new User("ashlynRae", "iluvpuppiestoo", "ash1@gmail.com",
                "Ashley", "High", "f", "ash1");
        UserDao uDao = new UserDao(db.getConnection());
        uDao.insert(testUser);
        db.closeConnection(true);
    }

    @Test
    public void loginPass() throws DataAccessException {
        // test when username is already in db, get a successful login
        LoginRequest loginRequest = new LoginRequest(testUser.getUsername(),testUser.getPassword());
        LoginResult loginResult = loginService.login(loginRequest);
        assertNotNull(loginResult);
        assertTrue(loginResult.isSuccess());
        assertEquals(loginResult.getUsername(),testUser.getUsername());
        assertEquals(loginResult.getPersonID(),testUser.getPersonID());
    }

    @Test
    public void loginFail(){
        // test when user is not registered
        LoginRequest loginRequest = new LoginRequest("olympian123","iheartbikes");
        LoginResult loginResult = loginService.login(loginRequest);
        assertNotNull(loginResult);
        assertFalse(loginResult.isSuccess());
        assertTrue(loginResult.getMessage().contains("Invalid username"));

        // test incorrect password
        loginRequest = new LoginRequest(testUser.getUsername(),"this_is_the_wrong_password");
        loginResult = loginService.login(loginRequest);
        assertNotNull(loginResult);
        assertFalse(loginResult.isSuccess());
        assertTrue(loginResult.getMessage().contains("Invalid password"));
    }
}
