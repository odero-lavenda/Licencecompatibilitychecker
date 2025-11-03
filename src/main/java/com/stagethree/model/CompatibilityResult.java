package com.stagethree.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompatibilityResult {
    boolean compatible;
    LicenseInfo license1;
    LicenseInfo license2;



}
