package cl.transbank.onepay.pos;

import android.app.Service;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.UUID;

import cl.transbank.onepay.pos.utils.DeviceUUID;
import cl.transbank.onepay.pos.utils.HTTPClient;

public class OnepayFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        HTTPClient.sendRegistrationToServer(refreshedToken, this);
    }
}
