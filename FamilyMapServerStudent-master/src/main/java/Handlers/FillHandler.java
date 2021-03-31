package Handlers;

import Service.FillService;
import Service.Result.FillResult;
import Utilites.IOStream;
import Utilites.JSONSerializer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * URL Path: /fill/
 * Description: Populates the server's database with generated data for the specified user name.
 *  The required "username" parameter must be a user already registered with the server.
 *  If there is any data in the database already associated with the given user name, it is deleted.
 *  The optional “generations” parameter lets the caller specify the number of generations of ancestors to be generated,
 *  and must be a non-negative integer (the default is 4, which results in 31 new persons each with associated events).
 * HTTP Method: POST
 * Auth Token Required: No
 * Request Body: None
 * Errors: Invalid username or generations parameter, Internal server error
 */
public class FillHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        IOStream ioStream = new IOStream();
        FillService fillService = new FillService();
        FillResult fillResult = null;

        try{
            // HTTP request type (GET or POST)
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                // get [username]/{generations} from URL path
                String[] paths = exchange.getRequestURI().toString().split("/");
                String username = paths[paths.length-2];

                // username is required. user must already be registered in db
                if (username != null) {
                    int generations = Integer.parseInt(paths[paths.length - 1]);
                    fillResult = fillService.fill(username, generations);
                    // send status code and headers in response body
                    // check if fill is success status code = OK, if not status code = bad request
                    if (!fillResult.isSuccess()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }

                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            }
            // call serialize to turn fillResult object into a string
            String resultString = JSONSerializer.serialize(fillResult);
            OutputStream os = exchange.getResponseBody();
            ioStream.writeString(resultString, os);
            os.close();
        } catch (IOException e){
            e.printStackTrace();
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR,0);
            exchange.getResponseBody().close();
        }
    }
}
