package service;

import DAO.AuthTokenDao;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDao;
import Model.AuthToken;
import Model.Person;
import Service.PersonsService;
import Service.Result.PersonsResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PersonsServiceTest {
    PersonsService personsService = new PersonsService();
    ArrayList<Person> personsList = new ArrayList<>();
    Database db = new Database();
    AuthTokenDao aDao;
    AuthToken authT1;
    PersonDao pDao;
    Person person1;
    Person person2;
    Person person3;

    @BeforeEach
    public void setup() throws DataAccessException {
        // make sure db is cleared before testing
        db.getConnection();
        db.clearTables();
        db.closeConnection(true);

        // create events for events array (2 with same username, 1 with different username)
        person1 = new Person("ash1","ashleyRae","Ashley","Highland",
                "f","Daddio123","Mommio123","michael123");
        person2 = new Person("michael123","ashleyRae","Michael","Highland",
                "f","Dad1","Mom1","ash1");
        person3 = new Person("cheesy123","em","emily","high",
                "f","Dad1","Mom1","1Love");

        personsList.add(person1);
        personsList.add(person2);
        personsList.add(person3);

        // insert personsArray into db
        pDao = new PersonDao(db.getConnection());
        for (Person person : personsList){
            pDao.insert(person);
        }
        // make sure to close all connections before opening a new one
        db.closeConnection(true);

        // create authtokens
        authT1 = new AuthToken("ashleyRae","angel123A");
    }

    @Test
    // make sure returns an array of all Event objects of specified user
    public void eventServicePass() throws DataAccessException {
        // insert authtokens
        aDao = new AuthTokenDao(db.getConnection());
        aDao.insert(authT1);
        db.closeConnection(true);

        // test if eventsService returns the all events with associated username
        PersonsResult personsResult = personsService.getAllPersons(authT1.getAuthtoken());
        assertNotNull(personsResult);
        assertTrue(personsResult.isSuccess());
        assertNotNull(personsResult.getData());
        assertTrue(personsResult.getData().contains(person1));
        assertTrue(personsResult.getData().contains(person2));
        assertFalse(personsResult.getData().contains(person3));
    }

    @Test
    // check for invalid authtoken, invalid eventID; test when event doesn't belong to specified user
    public void eventServiceFail() throws DataAccessException {
        // insert authtokens
        aDao = new AuthTokenDao(db.getConnection());
        aDao.insert(authT1);
        db.closeConnection(true);

        // test if eventsService returns success: false with correct error message
        PersonsResult personsResult = personsService.getAllPersons("WRONG_authentication");
        assertNotNull(personsResult);
        assertFalse(personsResult.isSuccess());
        assertNull(personsResult.getData());
        String expectedMessage = "Invalid auth token";
        assertTrue(personsResult.getMessage().contains(expectedMessage));
    }
}
