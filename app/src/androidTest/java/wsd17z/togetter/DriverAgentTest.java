package wsd17z.togetter;

import android.content.Intent;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.runner.RunWith;

import wsd17z.togetter.Agents.DriverAgent;
import wsd17z.togetter.Driver.PickupOffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static wsd17z.togetter.TestUtils.CLIENT_EMAIL;
import static wsd17z.togetter.TestUtils.DRIVER_EMAIL;
import static wsd17z.togetter.TestUtils.WAYPOINT_END;
import static wsd17z.togetter.TestUtils.WAYPOINT_START;
import static wsd17z.togetter.TestUtils.createTestDriverAgent;

// TODO: Add database checks to the tests
@RunWith(AndroidJUnit4.class)
public class DriverAgentTest {
    @Test
    public void ctorTest() throws Exception {
        DriverAgent da = new DriverAgent();
        assertNotNull(da);
    }

    @Test
    public void createPickupOfferTest() throws Exception {
        DriverAgent da = createTestDriverAgent();
        PickupOffer po = da.createPickupOffer(WAYPOINT_START, WAYPOINT_END, CLIENT_EMAIL);

        assertEquals(DRIVER_EMAIL, po.getDriverEmail());
        assertEquals(CLIENT_EMAIL, po.getEmail());
        assertNotNull(po);
    }

    @Test
    public void createPickupOfferDistanceTest() throws Exception {
        DriverAgent da = createTestDriverAgent();
        da.setEndPoints(new LatLng(0,0), new LatLng(0,0));
        PickupOffer po = da.createPickupOffer(WAYPOINT_START, WAYPOINT_END, CLIENT_EMAIL);
        assertNull(po);
    }

    @Test
    public void realizePickupTest() throws Exception {
        DriverAgent da = createTestDriverAgent();

        PickupOffer po = da.createPickupOffer(WAYPOINT_START, WAYPOINT_END, CLIENT_EMAIL);
        Intent in = da.realizePickup(CLIENT_EMAIL, CLIENT_EMAIL);
        assertNull(in);

        in = da.realizePickup(DRIVER_EMAIL, DRIVER_EMAIL);
        assertNull(in);

        in = da.realizePickup(DRIVER_EMAIL, CLIENT_EMAIL);
        assertNotNull(in);
    }

    @Test
    public void startPickupTest() throws Exception {
        DriverAgent da = createTestDriverAgent();

        PickupOffer po = da.createPickupOffer(WAYPOINT_START, WAYPOINT_END, CLIENT_EMAIL);
        Intent in = da.realizePickup(DRIVER_EMAIL, CLIENT_EMAIL);
        da.startPickup(DRIVER_EMAIL, CLIENT_EMAIL);

        // If above code throws, it won't enter the assert
        assertTrue(true);
    }

    @Test
    public void endPickupTest() throws Exception {
        DriverAgent da = createTestDriverAgent();

        PickupOffer po = da.createPickupOffer(WAYPOINT_START, WAYPOINT_END, CLIENT_EMAIL);
        Intent in = da.realizePickup(DRIVER_EMAIL, CLIENT_EMAIL);
        da.startPickup(DRIVER_EMAIL, CLIENT_EMAIL);
        da.endPickup(DRIVER_EMAIL, CLIENT_EMAIL);

        // If above code throws, it won't enter the assert
        assertTrue(true);
    }
}