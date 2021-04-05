package com.example.myfamilymap;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class HttpClient {
    private static final String LOG_TAG = "HttpClient";

    // POST Request/Response Method
    public String doPost(String urlString, String jsonRequestString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set HTTP request headers, if necessary
        connection.addRequestProperty("Accept", "application/json");
        connection.connect();

        try(OutputStream requestBody = connection.getOutputStream();) {
            // Send/Write JSON body to OutputStream ...
            writeString(jsonRequestString, requestBody);
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get HTTP response headers, if necessary
            // Map<String, List<String>> headers = connection.getHeaderFields();
            // OR
            // connection.getHeaderField("Content-Length");

            // Read JSON/response body from InputStream ...
            InputStream responseBody = connection.getInputStream();
            String responseString = readString(responseBody);
            responseBody.close();
            return responseString;
        } else {
            // SERVER RETURNED AN HTTP ERROR
            throw new IOException("Bad server connection.");
        }
//        } catch(IOException e){
//            Log.e(LOG_TAG, e.getMessage(), e);
//            throw e;
//        }
    }

    // GET Request/Response Method
    public String doGet(String urlString, String authToken) throws IOException {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");

            // Set HTTP request headers, if necessary
            connection.addRequestProperty("Accept", "text/html");
            connection.addRequestProperty("Authorization", authToken);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Read JSON/response body from InputStream ...
                InputStream responseBody = connection.getInputStream();
                String responseString = readString(responseBody);
                responseBody.close();
                return responseString;
            } else {
                // SERVER RETURNED AN HTTP ERROR
                throw new IOException();
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            throw e;
        }
    }

    // IOStreams //
    /* The readString method reads a String from an InputStream.*/
    public String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /* The writeString method writes a String to an OutputStream */
    public void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(sw);
        bw.write(str);
        bw.flush();
    }

}
