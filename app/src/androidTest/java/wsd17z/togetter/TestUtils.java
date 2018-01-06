package wsd17z.togetter;

import android.support.test.InstrumentationRegistry;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import wsd17z.togetter.Agents.ClientAgent;
import wsd17z.togetter.Agents.DbManagementAgent;
import wsd17z.togetter.Agents.DriverAgent;
import wsd17z.togetter.Agents.WalletAgent;
import wsd17z.togetter.DbManagement.DbManagementService;
import wsd17z.togetter.DbManagement.DbUserObject;
import wsd17z.togetter.Driver.IPickupService;
import wsd17z.togetter.Driver.PickupService;
import wsd17z.togetter.Wallet.WalletService;

/**
 * Created by Kosmos on 06/01/2018.
 */

public class TestUtils {
    public static final String DRIVER_EMAIL = "testDriver@email";
    public static final String CLIENT_EMAIL = "testClient@email";
    // Rybitwy 8
    public static final LatLng ENDPOINT_START = new LatLng(52.15035839999999, 21.019069);
    // Belgradzka 21A
    public static final LatLng ENDPOINT_END = new LatLng(52.1389258, 21.0546673);
    // Pustułeczki 40
    public static final LatLng WAYPOINT_START = new LatLng(52.144927, 21.0187804);
    // Kazury 2
    public static final LatLng WAYPOINT_END = new LatLng(52.1388705, 21.0456728);


    public static DriverAgent createTestDriverAgent() {
        DbManagementAgent.initDb(InstrumentationRegistry.getTargetContext());
        DbManagementService dbs = new DbManagementService(new DbManagementAgent());
        try {
            DbUserObject usr = new DbUserObject(CLIENT_EMAIL, CLIENT_EMAIL.hashCode());
            dbs.addUser(usr);
        } catch (Exception ex) {}

        try {
            DbUserObject usr = new DbUserObject(DRIVER_EMAIL, DRIVER_EMAIL.hashCode());
            dbs.addUser(usr);
        } catch (Exception ex) {}

        WalletService ws = new WalletService(new WalletAgent(dbs));
        DriverAgent da = new DriverAgent(10, 30, DRIVER_EMAIL, ws, dbs);
        da.setEndPoints(ENDPOINT_START, ENDPOINT_END);
        return da;
    }

    public static ClientAgent createTestClientAgent() {
        PickupService ps = new PickupService(createTestDriverAgent());
        List<IPickupService> pss = new ArrayList<>();
        pss.add(ps);
        ClientAgent ca = new ClientAgent(pss);
        ca.setEmail(CLIENT_EMAIL);
        ca.setEndPoints(WAYPOINT_START, WAYPOINT_END);
        return ca;
    }
}
