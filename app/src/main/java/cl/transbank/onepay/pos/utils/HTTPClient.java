package cl.transbank.onepay.pos.utils;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class HTTPClient {
    public static void sendRegistrationToServer(String token, Context context) {
        JsonObject json = new JsonObject();
        json.addProperty("deviceid", DeviceUUID.getUUID(context));
        json.addProperty("token", token);

        Ion.with(context)
                .load("http://7fe59832.ngrok.io/setup/register")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                    }
                });
    }
}