package Service;
import DAO.AuthTokenDao;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDao;
import Model.Event;
import Service.Result.EventsResult;

import java.util.ArrayList;

/**
 * Returns ALL events for ALL family members of the current user.
 */
public class EventsService {
    /**
     * The current user is determined from the provided auth token.
     * Errors: Invalid auth token, Internal server error
     * @return EventsResult: Array of Event objects with success, else: error message response
     */
    public EventsResult getAllEvents(String authToken) throws NullPointerException{
        Database db = new Database();
        ArrayList<Event> eventsArray = new ArrayList<>();

        try{
            // check if authtoken is valid (is already in db); if not, throw exception
            AuthTokenDao aDao = new AuthTokenDao(db.getConnection());
            if (aDao.find(authToken) == null){ throw new IllegalAccessException("Invalid auth token"); }
            String username = aDao.find(authToken).getAssociatedUsername();
            db.closeConnection(false);

            // find all events with specified username
            EventDao eDao = new EventDao(db.getConnection());
            eventsArray = eDao.findEventsWhere_(username);
            db.closeConnection(false);

            return new EventsResult(eventsArray,true);

        } catch (DataAccessException | IllegalAccessException e) {
            // if exception is thrown, we need to close database
            String message = "Error: " + e.toString();
            e.printStackTrace();

            try {
                db.closeConnection(false);
            } catch (DataAccessException dae) {
                dae.printStackTrace();
                message = "Error: " + dae.toString();
            }
            return new EventsResult(false,message);
        }
    }
}
