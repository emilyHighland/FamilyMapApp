package Service;

import DAO.AuthTokenDao;
import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDao;
import Model.Person;
import Service.Result.PersonsResult;

import java.util.ArrayList;

/**
 * Returns ALL family members of the current user. The current user is determined from the provided auth token.
 */
public class PersonsService {
    /**
     * Errors: Invalid auth token, Internal server error
     * @return PersonsResult: Array of Person objects with success, else: error message response
     */
    public PersonsResult getAllPersons(String authToken){
        Database db = new Database();
        ArrayList<Person> personsArray = new ArrayList<>();

        try{
            // check if authtoken is valid (is already in db); if not, throw exception
            AuthTokenDao aDao = new AuthTokenDao(db.getConnection());
            if (aDao.find(authToken) == null){ throw new IllegalAccessException("Invalid auth token"); }
            String username = aDao.find(authToken).getAssociatedUsername();
            db.closeConnection(false);

            // find all persons with specified username
            PersonDao pDao = new PersonDao(db.getConnection());
            personsArray = pDao.findPersonsWhere_(username);
            db.closeConnection(false);

            return new PersonsResult(personsArray,true);

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
            return new PersonsResult(false,message);
        }
    }
}
