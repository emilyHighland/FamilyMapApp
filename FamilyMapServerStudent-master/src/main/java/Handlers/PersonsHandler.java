package Handlers;

import Service.PersonsService;
import Service.Result.PersonsResult;
import Utilites.IOStream;
import Utilites.JSONSerializer;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * URL Path: /person
 * Description: Returns ALL family members of the current user. The current user is determined from the provided auth token.
 * HTTP Method: GET
 * Auth Token Required: Yes
 * Request Body: None
 * Errors: Invalid auth token, Internal server error
 * Success Response Body: The response body returns a JSON object with a “data” attribute that contains an array of Person objects.  Each Person object has the same format as described in previous section on the /person/[personID] API.
 */
public class PersonsHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        IOStream ioStream = new IOStream();
        PersonsResult personsResult = null;

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("GET")) {
                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();
                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization")) {
                    // Extract the auth token from the "Authorization" header
                    String authToken = reqHeaders.getFirst("Authorization");

                    // pass the event object to the specific service to make a result obj
                    PersonsService personsService = new PersonsService();
                    personsResult = personsService.getAllPersons(authToken);
                    // Start sending the HTTP response to the client, starting with the status code and any defined headers
                    if (!personsResult.isSuccess()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                } else {
                    // We did not get an auth token, so we return a "not authorized" status code to the client.
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
            } else {
                // We expected a GET but got something else, so we return a "bad request" status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
            // Now that the status code and headers have been sent to the client,
            // next we send the JSON data in the HTTP response body.

            // call serialize to turn event obj into a string
            String resultString = JSONSerializer.serialize(personsResult);
            // Get the response body output stream.
            OutputStream respBody = exchange.getResponseBody();
            // Write the JSON string to the output stream.
            ioStream.writeString(resultString, respBody);
            // Close the output stream.  This is how Java knows we are done sending data and the response is complete
            respBody.close();
        } catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the
            // client's fault), so we return an "internal server error" status code
            // to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            // Since the server is unable to complete the request, we close the response body output stream,
            // indicating that the response is complete.
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
