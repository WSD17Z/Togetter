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
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> datamdg = remoteMessage.getData();
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (datamdg.containsKey("isRealize") && datamdg.get("isRealize").equalsIgnoreCase("true")) {
                final String driverEmail = datamdg.get("driverEmail");
                final String clientEmail = datamdg.get("clientEmail");
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
                        Intent resultIntent = result.realizePickup(driverEmail, clientEmail);
                        if (resultIntent != null) {
                            startActivity(resultIntent);
                        }
                    }
                });
            }
        }


        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getNotification() != null) {
            RemoteMessage.Notification note = remoteMessage.getNotification();
            String body = note.getBody();
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        //Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }

    @Override
    public void onDeletedMessages() {
        Log.d(TAG, "Message got deleted!");

    }
}
