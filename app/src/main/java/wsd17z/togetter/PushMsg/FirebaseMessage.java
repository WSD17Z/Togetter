package wsd17z.togetter.PushMsg;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Kosmos on 06/01/2018.
 */

public class FirebaseMessage {
    private boolean isRealize;
    private String driverEmail, clientEmail;

    FirebaseMessage(RemoteMessage remoteMessage) throws Exception {
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();

            if (data.containsKey("isRealize")) {
                isRealize = data.get("isRealize").equalsIgnoreCase("true");
            }

            if (data.containsKey("driverEmail")) {
                driverEmail = data.get("driverEmail");
            }

            if (data.containsKey("clientEmail")) {
                clientEmail = data.get("clientEmail");
            }
        } else {
            throw new Exception("Remote message did not contain proper payload!");
        }
    }

    public boolean getIsRealize() {
        return isRealize;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public String getClientEmail() {
        return clientEmail;
    }
}
