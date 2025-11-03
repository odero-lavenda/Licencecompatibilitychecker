package com.stagethree.utility;

import com.stagethree.model.CompatibilityResult;
import com.stagethree.model.LicenseInfo;
import com.stagethree.service.LicenseService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResponseFormatter {
    public static String formatCompatibilityResult(CompatibilityResult result, LicenseService service) {
        StringBuilder sb = new StringBuilder();

        String l1 = result.getLicense1().getKey().toUpperCase();
        String l2 = result.getLicense2().getKey().toUpperCase();

        sb.append("🔍 **Compatibility Check: ").append(l1)
                .append(" + ").append(l2).append("**\n\n");

        if (result.isCompatible()) {
            sb.append("✅ **COMPATIBLE**\n");
            sb.append("These licenses can generally be used together.\n\n");
        } else {
            sb.append("⚠️ **POTENTIALLY INCOMPATIBLE**\n");
            sb.append("These licenses may have conflicts. Review terms carefully.\n\n");
        }

        sb.append("📋 **").append(l1).append("**: ")
                .append(service.getPermissiveness(result.getLicense1().getKey())).append("\n");
        sb.append("📋 **").append(l2).append("**: ")
                .append(service.getPermissiveness(result.getLicense2().getKey())).append("\n\n");

        sb.append("💡 **Tip**: Always consult legal counsel for specific use cases.");

        return sb.toString();
    }

    public static String formatLicenseInfo(LicenseInfo info, LicenseService service) {
        StringBuilder sb = new StringBuilder();

        sb.append("📜 **").append(info.getName()).append("**\n\n");
        sb.append("**Description:**\n").append(info.getDescription()).append("\n\n");
        sb.append("**Type:** ").append(service.getPermissiveness(info.getKey())).append("\n\n");

        if (info.getPermissions() != null && !info.getPermissions().isEmpty()) {
            sb.append("✅ **Permissions:**\n");
            for (String perm : info.getPermissions()) {
                sb.append("   • ").append(perm).append("\n");
            }
            sb.append("\n");
        }

        if (info.getConditions() != null && !info.getConditions().isEmpty()) {
            sb.append("⚠️ **Conditions:**\n");
            for (String cond : info.getConditions()) {
                sb.append("   • ").append(cond).append("\n");
            }
            sb.append("\n");
        }

        if (info.getLimitations() != null && !info.getLimitations().isEmpty()) {
            sb.append("❌ **Limitations:**\n");
            for (String limit : info.getLimitations()) {
                sb.append("   • ").append(limit).append("\n");
            }
            sb.append("\n");
        }

        sb.append("🎯 **Best for:** ").append(service.getBestUseCase(info.getKey())).append("\n\n");
        sb.append("🔗 [Learn more](").append(info.getUrl()).append(")");

        return sb.toString();
    }

    public static String formatLicenseList(List<LicenseInfo> licenses) {
        StringBuilder sb = new StringBuilder();
        sb.append("📚 **Popular Open Source Licenses:**\n\n");

        for (LicenseInfo lic : licenses) {
            sb.append("• **").append(lic.getKey().toUpperCase())
                    .append("** - ").append(lic.getName()).append("\n");
        }

        sb.append("\n💡 Ask me about any license for detailed information!");
        return sb.toString();
    }

    public static String formatDailyLicense(LicenseInfo info, LicenseService service) {
        StringBuilder sb = new StringBuilder();

        sb.append("📅 **License of the Day** - ")
                .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")))
                .append("\n\n");
        sb.append("🌟 **").append(info.getName()).append("**\n\n");
        sb.append(info.getDescription()).append("\n\n");
        sb.append("**Type:** ").append(service.getPermissiveness(info.getKey())).append("\n");
        sb.append("**Best for:** ").append(service.getBestUseCase(info.getKey())).append("\n\n");
        sb.append("🔗 [Learn more](").append(info.getUrl()).append(")");

        return sb.toString();
    }
}
