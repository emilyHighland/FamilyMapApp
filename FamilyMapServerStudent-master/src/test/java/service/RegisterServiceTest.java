package service;

import DAO.DataAccessException;
import DAO.Database;
import Service.RegisterService;
import Service.Request.RegisterRequest;
import Service.Result.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {
    RegisterService registerService = new RegisterService();
    RegisterRequest registerRequest;
    RegisterResult registerResult;
    Database db = new Database();

    @BeforeEach
    public void setup() throws DataAccessException {
        db.getConnection();
        db.clearTables();
        db.closeConnection(true);

        registerRequest = new RegisterRequest("ash123","ilikecats","ash@gmail.com",
                "Ashlyn","Book","f");
    }

    @Test
    public void registerPass() throws DataAccessException {
        registerResult = registerService.register(registerRequest);
        assertNotNull(registerResult);
        assertTrue(registerResult.isSuccess());
    }
}
