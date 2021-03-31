package Service;

import DAO.*;
import Generators.FamHistoryGenerator;
import Model.Person;
import Model.User;
import Service.Result.FillResult;

/**
 * Populates the server's database with generated data for the specified user name.
 * The required "username" parameter must be a user already registered with the server.
 * If there is any data in the database already associated with the given user name, it is deleted.
 * The optional “generations” parameter lets the caller specify the number of generations of ancestors to be generated,
 * and must be a non-negative integer (the default is 4, which results in 31 new persons each with associated events).
 */
public class FillService {
    Database db = new Database();
    User user;

    /**
     * Errors: Invalid username or generations parameter, Internal server error
     * @param username specific name for user
     * @param generations number of generations to populate (0=user, 1=user+parents, etc.)
     * @return FillResult: an object with Success/Error response.
     */
    public FillResult fill(String username, int generations){

        try {
            // check if user is already registered
            this.isRegistered(username);

            // check if generations parameter is non-negative
            if(generations < 0){ throw new IllegalArgumentException("Generations must be a non-negative integer.");}

            // if user is in db, delete any persons and events with associatedUsername
            PersonDao pDao = new PersonDao(db.getConnection());
            pDao.deletePersonsWhere_(username);
            db.closeConnection(true);
            EventDao eDao = new EventDao(db.getConnection());
            eDao.deleteEventsWhere_(username);
            db.closeConnection(true);

            // create person to populate db
            Person person = new Person(user.getPersonID(),user.getUsername(),user.getFirstName(),user.getLastName(),
                    user.getGender(),null,null,null);

            // generate famTree with associated username
            FamHistoryGenerator famGenerator = new FamHistoryGenerator();
            famGenerator.famGenerator(person,username,generations);
            this.populateDatabase(famGenerator);

            // return fillResult success: true
            int numPersons = famGenerator.getPersonList().size();
            int numEvents = famGenerator.getEventList().size();
            return new FillResult(true, "Successfully added " + numPersons + " persons and " + numEvents + " events to the database.");

        } catch(IllegalArgumentException e) {
            // if IllegalArgument thrown, we do NOT need to close db because it's already closed
            e.printStackTrace();
            String message = "Error: " + e.toString();
            return new FillResult(false,message);
        } catch(DataAccessException e){
            // any other thrown exception, we need to close the database
            String message = "Error: " + e.toString();
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException dae) {
                dae.printStackTrace();
                message = "Error: " + dae.toString();
            }
            return new FillResult(false,message);
        }
    }

    // The required "username" parameter must be a user already registered with the server
    private void isRegistered(String username) throws DataAccessException {
        UserDao uDao = new UserDao(db.getConnection());
        if ((user = uDao.find(username)) == null){ throw new DataAccessException("Invalid username");}
        db.closeConnection(false);
        // if doesn't throw an error, user is registered
    }

    // fill database with generated family history data
    private void populateDatabase(FamHistoryGenerator famGenerator) throws DataAccessException{
        Database db = new Database();
        // populate db with famTree
        PersonDao pDao2 = new PersonDao(db.getConnection());
        for (Model.Person Person : famGenerator.getPersonList()) {
            pDao2.insert(Person);
        }
        db.closeConnection(true);

        // populate db with famEvents
        EventDao eDao2 = new EventDao(db.getConnection());
        for (Model.Event Event : famGenerator.getEventList()) {
            eDao2.insert(Event);
        }
        db.closeConnection(true);
    }
}
