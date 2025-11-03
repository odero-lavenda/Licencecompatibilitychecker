package com.stagethree.utility;

import java.util.ArrayList;
import java.util.List;

public class LicenseExtractor {
    private static final String[] KNOWN_LICENSES = {
            "mit", "apache-2.0", "apache", "gpl-3.0", "gpl-2.0", "gpl",
            "bsd-3-clause", "bsd-2-clause", "bsd", "mpl-2.0", "mpl",
            "lgpl-3.0", "lgpl-2.1", "lgpl", "agpl-3.0", "agpl",
            "unlicense", "isc", "epl-2.0"
    };

    public static List<String> extractLicenseNames(String input) {
        List<String> licenses = new ArrayList<>();
        String lowerInput = input.toLowerCase();

        for (String license : KNOWN_LICENSES) {
            if (lowerInput.contains(license) && !licenses.contains(license)) {
                licenses.add(license);
            }
        }
        return licenses;
    }
}
