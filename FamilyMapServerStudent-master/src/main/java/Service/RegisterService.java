package Service;

import DAO.*;
import Generators.FamHistoryGenerator;
import Model.User;
import Service.Request.LoginRequest;
import Service.Request.RegisterRequest;
import Service.Result.LoginResult;
import Service.Result.RegisterResult;

/**
 * Creates a new user account, generates 4 generations of ancestor data for the new user,
 *  logs the user in, and returns an auth token.
 */
public class RegisterService {
    /**
     * Errors: Request property missing or has invalid value, Username already taken by another user, Internal server error
     * @param RRequest: an object with information to create new user.
     * @return RegisterResult: object with user info, auth token, else: error response
     */
    public RegisterResult register(RegisterRequest RRequest) {
        FamHistoryGenerator famGenerator = new FamHistoryGenerator();
        Database db = new Database();

        try {

            // create default 4 generations of ancestor data, returning user
            User user = famGenerator.generateData(RRequest);

            // insert User, generational data, and AuthToken into db
            UserDao uDao = new UserDao(db.openConnection());
            uDao.insert(user);
            db.closeConnection(true);

            // push generated persons to db
            PersonDao pDao = new PersonDao(db.getConnection());
            for (Model.Person Person : famGenerator.getPersonList()) {
                pDao.insert(Person);
            }
            db.closeConnection(true);

            // push generated events to db
            EventDao eDao = new EventDao(db.getConnection());
            for (Model.Event Event : famGenerator.getEventList()) {
                eDao.insert(Event);
            }
            db.closeConnection(true);

            // log user in (using loginService) -> returns authtoken
            LoginService loginService = new LoginService();
            LoginRequest loginRequest = new LoginRequest(user.getUsername(), user.getPassword());
            LoginResult loginResult = loginService.login(loginRequest);
            String authtoken = loginResult.getAuthtoken();

            return new RegisterResult(authtoken,RRequest.getUsername(), user.getPersonID(),true);

        } catch (DataAccessException e) {
            String message = "Error: " + e.getMessage();
            e.printStackTrace();
            try{
                db.closeConnection(false);
            }catch(DataAccessException e1){
                message = "Error: " + e1.getMessage();
                e.printStackTrace();
            }
            return new RegisterResult(false,message);
        }
    }
}
