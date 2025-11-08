package com.stagethree.controller;

import com.stagethree.service.MessageProcessorService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1")
public class MessageController {
    private final MessageProcessorService messageProcessor;

    @Autowired
    public MessageController(MessageProcessorService messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    @PostMapping(value = "/message", produces = "application/json")
    public String handleMessage(@RequestBody String requestBody) {
        try {
            JSONObject request = new JSONObject(requestBody);

            if (!isValidRequest(request)) {
                return buildErrorResponse(request.optString("id"), -32600, "Invalid Request").toString(2);
            }

            JSONObject params = request.getJSONObject("params");
            JSONObject message = params.getJSONObject("message");
            String userInput = extractTextFromParts(message.optJSONArray("parts"));

            String responseText = messageProcessor.processMessage(userInput);

            String taskId = generateTaskId();
            JSONObject response = buildSuccessResponse(request.optString("id"), taskId, responseText);
            return response.toString(2);

        } catch (Exception e) {
            e.printStackTrace();
            return buildErrorResponse("unknown", -32603, "Internal error: " + e.getMessage()).toString(2);
        }
    }

    private boolean isValidRequest(JSONObject request) {
        if (!request.has("jsonrpc") || !"2.0".equals(request.optString("jsonrpc"))) {
            return false;
        }

        if (!request.has("method") || !"message".equals(request.optString("method"))) {
            return false;
        }

        if (!request.has("id")) {
            return false;
        }

        // Check params structure
        if (!request.has("params")) {
            return false;
        }

        JSONObject params = request.getJSONObject("params");
        if (!params.has("message")) {
            return false;
        }

        JSONObject message = params.getJSONObject("message");
        return message.has("parts");
    }

    private String extractTextFromParts(org.json.JSONArray parts) {
        if (parts == null || parts.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length(); i++) {
            JSONObject part = parts.getJSONObject(i);
            sb.append(part.optString("text", ""));
        }
        return sb.toString().trim();
    }

    private String generateTaskId() {
        return "task_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private JSONObject buildSuccessResponse(String requestId, String taskId, String text) {
        JSONObject response = new JSONObject();
        response.put("jsonrpc", "2.0");
        response.put("id", requestId);

        JSONObject result = new JSONObject();
        result.put("state", "completed");

        JSONObject message = new JSONObject();
        message.put("parts", new org.json.JSONArray().put(new JSONObject().put("text", text)));

        result.put("message", message);
        result.put("taskId", taskId);

        response.put("result", result);
        return response;
    }

    private JSONObject buildErrorResponse(String requestId, int code, String message) {
        JSONObject error = new JSONObject();
        error.put("jsonrpc", "2.0");
        error.put("id", requestId);

        JSONObject err = new JSONObject();
        err.put("code", code);
        err.put("message", message);

        error.put("error", err);
        return error;
    }
}