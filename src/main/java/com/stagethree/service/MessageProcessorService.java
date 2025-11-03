package com.stagethree.service;

import com.stagethree.model.LicenseInfo;
import com.stagethree.model.CompatibilityResult;
import com.stagethree.utility.LicenseExtractor;
import com.stagethree.utility.ResponseFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageProcessorService {

    private final LicenseService licenseService;

    @Autowired
    public MessageProcessorService(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    public String processMessage(String input) {
        String normalized = input.toLowerCase().trim();

        if (isCompatibilityQuery(normalized)) {
            return handleCompatibilityCheck(normalized);
        }
        if (isLicenseInfoQuery(normalized)) {
            return handleLicenseInfo(normalized);
        }
        if (isListQuery(normalized)) {
            return handleListLicenses();
        }
        if (isDailyQuery(normalized)) {
            return handleDailyLicense();
        }
        return generateHelpMessage();
    }

    private boolean isCompatibilityQuery(String input) {
        return input.contains("compatible") ||
                input.contains("can i use") ||
                (input.contains("use") && input.contains("with"));
    }

    private boolean isLicenseInfoQuery(String input) {
        return input.contains("about") ||
                input.contains("tell me") ||
                input.contains("info") ||
                input.contains("explain");
    }

    private boolean isListQuery(String input) {
        return input.contains("list") ||
                input.contains("show all") ||
                input.contains("available licenses");
    }

    private boolean isDailyQuery(String input) {
        return input.contains("daily") ||
                input.contains("license of the day") ||
                input.contains("today");
    }

    private String handleCompatibilityCheck(String input) {
        List<String> licenses = LicenseExtractor.extractLicenseNames(input);

        if (licenses.size() < 2) {
            return "❌ Please specify two licenses to check compatibility.\n\n" +
                    "**Example:** \"Can I use MIT with GPL-3.0?\"";
        }

        CompatibilityResult result = licenseService.checkCompatibility(
                licenses.get(0), licenses.get(1)
        );

        return ResponseFormatter.formatCompatibilityResult(result, licenseService);
    }

    private String handleLicenseInfo(String input) {
        List<String> licenses = LicenseExtractor.extractLicenseNames(input);

        if (licenses.isEmpty()) {
            return "❌ Please specify a license name.\n\n" +
                    "**Example:** \"Tell me about MIT license\"\n\n" +
                    "**Popular licenses:** MIT, Apache-2.0, GPL-3.0, BSD-3-Clause";
        }

        LicenseInfo info = licenseService.getLicenseInfo(licenses.get(0));

        if (info == null) {
            return "❌ License not found: " + licenses.get(0).toUpperCase() + "\n\n" +
                    "Try: mit, apache-2.0, gpl-3.0, bsd-3-clause, mpl-2.0";
        }

        return ResponseFormatter.formatLicenseInfo(info, licenseService);
    }

    private String handleListLicenses() {
        List<LicenseInfo> licenses = licenseService.getPopularLicenses();
        return ResponseFormatter.formatLicenseList(licenses);
    }

    private String handleDailyLicense() {
        LicenseInfo info = licenseService.getDailyLicense();
        return ResponseFormatter.formatDailyLicense(info, licenseService);
    }

    private String generateHelpMessage() {
        return "👋 **Welcome to License Compatibility Checker!**\n\n" +
                "I help developers understand open source licenses. Here's what I can do:\n\n" +
                "🔍 **Check Compatibility:**\n" +
                "   \"Can I use MIT with GPL-3.0?\"\n\n" +
                "📋 **License Information:**\n" +
                "   \"Tell me about Apache-2.0\"\n\n" +
                "📚 **List All Licenses:**\n" +
                "   \"Show me all licenses\"\n\n" +
                "📅 **Daily License:**\n" +
                "   \"License of the day\"\n\n" +
                "💡 Just ask naturally - I'll understand!";
    }
}
