package delivery.com.proxy;

import java.io.IOException;

import delivery.com.util.CheckHandshake;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BaseProxy {
    public static OkHttpClient client;

    public String postPlain(String uri, RequestBody formBody) throws IOException {
        Request request =  new Request.Builder()
                .url(uri)
                .post(formBody)
                .build();

        String responseBody;

        CheckHandshake handshake = new CheckHandshake();
        client = handshake.getClient();
        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        responseBody = response.body().string();

        return responseBody;
    }
}