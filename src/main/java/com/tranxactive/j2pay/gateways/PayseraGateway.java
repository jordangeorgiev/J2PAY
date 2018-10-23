package com.tranxactive.j2pay.gateways;

import com.tranxactive.j2pay.gateways.core.Gateway;
import com.tranxactive.j2pay.gateways.parameters.Currency;
import com.tranxactive.j2pay.gateways.parameters.Customer;
import com.tranxactive.j2pay.gateways.parameters.CustomerCard;
import com.tranxactive.j2pay.gateways.responses.ErrorResponse;
import com.tranxactive.j2pay.gateways.responses.PurchaseResponse;
import com.tranxactive.j2pay.net.HTTPClient;
import com.tranxactive.j2pay.net.HTTPResponse;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class PayseraGateway extends Gateway {
    @Override
    public HTTPResponse purchase(JSONObject apiParameters, Customer customer, CustomerCard customerCard, Currency currency, float amount) {

/*

        Example request for payment with no items
        POST /rest/v1/payment HTTP/1.1
        Host: wallet.paysera.com
        Content-Type: application/json;charset=utf-8
        User-Agent: Paysera WalletApi PHP library
        Authorization: MAC id="wkVd93h2uS",
                        ts="1343811600",
                        nonce="nQnNaSNyubfPErjRO55yaaEYo9YZfKHN",
                        mac="Xk/+C/KOEapGtvaLjV6mKvk/0BfWydb/zlHFyoBx/Us=",
                        ext="body_hash=esRCxyYPpkKwaNzTaj0wtFwLG6gNw%2FCHw%2FIz2j5vp7Q%3D"
*/

        JSONObject resp;
        JSONObject requestJSON = this.buildPurchaseParameters(apiParameters, customer, customerCard, currency, amount);
        String requestString = requestJSON.toString();

        System.out.println ("requestString: " + requestString );

        PurchaseResponse successResponse = null;
        ErrorResponse errorResponse = new ErrorResponse();

        HTTPResponse httpResponse;

        int result;

        HashMap<String, String> headers = new HashMap<>();

        String signature = null;
        try {
            signature = generateSignature(
                    "wkVd93h2uS",
                    "IrdTc8uQodU7PRpLzzLTW6wqZAO6tAMU",
                    ""+System.currentTimeMillis(),
                    apiParameters.toMap()
            );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        headers.put("Authorization", signature);
        System.out.println("signature: " + signature);
        System.out.println("headers: " + headers);

        httpResponse = HTTPClient.httpPost(getApiURL(), requestString, ContentType.APPLICATION_JSON, headers);

        System.out.println("StatusCode: " + httpResponse.getStatusCode());
        System.out.println("JSONResponse: " + httpResponse.getJSONResponse());
        System.out.println("ResponseTime: " + httpResponse.getResponseTime());
        System.out.println("httpResponse.getContent: " + httpResponse.getContent());
        System.out.println("Success: " + httpResponse.isSuccessful());

        return httpResponse;
    }

    @Override
    public HTTPResponse refund(JSONObject apiParameters, JSONObject refundParameters, float amount) {
        return null;
    }

    @Override
    public HTTPResponse rebill(JSONObject apiParameters, JSONObject rebillParameters, float amount) {
        return null;
    }

    @Override
    public HTTPResponse voidTransaction(JSONObject apiParameters, JSONObject voidParameters) {
        return null;
    }

    @Override
    public JSONObject getApiSampleParameters() {
        return new JSONObject()
                .put("name", "also called api user name / api login id")
                .put("transactionKey", "the transaction key");
    }

    @Override
    public JSONObject getRefundSampleParameters() {
        return null;
    }

    @Override
    public JSONObject getRebillSampleParameters() {
        return null;
    }

    @Override
    public JSONObject getVoidSampleParameters() {
        return null;
    }


    //private methods are starting below.
    private JSONObject buildPurchaseParameters(JSONObject apiParameters, Customer customer, CustomerCard customerCard, Currency currency, float amount) {

/*
        {
            "description": "Payment for order No. 1234",
                "price": 1299,
                "currency": "EUR",
                "parameters": {
                "orderid": 1234
            }
        }
        */
        JSONObject object = new JSONObject();

        object
                .put("price", amount * 100)
                .put("currency", currency)
                .put("description", "Payment for order No. 1234")
                .put("parameters", new JSONObject()
                        .put("name", customer.getFirstName() + " " + customer.getLastName())
                        .put("orderid", customerCard.getNumber())
                );

        return object;
    }


    private String getApiURL() {
        return "https://wallet.paysera.com/rest/v1/payment";
    }

    private String generateNonce() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[256/8];
        secureRandom.nextBytes(bytes);
        return new String(Base64.encodeBase64(bytes));
    }

    private String generateSignature(
            String macId,
            String macSecret,
            /*Request originalRequest,*/
           /* byte[] body,*/
            String timestamp,
            Map<String, Object> parameters
    ) throws NoSuchAlgorithmException, InvalidKeyException {
        String nonce = generateNonce();
        byte[] bytes = new byte[64];
        String ext = generateExt(bytes, parameters);
/*

        String host = originalRequest.url().host();
        Integer port = originalRequest.url().port();
        String method = originalRequest.method();
        String path = originalRequest.url().encodedPath();
        String query = originalRequest.url().encodedQuery();

        if (query != null && !query.isEmpty()) {
            HttpUrl url = (new HttpUrl.Builder())
                    .scheme("https")
                    .host(host)
                    .encodedQuery(query)
                    .build();

            path += "?" + url.encodedQuery();
        }
*/

        // mac
        String mac = calculateMAC(
                timestamp,
                nonce,
                macSecret,
                "POST",
                "/rest/v1/payment",
                "wallet.paysera.com",
                10145,
                ext
        );

        String authorizationHeader = String.format(
                "MAC id=\"%s\", ts=\"%s\", nonce=\"%s\", mac=\"%s\"",
                macId,
                timestamp,
                nonce,
                mac
        );

        if (ext != null) {
            authorizationHeader += ", ext=\"" + ext + "\"";
        }

        return authorizationHeader;
    }

    private String generateExt(byte[] content, Map<String, Object> parameters) throws NoSuchAlgorithmException {
        Map<String, Object> extParameters = new HashMap<>();

        if (content != null && content.length > 0) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] contentHash = digest.digest(content);

            extParameters.put("body_hash", new String(Base64.encodeBase64(contentHash)));
        }

        extParameters.putAll(parameters);

        return convertToEncodedQueryString(extParameters);
    }

    private String convertToEncodedQueryString(Map<?, ?> map) {
        if (map.size() == 0) {
            return "";
        }
        HttpUrl.Builder urlBuilder =
                (new HttpUrl.Builder()).scheme("https").host("wallet.paysera.com");

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey().toString(), entry.getValue().toString());
        }

        return urlBuilder.build().encodedQuery();
    }

    private String calculateMAC(String timestamp, String nonce, String secret, String httpMethod, String path, String host, Integer port, String ext) throws NoSuchAlgorithmException, InvalidKeyException {
        final StringBuilder macStringBuilder = new StringBuilder()
                .append(timestamp)
                .append("\n")
                .append(nonce)
                .append("\n")
                .append(httpMethod)
                .append("\n")
                .append(path)
                .append("\n")
                .append(host)
                .append("\n")
                .append(port)
                .append("\n")
                .append(ext)
                .append("\n");

        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");

        mac.init(secretKey);

        byte[] HMACdigest = mac.doFinal(macStringBuilder.toString().getBytes());

        return new String(Base64.encodeBase64(HMACdigest));
    }

}
