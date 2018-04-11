package com.wifiscanner.utils;


import com.wifiscanner.WifiConstants;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Utils implements WifiConstants {


    public static String getMerchantDetails() {
        Map<String, String> merchantMap = new HashMap<>();
        merchantMap.put("amount", "122");
        merchantMap.put("orderId", generateOrderId());
        merchantMap.put("ostaTransactionReferenceId", generateOrderId());
        merchantMap.put("merchantTransactionReferenceId", generateTxnReferenceId());
        merchantMap.put("partnerReferenceId", "M1234567");

        String jsonStr = convertMapToString(merchantMap);
        return jsonStr;
    }

    public static String convertMapToString(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            String value = map.get(key);
            try {
                stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
                stringBuilder.append("=");
                stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }

        return stringBuilder.toString();
    }

    public static Map<String, String> convertStringToMap(String input) {
        Map<String, String> map = new HashMap<String, String>();

        String[] nameValuePairs = input.split("&");
        for (String nameValuePair : nameValuePairs) {
            String[] nameValue = nameValuePair.split("=");
            try {
                map.put(URLDecoder.decode(nameValue[0], "UTF-8"), nameValue.length > 1 ? URLDecoder.decode(
                        nameValue[1], "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }

        return map;
    }

    public static String generateOrderId() {
        return UUID.randomUUID().toString().toString().replaceAll("-", "");
    }

    public static String generateTxnReferenceId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
