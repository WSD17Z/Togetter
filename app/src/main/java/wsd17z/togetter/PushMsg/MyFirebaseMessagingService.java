package wsd17z.togetter.PushMsg;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import wsd17z.togetter.Activities.DriverSetRoadActivity;
import wsd17z.togetter.Activities.MainActivity;
import wsd17z.togetter.Driver.IPickupService;
import wsd17z.togetter.Driver.IUserService;

/**
 * Created by Agnieszka on 2017-12-16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        /*
            Push messages have to be sent as data, not a notification.
            Firebase console is not an option, only via standard POST request - I prefer Postman for this.
            Way to build proper request: https://stackoverflow.com/a/37845174/4902775
            add 3 keys: isRealize, driverEmail, clientEmail
         */
        try {
            final FirebaseMessage msg = new FirebaseMessage(remoteMessage);
            if (msg.getIsRealize()) {
                IFuture<IPickupService> userFuture = MainActivity.getPlatform().getService(MainActivity.getPlatform().getPlatformId(), IPickupService.class);
                userFuture.addResultListener(new IResultListener<IPickupService>() {
                    @Override
                    public void exceptionOccurred(Exception exception) {
                        Log.d("ERR", exception.toString());
                    }

                    @Override
                    public void resultAvailable(IPickupService result) {
                        // Create offer for demo
                        result.createPickupOffer(52.144956, 21.018605, 52.138400, 21.047565, "userEmail@com");
                        Intent resultIntent = result.realizePickup(msg.getDriverEmail(), msg.getClientEmail());
                        if (resultIntent != null) {
                            startActivity(resultIntent);
                        }
                    }
                });
            }
        } catch(Exception ex) {}

        if (remoteMessage.getNotification() != null) {
            RemoteMessage.Notification note = remoteMessage.getNotification();
            String body = note.getBody();
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onDeletedMessages() {
        Log.w(TAG, "Message got deleted!");
    }
}
