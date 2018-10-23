package com.tranxactive.j2pay.gateways;

import com.tranxactive.j2pay.gateways.core.Gateway;
import com.tranxactive.j2pay.gateways.parameters.Currency;
import com.tranxactive.j2pay.gateways.parameters.Customer;
import com.tranxactive.j2pay.gateways.parameters.CustomerCard;
import com.tranxactive.j2pay.gateways.responses.ErrorResponse;
import com.tranxactive.j2pay.gateways.responses.PurchaseResponse;
import com.tranxactive.j2pay.net.HTTPClient;
import com.tranxactive.j2pay.net.HTTPResponse;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class WirecardGateway extends Gateway {
    @Override
    public HTTPResponse purchase(JSONObject apiParameters, Customer customer, CustomerCard customerCard, Currency currency, float amount) {
        try {

            String requestString = this.buildPurchaseParameters(apiParameters, customer, customerCard, currency, amount);

            HTTPResponse httpResponse = HTTPClient.httpPost(this.getApiURL(), requestString, ContentType.APPLICATION_XML);

            return httpResponse;
        } catch (Exception e) {

        }
        return null;
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
        return null;
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

    private String getApiURL() {
        return "https://wpp-test.wirecard.com/api/payment/register";
    }

    private String buildPurchaseParameters(JSONObject apiParameters, Customer customer, CustomerCard customerCard, Currency currency, float amount) {

//        TODO Implement

        return null;
    }
}
