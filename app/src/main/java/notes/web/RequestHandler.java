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

import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RequestHandler implements HttpHandler {
    @Override public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        if (path.equals("/register") && method.equalsIgnoreCase("POST")) {
            handleRegister(exchange);
        }
        else if(path.equals("/login") && method.equalsIgnoreCase("POST")) {
            handleLogin(exchange);
        } 
        else if(path.equals("/addNote") && method.equalsIgnoreCase("POST")) {
            handleAddNote(exchange);
        } 
        else if(path.equals("/getNotes") && method.equalsIgnoreCase("POST")) {
            handleGetNotes(exchange);
        }
        else if(path.equals("/updateNote") && method.equalsIgnoreCase("POST")) {
            handleUpdateNote(exchange);
        }
        else {
            serveStaticFile(exchange, path);
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    private void handleRegister(HttpExchange exchange) throws IOException {
        try {
            JSONObject req = readJsonFromRequest(exchange);
            String username = req.getString("username");
            String password = req.getString("password");

            UserDAO dao=new UserDAO();
            boolean success = dao.registerUser(username, password);

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

    @SuppressWarnings("UseSpecificCatch")
    private void handleLogin(HttpExchange exchange) throws IOException {
        try {
            JSONObject req = readJsonFromRequest(exchange);
            String username = req.getString("username");
            String password = req.getString("password");   
            
            UserDAO dao=new UserDAO();
            boolean success=dao.loginUser(username, password);

            JSONObject res=new JSONObject();
            res.put("success", success);
            if(!success) res.put("message", "Invalid Credentials");

            sendJsonResponse(exchange, res);
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            res.put("success", false);
            res.put("message", e.getMessage());
            sendJsonResponse(exchange, res);
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    private void handleAddNote(HttpExchange exchange) throws IOException {
        try {
            JSONObject req=readJsonFromRequest(exchange);
            String username=req.getString("username");
            String title=req.getString("title");
            String content=req.getString("content");

            NotesDAO dao=new NotesDAO();
            boolean success=dao.addNote(username, title, content);

            JSONObject res=new JSONObject();
            res.put("success", success);
            if(!success) res.put("message", "Title already exists");

            sendJsonResponse(exchange, res);
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            res.put("success", false);
            res.put("message", e.getMessage());
            sendJsonResponse(exchange, res);
        }
    }

    @SuppressWarnings("UseSpecificCatch") 
    private void handleGetNotes(HttpExchange exchange) throws IOException {
        try {
            JSONObject req=readJsonFromRequest(exchange);
            String username=req.getString("username");

            NotesDAO dao=new NotesDAO();
            JSONArray notesArray=dao.getNotes(username);
            
            JSONObject res=new JSONObject();
            res.put("notes", notesArray);
            res.put("success", true);

            sendJsonResponse(exchange, res);
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            res.put("success", false);
            res.put("message", e.getMessage());
            sendJsonResponse(exchange, res);
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    private void handleUpdateNote(HttpExchange exchange) throws IOException {
        try {
            JSONObject req=readJsonFromRequest(exchange);
            String username=req.getString("username");
            String oldTitle=req.getString("oldTitle");
            String newTitle=req.getString("newTitle");
            String newContent=req.getString("newContent");

            NotesDAO dao=new NotesDAO();
            boolean success=dao.updateNote(username, oldTitle, newTitle, newContent);

            JSONObject res=new JSONObject();
            res.put("success", success);
            if(!success) res.put("message", "Title already exists");

            sendJsonResponse(exchange, res);
        } catch (Exception e) {
            JSONObject res = new JSONObject();
            res.put("success", false);
            res.put("message", e.getMessage());
            sendJsonResponse(exchange, res);
        }
    }

    @SuppressWarnings("resource")
    private JSONObject readJsonFromRequest(HttpExchange exchange) throws IOException {
        String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))
                .lines().collect(Collectors.joining("\n"));
        return new JSONObject(body);
    }

    private void sendJsonResponse(HttpExchange exchange, JSONObject res) throws IOException {
        byte[] bytes = res.toString().getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void serveStaticFile(HttpExchange exchange, String path) throws IOException {
        if (path.equals("/") || path.isEmpty()) path = "/index.html";
        Path filePath = Paths.get("public" + path);

        if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
            byte[] bytes = Files.readAllBytes(filePath);
            String contentType = guessContentType(path);
            exchange.getResponseHeaders().add("Content-Type", contentType);
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        } else {
            String notFound = "404 Not Found";
            exchange.sendResponseHeaders(404, notFound.length());
            try(OutputStream os = exchange.getResponseBody()) {
                os.write(notFound.getBytes());
            }
        }
    }

    private String guessContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        return "application/octet-stream";
    }
}