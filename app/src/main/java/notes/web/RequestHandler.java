package notes.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/register") && method.equalsIgnoreCase("POST")) {
            handleRegister(exchange);
        } else {
            serveStaticFile(exchange, path);
        }
    }

    private void handleRegister(HttpExchange exchange) throws IOException {
        try {
            JSONObject req = readJsonFromRequest(exchange);
            String username = req.getString("username");
            String password = req.getString("password");

            // TODO: You will implement this
            boolean success = false; // Replace with: UserDAO.registerUser(username, password);

            JSONObject res = new JSONObject();
            res.put("success", success);
            if (!success) res.put("message", "Username already exists");

            sendJsonResponse(exchange, res);
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            res.put("success", false);
            res.put("message", e.getMessage());
            sendJsonResponse(exchange, res);
        }
    }

    private JSONObject readJsonFromRequest(HttpExchange exchange) throws IOException {
        String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines().collect(Collectors.joining("\n"));
        return new JSONObject(body);
    }

    private void sendJsonResponse(HttpExchange exchange, JSONObject res) throws IOException {
        byte[] bytes = res.toString().getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private void serveStaticFile(HttpExchange exchange, String path) throws IOException {
        if (path.equals("/")) path = "/index.html";
        Path filePath = Paths.get("public" + path);

        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            byte[] bytes = Files.readAllBytes(filePath);
            String contentType = guessContentType(path);
            exchange.getResponseHeaders().add("Content-Type", contentType);
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        } else {
            String notFound = "404 Not Found";
            exchange.sendResponseHeaders(404, notFound.length());
            OutputStream os = exchange.getResponseBody();
            os.write(notFound.getBytes());
            os.close();
        }
    }

    private String guessContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        return "application/octet-stream";
    }
}