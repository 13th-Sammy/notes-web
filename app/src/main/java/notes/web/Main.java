package notes.web;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class Main {
    public static void main(String[] args) throws IOException {
        int port=Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server=HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new RequestHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:"+port);
    }
}