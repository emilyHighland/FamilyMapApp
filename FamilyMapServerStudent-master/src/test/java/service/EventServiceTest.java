package service;

import DAO.AuthTokenDao;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDao;
import Model.AuthToken;
import Model.Event;
import Service.EventService;
import Service.Result.EventResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {
    EventService eventService = new EventService();
    Database db = new Database();
    Event event;
    EventDao eDao;

    @BeforeEach
    public void setup() throws DataAccessException {
        // make sure db is cleared before testing
        Connection conn = db.getConnection();
        db.clearTables();
        // insert an event for testing
        event = new Event("pickleballing1st","Victoria","vic_123A",35.9f,140.1f,
                "Japan","Ushiku","pickleballing",2020);
        eDao = new EventDao(conn);
        eDao.insert(event);
        // make sure to close all connections before opening a new one
        db.closeConnection(true);
    }

    @Test
    // make sure returns a single Event object with the specified ID
    public void eventServicePass() throws DataAccessException {
        // insert authtoken with associated username
        AuthToken authT = new AuthToken("Victoria","this_is_my_authentication");
        AuthTokenDao aDao = new AuthTokenDao(db.getConnection());
        aDao.insert(authT);
        // make sure to close all connections before opening a new one
        db.closeConnection(true);

        // test if eventService returns the event asked for with success: true
        EventResult eventResult = eventService.singleEvent("pickleballing1st", authT.getAuthtoken());
        assertNotNull(eventResult);
        assertEquals(eventResult.getEventID(),event.getEventID());
        assertTrue(eventResult.isSuccess());
    }

    @Test
    // check for invalid authtoken, invalid eventID; test when event doesn't belong to specified user
    public void eventServiceFail() throws DataAccessException {
        // insert authtoken with associated username
        AuthToken authT = new AuthToken("Victoria","this_is_my_authentication");
        AuthTokenDao aDao = new AuthTokenDao(db.getConnection());
        aDao.insert(authT);
        // make sure to close all connections before opening a new one
        db.closeConnection(true);

        // test if eventService returns success: false with correct error message
        EventResult eventResult = eventService.singleEvent("WRONG_id", "NOT_correct_authentication");
        assertNotNull(eventResult);
        assertFalse(eventResult.isSuccess());
        assertNotEquals(eventResult.getEventID(),event.getEventID());
    }
}
