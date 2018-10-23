import okhttp3.*;
import org.junit.Test;

import java.io.IOException;

public class OkHttpUtils {

    @Test
    public void FormPostingparams() {
        try {
            new MyHttpPost().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class MyHttpPost {

        private final OkHttpClient client = new OkHttpClient();

        public void run() throws Exception {
            RequestBody formBody = new FormBody.Builder()
                    .add("search", "Jurassic Park")
                    .build();
            Request request = new Request.Builder()
                    .url("https://en.wikipedia.org/w/index.php")
                    .post(formBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                System.out.println(response.body().string());
            }
        }
    }

    @Test
    public void TestAuthentication() {
        try {
            new OkHttpAuthentication().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class OkHttpAuthentication {

        private final OkHttpClient client;

        public OkHttpAuthentication() {
            client = new OkHttpClient.Builder()
                    .authenticator(new Authenticator() {
                        @Override public Request authenticate(Route route, Response response) throws IOException {
                            if (response.request().header("Authorization") != null) {
                                return null; // Give up, we've already attempted to authenticate.
                            }

                            System.out.println("Authenticating for response: " + response);
                            System.out.println("Challenges: " + response.challenges());
                            String credential = Credentials.basic("jesse", "password1");
                            return response.request().newBuilder()
                                    .header("Authorization", credential)
                                    .build();
                        }
                    })
                    .build();
        }

        public void run() throws Exception {
            Request request = new Request.Builder()
                    .url("http://publicobject.com/secrets/hellosecret.txt")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                System.out.println(response.body().string());
            }
        }

    }


}
