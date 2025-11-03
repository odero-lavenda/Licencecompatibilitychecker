package com.stagethree.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LicenseInfo {
    private String name;
    private  String key;
    private String spdxId;
    private String description;
    private String url;
    private List<String> permissions ;
    private List<String>conditions;
    private List<String>limitations;
}
