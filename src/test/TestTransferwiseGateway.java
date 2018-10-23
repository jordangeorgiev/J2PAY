import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;

public class TestTransferwiseGateway {

    private static OkHttpClient client = new OkHttpClient();
    private static String baseApiURL = "https://api.sandbox.transferwise.tech/v1/";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Test
    public void checkAccountBalance(){
        //Get your profile id
        Request request = new Request.Builder()
                .header("Authorization", "Bearer c7464d81-63a5-42f9-b485-cec50311260b")
                .url(baseApiURL + "profiles")
                .build();
        execute(client, request);
    }

    @Test
    public void getProfileDetails(){
        int profileId=1203;
        //Check account balance
        Request request = new Request.Builder()
                .header("Authorization", "Bearer c7464d81-63a5-42f9-b485-cec50311260b")
                .url(baseApiURL + "borderless-accounts?profileId=" + profileId)
                .build();
        execute(client, request);
}

    @Test
    public void createQuote() {
        //TODO Step 1: Create a quote
/*      curl -X POST https://api.sandbox.transferwise.tech/v1/quotes \
        -H "Authorization: Bearer c7464d81-63a5-42f9-b485-cec50311260b" \
        -H "Content-Type: application/json" \
        -d '{
        "profile": 1203,
                "source": "EUR",
                "target": "GBP",
                "rateType": "FIXED",
                "targetAmount": 6,
                "type": "BALANCE_PAYOUT"
    }'
*/

        String json = "{\n" +
                "        \"profile\": 1203,\n" +
                "                \"source\": \"EUR\",\n" +
                "                \"target\": \"GBP\",\n" +
                "                \"rateType\": \"FIXED\",\n" +
                "                \"targetAmount\": 6,\n" +
                "                \"type\": \"BALANCE_PAYOUT\"\n" +
                "    }";
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .header("Authorization", "Bearer c7464d81-63a5-42f9-b485-cec50311260b")
                //Content-Type: application/json
                .header("Content-Type", "application/json")
                .url(baseApiURL + "quotes")
                .post(requestBody)
                .build();

        execute(client, request);
    }

    @Test
    public void listRecipientAccounts() {
//        GET https://api.sandbox.transferwise.tech/v1/accounts?profile=<profileId>&currency=<currencyCode>
        int profileId=1203;
        //Check account balance
        Request request = new Request.Builder()
                .header("Authorization", "Bearer c7464d81-63a5-42f9-b485-cec50311260b")
                .url(baseApiURL + "accounts?profile=" + profileId)
                .build();
        execute(client, request);
    }

    @Test
    public void createRecipientAccount() {
        /*
        Step 2: Create a recipient account

        Recipient is a person or institution who is the ultimate beneficiary of your payment.

        Recipient bank account details are different for different currencies.
        For example, you only need to know the IBAN number to send payments to most European and Nordic countries.
        But in order to send money to Canada, you’d need to fill out four fields: You recipient’s institution number,
                transit number, account number, and account type.

        curl -X POST https://api.sandbox.transferwise.tech/v1/accounts \
        -H "Authorization: Bearer c7464d81-63a5-42f9-b485-cec50311260b" \
        -H "Content-Type: application/json" \
        -d '{
        "currency": "GBP",
                "type": "sort_code",
                "profile": 1203,
                "accountHolderName": "Ann Johnson",
                "legalType": "PRIVATE",
                "details": {
                    "sortCode": "231470",
                    "accountNumber": "28821822"
                }
            }'
    */

        String json = "{\n" +
                "        \"currency\": \"GBP\",\n" +
                "                \"type\": \"sort_code\",\n" +
                "                \"profile\": 1203,\n" +
                "                \"accountHolderName\": \"Ann Johnson\",\n" +
                "                \"legalType\": \"PRIVATE\",\n" +
                "                \"details\": {\n" +
                "                    \"sortCode\": \"231470\",\n" +
                "                    \"accountNumber\": \"28821822\"\n" +
                "                }\n" +
                "            }";
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .header("Authorization", "Bearer c7464d81-63a5-42f9-b485-cec50311260b")
                //Content-Type: application/json
                .header("Content-Type", "application/json")
                .url(baseApiURL + "accounts")
                .post(requestBody)
                .build();

        execute(client, request);
    }


    @Test
    public void createTransfer() {
/*
        Step 3: Create a transfer

        A transfer is a payout order you make to a recipient account based on a quote.
        Once created, a transfer will need to be funded within the next 5 working days, or it’ll automatically get cancelled.

        curl -X POST https://api.sandbox.transferwise.tech/v1/transfers \
        -H "Authorization: Bearer c7464d81-63a5-42f9-b485-cec50311260b" \
        -H "Content-Type: application/json" \
        -d '{
        "targetAccount": 14247397,
                "quote": 1407478,
                "customerTransactionId": "c38ed6bc-2b48-44cb-9365-35eb2c363ee1",
                "details" : {
                "reference" : "to my friend",
                    "transferPurpose": "verification.transfers.purpose.pay.bills",
                    "sourceOfFunds": "verification.source.of.funds.other"
                    }
            }'
      */


        String json = "{\n" +
                "        \"targetAccount\": 14247397,\n" +
                "                \"quote\": 1407478,\n" +
                "                \"customerTransactionId\": \"c38ed6bc-2b48-44cb-9365-35eb2c363ee1\",\n" +
                "                \"details\" : {\n" +
                "                \"reference\" : \"to my friend\",\n" +
                "                    \"transferPurpose\": \"verification.transfers.purpose.pay.bills\",\n" +
                "                    \"sourceOfFunds\": \"verification.source.of.funds.other\"\n" +
                "                    }\n" +
                "            }";
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .header("Authorization", "Bearer c7464d81-63a5-42f9-b485-cec50311260b")
                //Content-Type: application/json
                .header("Content-Type", "application/json")
                .url(baseApiURL + "transfers")
                .post(requestBody)
                .build();

        execute(client, request);
    }

