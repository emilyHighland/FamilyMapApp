package Service;
import DAO.DataAccessException;
import DAO.Database;
import Service.Result.ClearResult;

/**
 * Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
 */
public class ClearService {

    /**
     * Errors: Internal server error
     * @return ParentResult: an object with Success/Error response.
     */
    public ClearResult clear() {
        Database db = new Database();
        try {
            db.openConnection();
            db.clearTables();
            db.closeConnection(true);
            return new ClearResult(true,"Clear succeeded.");

        } catch(DataAccessException e){
            // if exception is thrown, we need to close database
            e.printStackTrace();
            String message = "Error: " + e.toString();

            try {
                db.closeConnection(false);
            } catch (DataAccessException dae) {
                dae.printStackTrace();
                message = "Error: " + dae.toString();
            }
            return new ClearResult(false,message);
        }
    }
}
