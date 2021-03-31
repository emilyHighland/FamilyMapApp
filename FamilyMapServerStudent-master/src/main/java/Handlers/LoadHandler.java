package Handlers;

import Service.LoadService;
import Service.Request.LoadRequest;
import Service.Result.LoadResult;
import Utilites.IOStream;
import Utilites.JSONSerializer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * URL Path: /load
 * Description: Clears all data from the database (just like the /clear API), and then loads the posted user, person, and event data into the database.
 * HTTP Method: POST
 * Auth Token Required: No
 * Request Body: The “users” property in the request body contains an array of users to be created. The “persons” and “events” properties contain family history information for these users.  The objects contained in the “persons” and “events” arrays should be added to the server’s database.  The objects in the “users” array have the same format as those passed to the /user/register API with the addition of the personID.  The objects in the “persons” array have the same format as those returned by the /person/[personID] API.  The objects in the “events” array have the same format as those returned by the /event/[eventID] API.
 * Errors: Invalid request data (missing values, invalid values, etc.), Internal server error
 */
public class LoadHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        IOStream ioStream = new IOStream();
        LoadResult loadResult = null;

        try{
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                // get request body turn into string
                InputStream is = exchange.getRequestBody();
                String requestString = ioStream.readString(is);
                is.close();

                // call deserializer to turn Request string into a Request object
                LoadRequest loadRequest;
                if ((loadRequest = JSONSerializer.deserialize(requestString, LoadRequest.class)) != null) {
                    // pass the Request object to the specific Service to make a Result object
                    LoadService loadService = new LoadService();
                    loadResult = loadService.load(loadRequest);
                    // send status code and headers in response body
                    // check if load is success status code = OK, if not status code = bad request
                    if (!loadResult.isSuccess()) {
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
            // call serialize to turn Result object into a string
            String resultString = JSONSerializer.serialize(loadResult);
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
