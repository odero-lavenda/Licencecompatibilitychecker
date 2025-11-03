package com.stagethree.utility;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class HttpUtils {

    public static String readRequestBody(com.sun.net.httpserver.HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            body.append(line);
        }
        return body.toString();
    }

    public static void sendJsonResponse(com.sun.net.httpserver.HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static void sendResponse(com.sun.net.httpserver.HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static void sendJsonRpcError(com.sun.net.httpserver.HttpExchange exchange, String id, int code, String message) throws IOException {
        JSONObject error = new JSONObject();
        error.put("jsonrpc", "2.0");
        error.put("id", id);
        JSONObject errorObj = new JSONObject();
        errorObj.put("code", code);
        errorObj.put("message", message);
        error.put("error", errorObj);
        sendJsonResponse(exchange, 400, error.toString());
    }

    public static String extractTextFromParts(JSONArray parts) {
        if (parts == null) return "";
        for (int i = 0; i < parts.length(); i++) {
            JSONObject part = parts.getJSONObject(i);
            if ("text".equals(part.optString("kind"))) {
                return part.optString("text", "");
            }
        }
        return "";
    }

    public static JSONObject createResponseMessage(String taskId, String text) {
        JSONObject message = new JSONObject();
        message.put("role", "agent");
        message.put("kind", "message");
        message.put("messageId", "msg_" + UUID.randomUUID().toString().substring(0, 8));
        message.put("taskId", taskId);

        JSONObject textPart = new JSONObject();
        textPart.put("kind", "text");
        textPart.put("text", text);

        JSONArray parts = new JSONArray();
        parts.put(textPart);
        message.put("parts", parts);

        return message;
    }
}
