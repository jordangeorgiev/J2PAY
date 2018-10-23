import com.tranxactive.j2pay.gateways.core.AvailableGateways;
import com.tranxactive.j2pay.gateways.core.Gateway;
import com.tranxactive.j2pay.gateways.core.GatewayFactory;
import com.tranxactive.j2pay.gateways.parameters.Country;
import com.tranxactive.j2pay.gateways.parameters.Currency;
import com.tranxactive.j2pay.gateways.parameters.Customer;
import com.tranxactive.j2pay.gateways.parameters.CustomerCard;
import com.tranxactive.j2pay.net.HTTPResponse;
import org.json.JSONObject;
import org.junit.Test;

public class TestPayseraGateway {

    @Test
    public void test(){

        Gateway gateway = GatewayFactory.getGateway(AvailableGateways.PAYSERA);
        JSONObject apiSampleParameters = gateway.getApiSampleParameters();

        apiSampleParameters.put("name", "6tVhs23PU");
        apiSampleParameters.put("transactionKey", "7m746Fz9qYNm8P8E");

        Customer customer = new Customer();

        customer
                .setFirstName("test first name")
                .setLastName("test last name")
                .setCountry(Country.US)
                .setState("TX")
                .setCity("test city")
                .setAddress("test address")
                .setZip("12345")
                .setPhoneNumber("1234567890");

        CustomerCard customerCard = new CustomerCard();

        customerCard
                .setName("test card name")
                .setNumber("5424000000000015")
                .setCvv("123")
                .setExpiryMonth("01")
                .setExpiryYear("2022");

        gateway.setTestMode(true);

        HTTPResponse response = gateway.purchase(apiSampleParameters, customer, customerCard, Currency.USD, 45);
/*
        if(response != null) {
            System.out.println (response.getJSONResponse());
            System.out.println (response.isSuccessful());
        } else {
            System.out.println("NULL response!");
        }
        */
    }


}
