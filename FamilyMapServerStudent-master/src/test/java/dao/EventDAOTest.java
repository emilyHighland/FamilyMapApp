package dao;

import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDao;
import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventDAOTest {
    private Database db;
    private EventDao eDao;
    private Event bestEvent;
    private Event event2;
    private final ArrayList<Event> EMPTY_ARRAY = new ArrayList<>();

    @BeforeEach
    public void setUp() throws DataAccessException {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        bestEvent = new Event("Biking_123A","Gale","Gale123A",35.9f,140.1f,
                "Japan","Ushiku","Biking_Around",2016);
        event2 = new Event("BikingAgain_123","Gale","Phillis",45.9f,135.1f,
                "Japan","Ushiku","Biking_Around_Again",2018);
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the EventDAO so it can access the database
        eDao = new EventDao(conn);
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
        eDao.insert(bestEvent);
        //So lets use a find method to get the event that we just put in back out
        Event compareTest = eDao.find(bestEvent.getEventID());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        eDao.insert(bestEvent);
        //but our sql table is set up so that "eventID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()->  eDao.insert(bestEvent));
    }

    @Test
    public void findPass() throws DataAccessException {
        eDao.insert(bestEvent);
        Event compareTest = eDao.find(bestEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        eDao.insert(bestEvent);
        Event compareTest = eDao.find(bestEvent.getEventID());
        assertNotNull(compareTest);
        eDao.clearEvents();
        assertNull(eDao.find(compareTest.getEventID()));
    }

    @Test
    public void findEventsWhere_Pass() throws DataAccessException {
        ArrayList<Event> eventsArray = new ArrayList<>();
        eventsArray.add(bestEvent);
        eventsArray.add(event2);
        // insert events with same associated username
        eDao.insert(bestEvent);
        eDao.insert(event2);
        // make sure events were inserted
        ArrayList<Event> testArray = eDao.findEventsWhere_("Gale");
        assertNotNull(testArray);
        // make sure we got all events with the same associated username
        assertEquals(eventsArray,testArray);
    }

    @Test
    public void findEventsWhere_Fail() throws DataAccessException {
        // event3 does NOT have the same associated username as bestEvent and event2
        Event event3 = new Event("BikingAroundAgain_123","NOT_Gale","lisa123",35.9f, 130.1f,"Japan","Ushiku","Biking",2020);
        eDao.insert(bestEvent);
        eDao.insert(event2);
        eDao.insert(event3);
        // testArray is the list of events we find in the db with the same associated username
        ArrayList<Event> testArray = eDao.findEventsWhere_("Gale");
        // make sure we did not find events with different associated usernames
        assertFalse(testArray.contains(event3));
    }

    @Test
    public void deleteEventsWhere_Pass() throws DataAccessException {
        eDao.insert(bestEvent);
        eDao.insert(event2);
        // make sure events were inserted
        ArrayList<Event> testArray = eDao.findEventsWhere_("Gale");
        assertNotNull(testArray);
        eDao.deleteEventsWhere_("Gale");
        // make sure events with username "Gale" were deleted
        testArray = eDao.findEventsWhere_("Gale");
        assertEquals(EMPTY_ARRAY,testArray);
    }

    @Test
    public void deleteEventsWhere_Fail() throws DataAccessException {
        // event3 does NOT have the same associated username as bestEvent and event2
        Event event3 = new Event("BikingAroundAgain_123","NOT_Gale","lisa123",35.9f, 130.1f,"Japan","Ushiku","Biking",2020);
        eDao.insert(bestEvent);
        eDao.insert(event2);
        eDao.insert(event3);
        // make sure events were inserted
        ArrayList<Event> usernameGaleArray = eDao.findEventsWhere_("Gale");
        ArrayList<Event> usernameNOTGaleArray = eDao.findEventsWhere_("NOT_Gale");
        assertNotNull(usernameGaleArray);
        assertNotNull(usernameNOTGaleArray);
        //delete only "Gale" username events
        eDao.deleteEventsWhere_("Gale");
        // make sure we didn't delete events with a different username
        assertFalse(usernameGaleArray.contains(event3));
        ArrayList<Event> testNOTGaleArray = eDao.findEventsWhere_("NOT_Gale");
        assertNotNull(testNOTGaleArray);
        assertTrue(testNOTGaleArray.contains(event3));
        // try to delete an event that's not there
        eDao.deleteEventsWhere_("gooooooo");
    }

    @Test
    public void clearPass() throws DataAccessException {
        eDao.insert(bestEvent);
        // make sure event was inserted into db
        Event compareTest = eDao.find(bestEvent.getEventID());
        assertNotNull(compareTest);
        eDao.clearEvents();
        // make sure events table was cleared
        assertNull(eDao.find(compareTest.getEventID()));
    }

    @Test
    public void clearFail() throws DataAccessException{
        // clear an empty events table
        eDao.clearEvents();
    }
}
