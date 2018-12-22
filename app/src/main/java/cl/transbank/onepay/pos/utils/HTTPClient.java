package cl.transbank.onepay.pos.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import cl.transbank.onepay.pos.model.Item;

public class HTTPClient {

    static String BASE_URL;

    public interface HTTPClientListener {
        public void onCompleted(JsonObject result);
    }

    private static String getBaseURL(Context context)  {

        if (BASE_URL == null) {
            Properties properties = new Properties();
            InputStream inputStream = null;
            try {
                inputStream = context.getAssets().open("app.properties");
                properties.load(inputStream);
                BASE_URL = properties.getProperty("server_url");
            } catch (IOException e) {
                Log.i("Cortafilas", "No server_url property, using the default hardcoded value");
                BASE_URL = "https://cortafilas-onepay.herokuapp.com";
            }
        }

        return BASE_URL;
    }

    public static void sendRegistrationToServer(String token, Context context, final HTTPClientListener callback) {
        JsonObject json = new JsonObject();
        json.addProperty("deviceid", DeviceUUID.getUUID(context));
        json.addProperty("token", token);

        Ion.with(context)
                .load(getBaseURL(context) + "/setup/register")
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

        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(mItems);

        json.addProperty("deviceid", DeviceUUID.getUUID(context));
        json.add("items", jsonElement);

        Ion.with(context)
                .load(getBaseURL(context) + "/transaction/"+ deviceid + "/create")
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

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        boolean connected = networkInfo != null && networkInfo.isAvailable() &&
                networkInfo.isConnected();

        return connected;
    }
}