package service;

import DAO.AuthTokenDao;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDao;
import Model.AuthToken;
import Model.Person;
import Service.PersonService;
import Service.Result.PersonResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonServiceTest {
    PersonService personService = new PersonService();
    Database db = new Database();
    Person person;
    AuthToken authT;

    @BeforeEach
    public void setup() throws DataAccessException {
        db.getConnection();
        db.clearTables();
        db.closeConnection(true);

        // insert person for testing
        person = new Person("Sheila_Parker","sheila","Sheila",
                "Parker","f","Patrick_Spencer","Gale_Spencer",null);
        PersonDao pDao = new PersonDao(db.getConnection());
        pDao.insert(person);
        db.closeConnection(true);

        // insert authtoken with associated username
        authT = new AuthToken("sheila","things123");
        AuthTokenDao aDao = new AuthTokenDao(db.getConnection());
        aDao.insert(authT);
        db.closeConnection(true);
    }

    @Test
    // make sure returns a single Person object with the specified ID
    public void personServicePass() throws DataAccessException {
        // test if personService returns the person asked for with success: true
        PersonResult personResult = personService.getPerson("Sheila_Parker", authT.getAuthtoken());
        assertNotNull(personResult);
        assertEquals(personResult.getPersonID(),person.getPersonID());
        assertTrue(personResult.isSuccess());
    }

    @Test
    // check for invalid authtoken, invalid eventID; test when event doesn't belong to specified user
    public void personServiceFail() throws DataAccessException {
        // test if personService returns success: false with different ids
        PersonResult personResult = personService.getPerson("WRONG_id", "NOT_correct_authentication");
        assertNotNull(personResult);
        assertFalse(personResult.isSuccess());
        assertNotEquals(personResult.getPersonID(),person.getPersonID());
    }
}
