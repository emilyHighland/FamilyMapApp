package service;

import DAO.*;
import Model.Event;
import Model.Person;
import Model.User;
import Service.LoadService;
import Service.Request.LoadRequest;
import Service.Result.LoadResult;
import Utilites.JSONSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {
    LoadService loadService = new LoadService();
    Database db = new Database();
    User testUser;

    @BeforeEach
    public void setup() throws DataAccessException {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);

        // insert user information to test if loadService clears existing data
        testUser = new User("ashlynRae", "i<3puppies", "ash1@gmail.com",
                "Ashley", "High", "f", "ash1");
        UserDao uDao = new UserDao(db.getConnection());
        uDao.insert(testUser);
        db.closeConnection(true);
    }

    @Test
    public void loadPass() throws DataAccessException {
        // make sure load request is not null and that data parsed correctly
        LoadRequest loadRequest = this.loadFile();
        assertNotNull(loadRequest);
        LoadResult loadResult = loadService.load(loadRequest);

        // check that db was cleared
        UserDao uDao1 = new UserDao(db.getConnection());
        assertNull(uDao1.find(testUser.getUsername()));
        db.closeConnection(false);

        // check if load result was success
        assertTrue(loadResult.isSuccess());

        // PERSONS
        // see if we can find persons array in db
        PersonDao pDao = new PersonDao(db.getConnection());
        ArrayList<Person> personTestArray = pDao.findPersonsWhere_("sheila");
        ArrayList<Person> patrickPersonsArray = pDao.findPersonsWhere_("patrick");
        personTestArray.addAll(patrickPersonsArray);
        db.closeConnection(false);

        // if persons array is found in db, does it match our load result persons array
        assertEquals(loadRequest.getPersons(),personTestArray);

        // EVENTS
        // see if we can find events array in db
        EventDao eDao = new EventDao(db.getConnection());
        ArrayList<Event> eventTestArray = eDao.findEventsWhere_("sheila");
        ArrayList<Event> patrickEventsArray = eDao.findEventsWhere_("patrick");
        eventTestArray.addAll(patrickEventsArray);
        db.closeConnection(false);

        // if persons array is found in db, does it match our load result persons array
        assertEquals(loadRequest.getEvents(),eventTestArray);

        // USERS
        // see if we can find users in db
        UserDao uDao = new UserDao(db.getConnection());
        ArrayList<User> userTestArray = new ArrayList<>();
        userTestArray.add(uDao.find("sheila"));
        userTestArray.add(uDao.find("patrick"));
        db.closeConnection(false);

        // if persons array is found in db, does it match our load result persons array
        assertEquals(loadRequest.getUsers(),userTestArray);
    }

    @Test
    public void loadFail() throws DataAccessException {
        // check if load result returned success: false with proper error message
        LoadResult loadResult = loadService.load(null);
        assertFalse(loadResult.isSuccess());
        String expectedMessage = "Invalid request data";
        assertTrue(loadResult.getMessage().contains(expectedMessage));
    }


    // load test file
    private LoadRequest loadFile() {
        JSONSerializer jsonSerializer = new JSONSerializer();
        File file = new File("passoffFiles/LoadData.json");

        try {
            return jsonSerializer.parse(file, LoadRequest.class);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            System.out.printf("Unable to parse file %s because of exception: %s\n", file, ex.toString());
        }
        return null;
    }
}
