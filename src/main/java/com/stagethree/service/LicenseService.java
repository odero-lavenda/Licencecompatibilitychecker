package com.stagethree.service;

import com.stagethree.model.CompatibilityResult;
import com.stagethree.model.LicenseInfo;
import jakarta.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class LicenseService {

    private static final String GITHUB_LICENSE_API = "https://api.github.com/licenses";
    private final Map<String, LicenseInfo> licenseCache = new HashMap<>();
    private final Map<String, Set<String>> compatibilityMatrix = new HashMap<>();

    @PostConstruct
    public void initializeCompatibilityMatrix() {
        Set<String> permissive = new HashSet<>(Arrays.asList(
                "mit", "apache-2.0", "bsd-2-clause", "bsd-3-clause", "isc", "unlicense"
        ));

        Set<String> copyleft = new HashSet<>(Arrays.asList(
                "gpl-2.0", "gpl-3.0", "agpl-3.0"
        ));

        Set<String> weakCopyleft = new HashSet<>(Arrays.asList(
                "lgpl-2.1", "lgpl-3.0", "mpl-2.0", "epl-2.0"
        ));

        for (String lic : permissive) {
            compatibilityMatrix.put(lic, new HashSet<>());
            compatibilityMatrix.get(lic).addAll(permissive);
            compatibilityMatrix.get(lic).addAll(weakCopyleft);
            compatibilityMatrix.get(lic).addAll(copyleft);
        }

        for (String lic : weakCopyleft) {
            compatibilityMatrix.put(lic, new HashSet<>());
            compatibilityMatrix.get(lic).addAll(permissive);
            compatibilityMatrix.get(lic).addAll(weakCopyleft);
            compatibilityMatrix.get(lic).addAll(copyleft);
        }

        for (String lic : copyleft) {
            compatibilityMatrix.put(lic, new HashSet<>());
            compatibilityMatrix.get(lic).addAll(permissive);
            compatibilityMatrix.get(lic).add(lic);
        }
    }

    @Cacheable(value = "compatibility", key = "#license1 + '-' + #license2")
    public CompatibilityResult checkCompatibility(String license1, String license2) {
        String l1 = license1.toLowerCase();
        String l2 = license2.toLowerCase();
        boolean compatible = isCompatible(l1, l2);
        LicenseInfo info1 = getLicenseInfo(l1);
        LicenseInfo info2 = getLicenseInfo(l2);
        return new CompatibilityResult(compatible, info1, info2);
    }

    @Cacheable(value = "licenses", key = "#licenseKey")
    public LicenseInfo getLicenseInfo(String licenseKey) {
        String key = licenseKey.toLowerCase();
        if (licenseCache.containsKey(key)) {
            return licenseCache.get(key);
        }
        return fetchLicenseFromAPI(key);
    }

    @Cacheable("popularLicenses")
    public List<LicenseInfo> getPopularLicenses() {
        String[] popular = {"mit", "apache-2.0", "gpl-3.0", "bsd-3-clause",
                "mpl-2.0", "lgpl-3.0", "agpl-3.0", "unlicense"};
        List<LicenseInfo> licenses = new ArrayList<>();
        for (String key : popular) {
            LicenseInfo info = getLicenseInfo(key);
            if (info != null) {
                licenses.add(info);
            }
        }
        return licenses;
    }

    public LicenseInfo getDailyLicense() {
        String[] licenses = {"mit", "apache-2.0", "gpl-3.0", "bsd-3-clause",
                "mpl-2.0", "lgpl-3.0", "agpl-3.0", "unlicense"};
        int dayOfYear = LocalDateTime.now().getDayOfYear();
        String todayLicense = licenses[dayOfYear % licenses.length];
        return getLicenseInfo(todayLicense);
    }

    public String getPermissiveness(String licenseKey) {
        String key = licenseKey.toLowerCase();
        if (Arrays.asList("mit", "apache-2.0", "bsd-2-clause", "bsd-3-clause",
                "isc", "unlicense").contains(key)) {
            return "Permissive";
        } else if (Arrays.asList("gpl-2.0", "gpl-3.0", "agpl-3.0").contains(key)) {
            return "Strong Copyleft";
        } else if (Arrays.asList("lgpl-2.1", "lgpl-3.0", "mpl-2.0").contains(key)) {
            return "Weak Copyleft";
        }
        return "Other";
    }

    public String getBestUseCase(String licenseKey) {
        switch (licenseKey.toLowerCase()) {
            case "mit":
                return "Simple projects, maximum freedom, minimal restrictions";
            case "apache-2.0":
                return "Corporate projects with patent protection";
            case "gpl-3.0":
                return "Free software projects ensuring open modifications";
            case "bsd-3-clause":
                return "Academic projects with attribution requirements";
            case "mpl-2.0":
                return "Libraries with file-level copyleft";
            case "lgpl-3.0":
                return "Libraries used in proprietary software";
            case "agpl-3.0":
                return "Web services and SaaS applications";
            case "unlicense":
                return "Public domain dedication, maximum freedom";
            default:
                return "Various open-source projects";
        }
    }

    private LicenseInfo fetchLicenseFromAPI(String licenseKey) {
        try {
            String url = GITHUB_LICENSE_API + "/" + licenseKey;
            String jsonResponse = makeAPIRequest(url);
            JSONObject json = new JSONObject(jsonResponse);

            LicenseInfo info = new LicenseInfo();
            info.setKey(json.getString("key"));
            info.setName(json.getString("name"));
            info.setSpdxId(json.optString("spdx_id", "N/A"));
            info.setDescription(json.optString("description", "No description available"));
            info.setUrl(json.getString("html_url"));
            info.setPermissions(parseJsonArray(json.optJSONArray("permissions")));
            info.setConditions(parseJsonArray(json.optJSONArray("conditions")));
            info.setLimitations(parseJsonArray(json.optJSONArray("limitations")));

            licenseCache.put(licenseKey, info);
            return info;
        } catch (Exception e) {
            System.err.println("Error fetching license " + licenseKey + ": " + e.getMessage());
            return null;
        }
    }

    private String makeAPIRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/vnd.github+json");
        conn.setRequestProperty("User-Agent", "LicenseCheckerAgent/1.0");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private List<String> parseJsonArray(JSONArray array) {
        List<String> result = new ArrayList<>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                result.add(formatPermission(array.getString(i)));
            }
        }
        return result;
    }

    private String formatPermission(String key) {
        return key.substring(0, 1).toUpperCase() + key.substring(1)
                .replace("-", " ")
                .replace("_", " ");
    }

    private boolean isCompatible(String license1, String license2) {
        if (compatibilityMatrix.containsKey(license1)) {
            return compatibilityMatrix.get(license1).contains(license2);
        }
        return false;
    }
    }
