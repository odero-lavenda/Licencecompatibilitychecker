package com.stagethree.controller;

import org.json.JSONObject;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {

    @GetMapping("/{taskId}")
    public String getTaskStatus(@PathVariable String taskId) {
        JSONObject task = new JSONObject();
        task.put("taskId", taskId);
        task.put("state", "completed");
        task.put("timestamp", LocalDateTime.now().toString());

        task.put("message", "The task has been processed successfully.");

        return task.toString(2); // pretty-print JSON
    }
}
