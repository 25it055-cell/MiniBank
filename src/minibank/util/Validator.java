package minibank.util;

import java.util.regex.Pattern;

/**
 * Input validator using precompiled regular expressions (Practical 4 Part B).
 */
public class Validator {
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PAN_PATTERN = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]$");
    private static final Pattern IFSC_PATTERN = Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^[1-9]\\d*$");

    public static boolean isValidMobile(String mobile) {
        return mobile != null && MOBILE_PATTERN.matcher(mobile.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPan(String pan) {
        return pan != null && PAN_PATTERN.matcher(pan.trim()).matches();
    }

    public static boolean isValidIfsc(String ifsc) {
        return ifsc != null && IFSC_PATTERN.matcher(ifsc.trim()).matches();
    }

    /**
     * Supplementary problem Practical 4: validates positive integer amount.
     */
    public static boolean isValidAmount(String amountStr) {
        return amountStr != null && AMOUNT_PATTERN.matcher(amountStr.trim()).matches();
    }
}

