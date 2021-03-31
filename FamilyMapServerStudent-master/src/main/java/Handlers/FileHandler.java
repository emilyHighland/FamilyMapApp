package Handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Default handler: handles all requests that don't have a specific path ("/")
 */
public class FileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //System.out.println("Start fileHandler");
        String urlPath = exchange.getRequestURI().getPath();
        if ((Objects.equals(urlPath, "/")) || (urlPath == null)) { // starts with /
            urlPath = "/index.html";
        }
        String filePath = "web" + urlPath;
        File file = new File(filePath);
        if (!file.exists()) {
            file = new File("web/HTML/404.html");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
        }
        OutputStream respBody = exchange.getResponseBody();
        Files.copy(file.toPath(), respBody);
        respBody.close();
    }
}
