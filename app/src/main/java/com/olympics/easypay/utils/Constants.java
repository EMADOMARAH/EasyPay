package com.olympics.easypay.utils;

import java.util.regex.Pattern;

public class Constants {
    //Network
    public static final String BASE_URL = "http://196.221.197.51/";
    public static final String BASE_QR = "EasyPayQr/";
    public static final String BASE_PERSONAL = "easypayqr/personal_qr/";
    public static final String BASE_LINK = "http://196.221.197.51/EasyPay/";
    public static final String API_KEY = "58e2783240320ddcfe031255d49783d4";

    //Prefs
    public static final String SHARED_PREFS = "EasyPaySharedPrefs";
    public static final String TOKEN = "TokenPrefs";
    public static final String CARD = "CardNumberPrefs";
    public static final String EMAIL = "EmailPrefs";
    public static final String PASS = "PassPrefs";
    public static final String FIRST_TIME = "FirstPrefs";
    public static final String TRAIN_TICKET = "TrainTicketPrefs";
    public static final String BUS_TIME = "BusTimePrefs";

    //IntentKeys
    public static final String MODEL_KEY = "ModelKey";
    public static final String BUNDLE_KEY = "BundleKey";

    //Patterns
    public static final Pattern PATTERN_pass_digits =
            Pattern.compile(
                    ".*[0-9].*"
            );
    public static final Pattern PATTERN_pass_lowercase =
            Pattern.compile(
                    ".*[a-z].*"
            );
    public static final Pattern PATTERN_pass_uppercase =
            Pattern.compile(
                    ".*[A-Z].*"
            );
    public static final Pattern PATTERN_pass_special =
            Pattern.compile(
                    ".*[@#$%^&+=].*"
            );
    public static final Pattern PATTERN_pass_space =
            Pattern.compile(
                    ".*[\\s].*"
            );
    public static final Pattern PATTERN_pass_length =
            Pattern.compile(
                    ".{8,}"
            );
}
