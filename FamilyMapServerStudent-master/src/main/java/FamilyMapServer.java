import com.sun.net.httpserver.HttpServer;
import Handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class FamilyMapServer {
    public void main(int port){
        try {
            startServer(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startServer(int port) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(port);
        HttpServer server = HttpServer.create(serverAddress, 10);
        registerHandlers(server);
        server.start();
        System.out.println("FamilyMapServer listening on port " + port);
    }

    private void registerHandlers(HttpServer server){
        server.createContext("/", new FileHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill/", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person/", new PersonHandler());
        server.createContext("/person", new PersonsHandler());
        server.createContext("/event/", new EventHandler());
        server.createContext("/event", new EventsHandler());
    }
}
