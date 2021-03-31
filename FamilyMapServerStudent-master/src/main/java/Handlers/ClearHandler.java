package Handlers;

import Service.ClearService;
import Service.Result.ClearResult;
import Utilites.IOStream;
import Utilites.JSONSerializer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * URL Path: /clear
 * Description: Deletes ALL data from the database, including user accounts,
 *  auth tokens, generated person and event data.
 * HTTP Method: POST
 * AuthToken Required: No
 * Request Body: None
 * Errors: Internal server error
 */
public class ClearHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        IOStream ioStream = new IOStream();
        ClearResult clearResult = null;

        try {
            // HTTP request type (GET or POST)
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                // clearService returns whether db.clear was a success
                ClearService clearService = new ClearService();
                clearResult = clearService.clear();
                // send status code and headers in response body
                // check if clear is success status code = OK, if not status code = bad request
                if ((clearResult == null) || (!clearResult.isSuccess())){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            }
            // call serialize to turn clearResult object into a string
            String resultString = JSONSerializer.serialize(clearResult);
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
