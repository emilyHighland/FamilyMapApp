package Service;

import DAO.AuthTokenDao;
import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDao;
import Generators.UUIDGenerator;
import Model.AuthToken;
import Model.User;
import Service.Request.LoginRequest;
import Service.Result.LoginResult;

/**
 * Logs in the user and returns an auth token.
 */
public class LoginService {
    /**
     * Errors: Request property missing or has invalid value, Internal server error
     * @param loginRequest: an object with information to request a login.
     * @return LoginResult: auth token with success, else: error message response
     */
    public LoginResult login(LoginRequest loginRequest) {
        Database db = new Database();
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        try {
            // check db for correct username/password
            UserDao uDao = new UserDao(db.getConnection());
            if (uDao.find(username) == null) throw new DataAccessException("Invalid username.");
            User user = uDao.find(username);
            db.closeConnection(false);
            if (!password.equals(user.getPassword())) throw new IllegalAccessException("Invalid password.");

            // if username and password are correct, generate authTokenID
            UUIDGenerator generateID = new UUIDGenerator();
            String authtoken = generateID.UUIDGenerator();

            // make authToken object
            AuthToken authTokenModel = new AuthToken(loginRequest.getUsername(),authtoken);

            // push to db
            AuthTokenDao aDao = new AuthTokenDao(db.getConnection());
            aDao.insert(authTokenModel);
            db.closeConnection(true);

            return new LoginResult(authtoken,loginRequest.getUsername(),user.getPersonID(),true);

        } catch (IllegalAccessException e) {
            // if IllegalAccessException is thrown, we do NOT need to close db because it's already closed
            e.printStackTrace();
            String message = "Error: " + e.toString();
            return new LoginResult(message,false);

        } catch (DataAccessException e) {
            // in case of invalid username or password, or other DataAccessException we need to make sure db gets closed
            String message = e.getMessage();
            e.printStackTrace();

            try{
                db.closeConnection(false);
            }catch(DataAccessException e1){
                message = e1.getMessage();
                e.printStackTrace();
            }
            return new LoginResult("Error: " + message,false);
        }
    }
}
