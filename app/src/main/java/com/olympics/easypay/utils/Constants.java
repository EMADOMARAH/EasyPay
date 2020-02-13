package com.olympics.easypay.utils;

import com.google.android.gms.wallet.WalletConstants;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Constants {
    //Network
    public static final String BASE_URL = "http://196.221.197.51/";
    public static final String BASE_QR = "EasyPayQr/";

    //Prefs
    public static final String SHARED_PREFS = "EasyPaySharedPrefs";
    public static final String TOKEN = "TokenPrefs";
    public static final String CARD = "CardNumberPrefs";
    public static final String EMAIL = "EmailPrefs";
    public static final String PASS = "PassPrefs";
    public static final String FIRST_TIME = "FirstPrefs";
    public static final String TRAIN_TICKET = "TrainTicketPrefs";

    //IntentKeys
    public static final String MODEL_KEY = "ModelKey";

    //Patterns
    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    //Google Pay
    public static final int PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;

    public static final List<String> SUPPORTED_NETWORKS = Collections.singletonList("VISA");

    public static final List<String> SUPPORTED_METHODS = Arrays.asList("PAN_ONLY", "CRYPTOGRAM_3DS");

    public static final String COUNTRY_CODE = "EG";

    public static final String CURRENCY_CODE = "EGP";

    public static final String PAYMENT_GATEWAY_TOKENIZATION_NAME = "example";

    public static final HashMap<String, String> PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS =
            new HashMap<String, String>() {
                {
                    put("gateway", PAYMENT_GATEWAY_TOKENIZATION_NAME);
                    put("gatewayMerchantId", "exampleGatewayMerchantId");
                }
            };

    public static final String DIRECT_TOKENIZATION_PUBLIC_KEY = "REPLACE_ME";

    public static final HashMap<String, String> DIRECT_TOKENIZATION_PARAMETERS =
            new HashMap<String, String>() {
                {
                    put("protocolVersion", "ECv2");
                    put("publicKey", DIRECT_TOKENIZATION_PUBLIC_KEY);
                }
            };
}
