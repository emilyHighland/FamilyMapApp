package com.example.myfamilymap;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import Request.LoadRequest;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.EventsResult;
import Result.LoginResult;
import Result.PersonsResult;
import Result.RegisterResult;

public class ServerProxy {
    private static final String LOG_TAG = "ServerProxy";
    private HttpClient httpClient = new HttpClient();
    DataCache dataCache;
    String urlString;

    public ServerProxy(String host, String port) {
        urlString = "http://" + host + ":" + port;
    }

    public void loginProxy(LoginRequest loginRequest) {
        try {
            String loginUrl = urlString + "/user/login";
            // send JSON serializer output string to httpClient
            String requestString = serialize(loginRequest);
            // turn returned JSON string into Result object
            String jsonString = httpClient.doPost(loginUrl, requestString);
            // store Result in DataCache
            dataCache.setLoginResult(deserialize(jsonString, LoginResult.class));
        } catch(IOException e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    public void registerProxy(RegisterRequest registerRequest) {
        try {
            String registerUrl = urlString + "/user/register";
            // send JSON serializer registerRequest output string to httpClient
            String requestString = serialize(registerRequest);
            // turn returned JSON string into Result object
            String jsonString = httpClient.doPost(registerUrl,requestString);
            // store Result in DataCache
            dataCache.setRegisterResult(deserialize(jsonString, RegisterResult.class));
        } catch(IOException e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    public void getDataProxy() {
        // TODO: need to get PersonsResult and EventsResult calling client api's and storing them in DataCache
        try {
            // LoginResult contains authToken needed to find associated family tree data
            String authToken = dataCache.getLoginResult().getAuthtoken();
            // call GET for ALL persons and ALL events
            getPersonsProxy(authToken);
            getEventsProxy(authToken);
            // if person and event GET methods are successful, set dataRetrieved bool to true
            if (dataCache.getPersonsResult().isSuccess() && dataCache.getEventsResult().isSuccess()){
                dataCache.dataRetrieved = true;
            }
        } catch(Exception e){
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    private void getPersonsProxy(String authToken) throws IOException {
        String personsUrl = urlString + "/person";
        // turn returned JSON string into Result object
        String jsonString = httpClient.doGet(personsUrl, authToken);
        // store Result in DataCache
        dataCache.setPersonsResult(deserialize(jsonString, PersonsResult.class));
    }

    private void getEventsProxy(String authToken) throws IOException {
        String eventsUrl = urlString + "/event";
        // turn returned JSON string into Result object
        String jsonString = httpClient.doGet(eventsUrl,authToken);
        // store Result in DataCache
        dataCache.setEventsResult(deserialize(jsonString, EventsResult.class));
    }






    // JSON serializer/deserializer
    // Takes an object and turn it into a JSON string
    public static String serialize(Object object){
        return (new Gson()).toJson(object);
    }

    // Takes a string and turn it into an object
    public static <T> T deserialize(String value, Class<T> returnType) throws JsonParseException {
        try {
            return (new Gson()).fromJson(value, returnType);
        } catch(JsonParseException e){
            e.printStackTrace();
        }
        return null;
    }

    // takes file returns as array of information
    public <T> T parse(File file, Class<T> returnType) throws IOException {
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            return (new Gson()).fromJson(bufferedReader, returnType);
        }
    }
}

