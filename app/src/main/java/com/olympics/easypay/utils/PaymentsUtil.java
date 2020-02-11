package com.olympics.easypay.utils;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class PaymentsUtil {
    private static final BigDecimal MICROS = new BigDecimal(1000000d);

    private PaymentsUtil() {
    }

    private static JSONObject getBaseRequest() throws JSONException {
        return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
    }

    public static PaymentsClient createPaymentsClient(Activity activity) {
        Wallet.WalletOptions walletOptions =
                new Wallet.WalletOptions.Builder().setEnvironment(Constants.PAYMENTS_ENVIRONMENT).build();
        return Wallet.getPaymentsClient(activity, walletOptions);
    }

    private static JSONObject getGatewayTokenizationSpecification() throws JSONException {
        return new JSONObject() {{
            put("type", "PAYMENT_GATEWAY");
            put("parameters", new JSONObject() {
                {
                    put("gateway", "example");
                    put("gatewayMerchantId", "exampleGatewayMerchantId");
                }
            });
        }};
    }

//    private static JSONObject getDirectTokenizationSpecification()
//            throws JSONException, RuntimeException {
//        if (Constants.DIRECT_TOKENIZATION_PARAMETERS.isEmpty()
//                || Constants.DIRECT_TOKENIZATION_PUBLIC_KEY.isEmpty()
//                || Constants.DIRECT_TOKENIZATION_PUBLIC_KEY == null
//                || Constants.DIRECT_TOKENIZATION_PUBLIC_KEY == "REPLACE_ME") {
//            throw new RuntimeException(
//                    "Please edit the Constants.java file to add protocol version & public key.");
//        }
//        JSONObject tokenizationSpecification = new JSONObject();
//
//        tokenizationSpecification.put("type", "DIRECT");
//        JSONObject parameters = new JSONObject(Constants.DIRECT_TOKENIZATION_PARAMETERS);
//        tokenizationSpecification.put("parameters", parameters);
//
//        return tokenizationSpecification;
//    }

    private static JSONArray getAllowedCardNetworks() throws JSONException {
        return new JSONArray(Constants.SUPPORTED_NETWORKS);
    }

    private static JSONArray getAllowedCardAuthMethods() {
        return new JSONArray(Constants.SUPPORTED_METHODS);
    }

    private static JSONObject getBaseCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = new JSONObject();
        cardPaymentMethod.put("type", "CARD");

        JSONObject parameters = new JSONObject();
        parameters.put("allowedAuthMethods", getAllowedCardAuthMethods());
        parameters.put("allowedCardNetworks", getAllowedCardNetworks());
        // Optionally, you can add billing address/phone number associated with a CARD payment method.
        parameters.put("billingAddressRequired", false);

        cardPaymentMethod.put("parameters", parameters);

        return cardPaymentMethod;
    }

    private static JSONObject getCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = getBaseCardPaymentMethod();
        cardPaymentMethod.put("tokenizationSpecification", getGatewayTokenizationSpecification());

        return cardPaymentMethod;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Optional<JSONObject> getIsReadyToPayRequest() {
        try {
            JSONObject isReadyToPayRequest = getBaseRequest();
            isReadyToPayRequest.put(
                    "allowedPaymentMethods", new JSONArray().put(getBaseCardPaymentMethod()));

            return Optional.of(isReadyToPayRequest);
        } catch (JSONException e) {
            return Optional.empty();
        }
    }

    private static JSONObject getTransactionInfo(String price) throws JSONException {
        JSONObject transactionInfo = new JSONObject();
        transactionInfo.put("totalPrice", price);
        transactionInfo.put("totalPriceStatus", "FINAL");
        transactionInfo.put("countryCode", Constants.COUNTRY_CODE);
        transactionInfo.put("currencyCode", Constants.CURRENCY_CODE);

        return transactionInfo;
    }

    private static JSONObject getMerchantInfo() throws JSONException {
        return new JSONObject().put("merchantName", "Example Merchant");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Optional<JSONObject> getPaymentDataRequest(String price) {
        try {
            JSONObject paymentDataRequest = PaymentsUtil.getBaseRequest();
            paymentDataRequest.put(
                    "allowedPaymentMethods", new JSONArray().put(PaymentsUtil.getCardPaymentMethod()));
            paymentDataRequest.put("transactionInfo", PaymentsUtil.getTransactionInfo(price));
            paymentDataRequest.put("merchantInfo", PaymentsUtil.getMerchantInfo());

            paymentDataRequest.put("shippingAddressRequired", false);

            return Optional.of(paymentDataRequest);
        } catch (JSONException e) {
            return Optional.empty();
        }
    }

    public static String microsToString(long micros) {
        return new BigDecimal(micros).divide(MICROS).setScale(2, RoundingMode.HALF_EVEN).toString();
    }
}
