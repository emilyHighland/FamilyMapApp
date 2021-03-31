package service;

import DAO.AuthTokenDao;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDao;
import Model.AuthToken;
import Model.Event;
import Service.EventsService;
import Service.Result.EventsResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EventsServiceTest {
    EventsService eventsService = new EventsService();
    ArrayList<Event> eventsList = new ArrayList<>();
    Database db = new Database();
    AuthTokenDao aDao;
    EventDao eDao;
    AuthToken authT1;
    Event event1;
    Event event2;
    Event event3;

    @BeforeEach
    public void setup() throws DataAccessException {
        // make sure db is cleared before testing
        db.getConnection();
        db.clearTables();
        db.closeConnection(true);

        // create events for events array (2 with same username, 1 with different username)
        event1 = new Event("pickleballing1st","Victoria","vic_123",35.9f,140.1f,
                "Japan","Ushiku","pickleballing",2010);
        event2 = new Event("pickleballing2nd","Victoria","phillis123",35.9f,140.1f,
                "Japan","Ushiku","pickleballing",2010);
        event3 = new Event("pickleballing1stAgain","Gale","gale123",45.9f,135.1f,
                "Japan","Ushiku","pickleballing",2020);
        eventsList.add(event1);
        eventsList.add(event2);
        eventsList.add(event3);

        // insert eventsArray into db
        eDao = new EventDao(db.getConnection());
        for (Event event : eventsList){
            eDao.insert(event);
        }
        // make sure to close all connections before opening a new one
        db.closeConnection(true);

        // create authtokens
        authT1 = new AuthToken("Victoria","authenticate");
    }

    @Test
    // make sure returns an array of all Event objects of specified user
    public void eventServicePass() throws DataAccessException {
        // insert authtokens
        aDao = new AuthTokenDao(db.getConnection());
        aDao.insert(authT1);
        db.closeConnection(true);

        // test if eventsService returns the all events with associated username
        EventsResult eventsResult = eventsService.getAllEvents(authT1.getAuthtoken());
        assertNotNull(eventsResult);
        assertTrue(eventsResult.isSuccess());
        assertNotNull(eventsResult.getData());
        assertTrue(eventsResult.getData().contains(event1));
        assertTrue(eventsResult.getData().contains(event2));
        assertFalse(eventsResult.getData().contains(event3));
    }

    @Test
    // check for invalid authtoken, invalid eventID; test when event doesn't belong to specified user
    public void eventServiceFail() throws DataAccessException {
        // insert authtokens
        aDao = new AuthTokenDao(db.getConnection());
        aDao.insert(authT1);
        db.closeConnection(true);

        // test if eventsService returns success: false with correct error message
        EventsResult eventsResult = eventsService.getAllEvents("NOT_correct_authentication");
        assertNotNull(eventsResult);
        assertFalse(eventsResult.isSuccess());
        assertNull(eventsResult.getData());
        String expectedMessage = "Invalid auth token";
        assertTrue(eventsResult.getMessage().contains(expectedMessage));
    }
}
