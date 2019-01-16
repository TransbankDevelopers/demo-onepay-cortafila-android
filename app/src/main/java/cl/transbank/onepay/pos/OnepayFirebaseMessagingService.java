package cl.transbank.onepay.pos;

import android.app.Service;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

import cl.transbank.onepay.pos.utils.KeyValuePersistence;

public class OnepayFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("POS", "data: "+ remoteMessage.getData());

        HashMap paymentDataHashMap = new HashMap(remoteMessage.getData());

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent("transaction_result");
        intent.putExtra("data",  paymentDataHashMap);
        KeyValuePersistence.setLastPayment(this, paymentDataHashMap);
        localBroadcastManager.sendBroadcast(intent);
    }
}
