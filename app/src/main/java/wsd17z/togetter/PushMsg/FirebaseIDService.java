package wsd17z.togetter.PushMsg;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Agnieszka on 2017-12-16.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    public static String FIREBASE_TOKEN;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        FIREBASE_TOKEN = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + FIREBASE_TOKEN);

        // TODO: Implement this method to send any registration to your app's servers.
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(FIREBASE_TOKEN);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}
