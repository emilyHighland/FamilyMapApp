package Service;

import DAO.*;
import Model.Event;
import Model.Person;
import Model.User;
import Service.Request.LoadRequest;
import Service.Result.LoadResult;

import java.util.ArrayList;

/**
 * Clears all data from the database (just like the /clear API), and then loads the posted user, person, and event data into the database.
 */
public class LoadService {

    /**
     * Errors: Invalid request data (missing values, invalid values, etc.), Internal server error
     * @param loadRequest: an object with information to be added to the database.
     * @return LoadResult: an object with Success/Error response.
     */
    public LoadResult load(LoadRequest loadRequest) {
        Database db = new Database();

        try {
            // make sure we have something to load
            if (loadRequest == null){ throw new IllegalArgumentException("Invalid request data");}

            // clear all data from db
            ClearService clearService = new ClearService();
            clearService.clear();

            // load user array data
            ArrayList<User> users = loadRequest.getUsers();
            UserDao uDao = new UserDao(db.openConnection());
            for (Model.User User : users) {
                uDao.insert(User);
            }
            db.closeConnection(true);

            // load person array data
            ArrayList<Person> persons = loadRequest.getPersons();
            PersonDao pDao = new PersonDao(db.openConnection());
            for (Model.Person Person : persons) {
                pDao.insert(Person);
            }
            db.closeConnection(true);

            // load event array data
            ArrayList<Event> events = loadRequest.getEvents();
            EventDao eDao = new EventDao(db.openConnection());
            for (Model.Event Event : events) {
                eDao.insert(Event);
            }
            db.closeConnection(true);

            return new LoadResult(true,"Successfully added " + users.size() + " users, " + persons.size() +
                    " persons, and " + events.size() + " events to the database.");

        } catch(IllegalArgumentException e) {
            // if IllegalArgument thrown, we do NOT need to close db because it's already closed
            e.printStackTrace();
            String message = "Error: " + e.toString();
            return new LoadResult(false,message);

        } catch(DataAccessException e){
            // if exception is thrown, we need to close db
            String message= "Error: " + e.toString();
            e.printStackTrace();

            try {
                db.closeConnection(false);
            } catch (DataAccessException dae) {
                dae.printStackTrace();
                message = dae.toString();
            }
            return new LoadResult(false,message);
        }
    }
}
