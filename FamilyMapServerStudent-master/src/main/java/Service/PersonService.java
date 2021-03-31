package Service;

import DAO.AuthTokenDao;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDao;
import Model.Person;
import Service.Result.PersonResult;

import java.util.Objects;

/**
 * Returns the single Person object with the specified ID.
 */
public class PersonService {

    /**
     * Errors: Invalid auth token, Invalid personID parameter, Requested person does not belong to this user, Internal server error
     * @param personID a persons specific ID to retrieve their information.
     * @param authToken authentication
     * @return PersonResult: Person with success response, else: error message response
     */
    public PersonResult getPerson(String personID,String authToken){
        Database db = new Database();

        try {
            // check if authtoken is valid (is already in db); if not, throw exception
            AuthTokenDao aDao = new AuthTokenDao(db.getConnection());
            if (aDao.find(authToken) == null) { throw new DataAccessException("Invalid auth token");}
            String aUsername = aDao.find(authToken).getAssociatedUsername();
            db.closeConnection(false);

            // check if personID is valid (is already in db); if not, throw exception
            PersonDao pDao = new PersonDao(db.getConnection());
            if (pDao.find(personID) == null) { throw new DataAccessException("Invalid personID");}
            Person person = pDao.find(personID);
            db.closeConnection(false);

            // check if requested person belongs to this user; if not, throw exception
            if (!Objects.equals(person.getAssociatedUsername(), aUsername)){
                throw new IllegalAccessException("Requested person does not belong to this user");}

            return new PersonResult(person.getAssociatedUsername(),personID,person.getFirstName(),person.getLastName(),
                    person.getGender(),person.getFatherID(),person.getMotherID(),person.getSpouseID(),true);

        } catch (IllegalAccessException e){
            // if IllegalAccessException is thrown, we do NOT need to close db because it's already closed
            e.printStackTrace();
            String message = "Error: " + e.toString();
            return new PersonResult(false,message);

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
            return new PersonResult(false, message);
        }
    }
}
