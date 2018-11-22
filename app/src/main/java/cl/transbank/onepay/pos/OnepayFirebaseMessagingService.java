package cl.transbank.onepay.pos;

import android.app.Service;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class OnepayFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("POS", remoteMessage.getCollapseKey());
        Log.d("POS", remoteMessage.getFrom());
        Log.d("POS", remoteMessage.getMessageId());
        Log.d("POS", remoteMessage.getNotification().getTitle());
        Log.d("POS", remoteMessage.getNotification().getBody());

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent("transaction_result");
        localBroadcastManager.sendBroadcast(intent);
    }
}
