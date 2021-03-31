package Handlers;


import Service.LoginService;
import Service.Request.LoginRequest;
import Service.Result.LoginResult;
import Utilites.IOStream;
import Utilites.JSONSerializer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * URL Path: /user/login
 * Description: Logs in the user and returns an auth token.
 * HTTP Method: POST
 * Auth Token Required: No
 * Errors: Request property missing or has invalid value, Internal server error
 */
public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        IOStream ioStream = new IOStream();
        LoginResult loginResult = null;

        try {
            // HTTP request type (GET or POST)
            if (exchange.getRequestMethod().toUpperCase().equals("POST")) {
                // get request body turn into string
                String requestString = ioStream.readString(exchange.getRequestBody());
                // call deserializer to turn Request string into a Request object
                LoginRequest loginRequest = JSONSerializer.deserialize(requestString, LoginRequest.class);
                if (loginRequest != null) {
                    // pass the Request object to the specific Service to make a Result object
                    LoginService loginService = new LoginService();
                    loginResult = loginService.login(loginRequest);
                    // send status code and headers in response body
                    // check if login is success status code = OK, if not status code = bad request
                    if (!loginResult.isSuccess()) {
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
            String resultString = JSONSerializer.serialize(loginResult);
            OutputStream os = exchange.getResponseBody();
            ioStream.writeString(resultString, os);
            os.close();
        } catch (IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            // We are not sending a response body, so close the response body
            // output stream, indicating that the response is complete.
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