    @Test
    public void fundTransfer() {
        fundTransfer("47515412");
    }

    public void fundTransfer(String id) {
        /*
        Step 4: Fund a transfer

        This API call is the final step for executing payouts.
        TransferWise will now debit funds from your borderless account balance and start processing your transfer.
        If your borderless balance is short of funds then this call will fail with "insufficient funds" error.

                curl -X POST https://api.sandbox.transferwise.tech/v1/transfers/47515412/payments \
                -H "Authorization: Bearer c7464d81-63a5-42f9-b485-cec50311260b" \
                -H "Content-Type: application/json" \
                -d '{
                    "type": "BALANCE"
                }'
    */
        String json = "{\n" +
                "                    \"type\": \"BALANCE\"\n" +
                "                }";
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .header("Authorization", "Bearer c7464d81-63a5-42f9-b485-cec50311260b")
                //Content-Type: application/json
                .header("Content-Type", "application/json")
                .url(baseApiURL + "transfers/" + id + "/payments")
                .post(requestBody)
                .build();

        execute(client, request);
    }

    @Test()
    public void getTransferInfo(){
        getTransferInfo("47515412");
    }

    public void getTransferInfo(String id) {
/*      Get transfer info by id
        curl -X GET https://api.sandbox.transferwise.tech/v1/transfers/47515412 \
        -H "Authorization: Bearer c7464d81-63a5-42f9-b485-cec50311260b"
        */
        Request request = new Request.Builder()
                .header("Authorization", "Bearer c7464d81-63a5-42f9-b485-cec50311260b")
                .url(baseApiURL + "transfers/" + id)
                .build();
        execute(client, request);
    }

    @Test
    public void getDeliveryEstimate() {
        getDeliveryEstimate("47515412");
    }

    public void getDeliveryEstimate(String id) {
/*
        GET https://api.sandbox.transferwise.tech/v1/delivery-estimates/47515412 \
        -H "Authorization: Bearer c7464d81-63a5-42f9-b485-cec50311260b"
            */
        Request request = new Request.Builder()
                .header("Authorization", "Bearer c7464d81-63a5-42f9-b485-cec50311260b")
                .url(baseApiURL + "delivery-estimates/" + id)
                .build();
        execute(client, request);
    }


    @Test
    public void listTransfers(){

        /*curl -X GET
        https://api.sandbox.transferwise.tech/v1/transfers?offset=0&limit=100&profile=1203&status=funds_refunded&createdDateStart=2018-09-15&createdDateEnd=2018-12-30 -H "Authorization: Bearer c7464d81-63a5-42f9-b485-cec50311260b" */

        Request request = new Request.Builder()
                .header("Authorization", "Bearer c7464d81-63a5-42f9-b485-cec50311260b")
                .url(baseApiURL + "transfers?offset=0&" +
                        "limit=100&profile=1203&" +
                       /* "status=funds_refunded&" +*/
                        "createdDateStart=2018-09-15&" +
                        "createdDateEnd=2018-12-30")
                .build();
        execute(client, request);
    }

    @Test
    public void listRates() {

        listRates("USD", null);
        listRates("", "EUR");
        listRates("EUR", "USD");
    }

    public void listRates(String source, String target) {

        /*curl -X GET https://api.sandbox.transferwise.tech/v1/rates
         ?source=EUR&target=USD
     -H "Authorization: Bearer c7464d81-63a5-42f9-b485-cec50311260b"*/
        String url = "rates";
        if(!StringUtils.isAllBlank(source, target)) url = StringUtils.join(url, "?");
        if(StringUtils.isNoneBlank(source)) url = StringUtils.join(url, "source=", source, "&");
        if(StringUtils.isNoneBlank(target)) url = StringUtils.join(url, "target=", target, "&");

        Request request = new Request.Builder()
                .header("Authorization", "Bearer c7464d81-63a5-42f9-b485-cec50311260b")
                .url(baseApiURL + url)
                .build();
        execute(client, request);
    }

//    Private util methods
    private Response execute(OkHttpClient client, Request request) {
        Response response = null;
        try {
            response = client.newCall(request).execute();
            printResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void printResponse(Response response) throws IOException {
        if(response == null) {
            System.out.println("NULL Response");
            return;
        }
        System.out.println(response);
        System.out.println("isSuccessful: " + response.isSuccessful());
        String responseData = response.body().string();
//        System.out.println(responseData);
        System.out.println(toPrettyFormat(responseData));
    }

    /**
     * Convert a JSON string to pretty print version
     * @param jsonString
     * @return
     */
    public static String toPrettyFormat(String jsonString)
    {
        return new GsonBuilder().setPrettyPrinting().create().toJson(
                new JsonParser().parse(jsonString));
    }
}
