package cl.transbank.onepay.pos.utils;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import cl.transbank.onepay.pos.model.Item;

public class HTTPClient {

    static String BASE_URL = "http://2b31b8d7.ngrok.io";

    public interface HTTPClientListener {
        public void onCompleted(JsonObject result);
    }

    public static void sendRegistrationToServer(String token, Context context, final HTTPClientListener callback) {
        JsonObject json = new JsonObject();
        json.addProperty("deviceid", DeviceUUID.getUUID(context));
        json.addProperty("token", token);

        Ion.with(context)
                .load(BASE_URL + "/setup/register")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (callback != null) {
                            callback.onCompleted(result);
                        }
                    }
                });
    }

    public static void createTransaction(ArrayList<Item> mItems, Context context, final HTTPClientListener callback) {
        JsonObject json = new JsonObject();
        String deviceid = DeviceUUID.getUUID(context);

        json.addProperty("deviceid", DeviceUUID.getUUID(context));

        Ion.with(context)
                .load(BASE_URL + "/transaction/"+ deviceid + "/create")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        if (callback != null) {
                            callback.onCompleted(result);
                        }
                    }
                });
    }
}