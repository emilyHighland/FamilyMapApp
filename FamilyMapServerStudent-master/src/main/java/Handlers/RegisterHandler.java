package Handlers;


import Service.RegisterService;
import Service.Request.RegisterRequest;
import Service.Result.RegisterResult;
import Utilites.IOStream;
import Utilites.JSONSerializer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * URL Path: /user/register
 * Description: Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in, and returns an auth token.
 * HTTP Method: POST
 * Auth Token Required: No
 * Errors: Request property missing or has invalid value,
 *  Username already taken by another user, Internal server error
 */
public class RegisterHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        IOStream ioStream = new IOStream();
        RegisterResult registerResult = null;

        try {
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                // get request body turn into string
                InputStream is = exchange.getRequestBody();
                String requestString = ioStream.readString(is);
                is.close();

                // call deserializer to turn Request string into a Request object
                RegisterRequest registerRequest = JSONSerializer.deserialize(requestString, RegisterRequest.class);
                if (registerRequest != null) {
                    // pass the Request object to the specific Service to make a Result object
                    RegisterService registerService = new RegisterService();
                    registerResult = registerService.register(registerRequest);
                    // send status code and headers in response body
                    // check if register is success status code = OK, if not status code = bad request
                    if (!registerResult.isSuccess()) {
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
            String resultString = JSONSerializer.serialize(registerResult);
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
