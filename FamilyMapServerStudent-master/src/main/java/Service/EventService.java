package Service;

import DAO.AuthTokenDao;
import DAO.DataAccessException;
import DAO.Database;
import DAO.EventDao;
import Model.Event;
import Service.Result.EventResult;

import java.util.Objects;

/**
 * Returns the single Event object with the specified ID.
 */
public class EventService {
    /**
     * Errors: Invalid auth token, Invalid eventID parameter, Requested event does not belong to this user, Internal server error
     * @param eventID a specific event ID
     * @param authToken authentication token
     * @return EventResult: Event with success response, else: error message response
     */
    public EventResult singleEvent(String eventID,String authToken) {
        Database db = new Database();
        try{
            // check if authtoken is valid (is already in db); if not, throw exception
            AuthTokenDao aDao = new AuthTokenDao(db.getConnection());
            if (aDao.find(authToken) == null){ throw new DataAccessException("Invalid auth token"); }
            String aUsername = aDao.find(authToken).getAssociatedUsername();
            db.closeConnection(false);

            // check if eventID is valid (is already in db); if not, throw exception
            EventDao eDao = new EventDao(db.getConnection());
            if (eDao.find(eventID) == null){ throw new DataAccessException("Invalid eventID");}
            Event event = eDao.find(eventID);
            db.closeConnection(false);

            // check if requested event belongs to this user; if not, throw exception
            if (!Objects.equals(event.getAssociatedUsername(),aUsername)){
                throw new IllegalAccessException("Requested event does not belong to this user");}

            return new EventResult(event.getAssociatedUsername(),eventID,event.getPersonID(),event.getLatitude(),event.getLongitude(),
                    event.getCountry(),event.getCity(),event.getEventType(),event.getYear(),true);

        } catch (IllegalAccessException i){
            // if IllegalAccessException is thrown, we do NOT need to close db because it's already closed
            i.printStackTrace();
            String message = "Error: " + i.toString();
            return new EventResult(false,message);

        } catch (DataAccessException e) {
            // in case of invalid authtoken, ID, or other DataAccessException we need to make sure db is closed
            String message = "Error: " + e.toString();
            e.printStackTrace();

            try {
                db.closeConnection(false);
            } catch (DataAccessException dae) {
                dae.printStackTrace();
                message = "Error: " + dae.toString();
            }
            return new EventResult(false,message);
        }
    }
}
