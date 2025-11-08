package com.stagethree.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentCardController {
    private static final String AGENT_ID = "license-checker-agent";
    private static final String AGENT_NAME = "License Compatibility Checker";
    private static final String AGENT_VERSION = "1.0.0";

    @GetMapping(value = "/.well-known/agent-card", produces = "application/json")
    public String getAgentCard() {
        JSONObject agentCard = buildAgentCard();
        return agentCard.toString(2);
    }

    private JSONObject buildAgentCard() {
        JSONObject card = new JSONObject();
        card.put("id", AGENT_ID);
        card.put("name", AGENT_NAME);
        card.put("version", AGENT_VERSION);
        card.put("description",
                "Checks open source license compatibility, provides license details, " +
                        "and shares daily license insights. Perfect for developers who need to " +
                        "understand licensing requirements.");

        JSONArray capabilities = new JSONArray();
        capabilities.put("license-compatibility-check");
        capabilities.put("license-information");
        capabilities.put("daily-license-insights");
        card.put("capabilities", capabilities);

        card.put("skills", buildSkills());

        JSONObject endpoint = new JSONObject();

        endpoint.put("url", "https://licencecompatibilitychecker-production.up.railway.app/v1/message");
        endpoint.put("protocol", "A2A");
        card.put("endpoint", endpoint);

        return card;
    }

    private JSONArray buildSkills() {
        JSONArray skills = new JSONArray();

        skills.put(createSkill(
                "Check Compatibility",
                "Verify if two licenses can be used together",
                "Can I use MIT with GPL-3.0?"
        ));

        skills.put(createSkill(
                "License Details",
                "Get comprehensive information about any license",
                "Tell me about Apache-2.0 license"
        ));

        skills.put(createSkill(
                "List Available Licenses",
                "Show all supported open source licenses",
                "Show me all licenses"
        ));

        return skills;
    }

    private JSONObject createSkill(String name, String description, String example) {
        JSONObject skill = new JSONObject();
        skill.put("name", name);
        skill.put("description", description);
        skill.put("example", example);
        return skill;
    }
}