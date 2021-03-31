package Handlers;

import Service.PersonService;
import Service.Result.PersonResult;
import Utilites.IOStream;
import Utilites.JSONSerializer;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * URL Path: /person/
 * Description: Returns the single Person object with the specified ID.
 * HTTP Method: GET
 * Auth Token Required: Yes
 * Request Body: None
 * Errors: Invalid auth token, Invalid personID parameter,
 *  Requested person does not belong to this user, Internal server error
 */
public class PersonHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        IOStream ioStream = new IOStream();
        PersonResult personResult = null;

        try{
            if(exchange.getRequestMethod().toUpperCase().equals("GET")){
                // Get the HTTP request headers
                Headers reqHeaders = exchange.getRequestHeaders();
                // Check to see if an "Authorization" header is present
                if (reqHeaders.containsKey("Authorization")) {
                    // Extract the auth token from the "Authorization" header
                    String authToken = reqHeaders.getFirst("Authorization");

                    // split url path to get eventID
                    String[] paths = exchange.getRequestURI().toString().split("/");
                    String personID = paths[paths.length - 1];
                    if (personID != null) {
                        // pass the event object to the specific service to make a result obj
                        PersonService personService = new PersonService();
                        personResult = personService.getPerson(personID, authToken);
                        // Start sending the HTTP response to the client, starting with  the status code and any defined headers
                        if (!personResult.isSuccess()) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        } else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        }
                    } else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }
                } else {
                    // We did not get an auth token, so we return a "not authorized" status code to the client.
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
            } else {
                // We expected a GET but got something else, so we return a "bad request" status code to the client.
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
            // Now that the status code and headers have been sent to the client, next we send the JSON data in the HTTP response body.

            // call serialize to turn event obj into a string
            String resultString = JSONSerializer.serialize(personResult);
            // Get the response body output stream.
            OutputStream respBody = exchange.getResponseBody();
            // Write the JSON string to the output stream.
            ioStream.writeString(resultString, respBody);
            // Close the output stream.  This is how Java knows we are done sending data and the response is complete
            respBody.close();
        } catch (IOException e) {
            // Some kind of internal error has occurred inside the server (not the client's fault),
            // so we return an "internal server error" status code to the client.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            // Since the server is unable to complete the request, we close the response body output stream,
            // indicating that the response is complete.
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
