package wsd17z.togetter;

import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import wsd17z.togetter.Agents.ClientAgent;
import wsd17z.togetter.Agents.DriverAgent;
import wsd17z.togetter.Agents.UserAgent;
import wsd17z.togetter.Driver.IPickupService;
import wsd17z.togetter.Driver.PickupOffer;
import wsd17z.togetter.Driver.PickupService;

import static org.junit.Assert.assertEquals;
import static wsd17z.togetter.TestUtils.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ClientAgentTest {
    @Test
    public void ctorTest() throws Exception {
        ClientAgent ca = new ClientAgent();
        assertNotNull(ca);
    }

    @Test
    public void ctorWithServicesTest() throws Exception {
        ClientAgent ca = createTestClientAgent();
        assertNotNull(ca);
    }

    @Test
    public void queryForPickupOffersTest() throws Exception {
        ClientAgent ca = createTestClientAgent();

        List<PickupOffer> offers = ca.queryForPickupOffers();
        assertNotNull(offers);
        assertEquals(1, offers.size());
        PickupOffer off = offers.get(0);
        assertEquals(CLIENT_EMAIL, off.getEmail());
        assertEquals(DRIVER_EMAIL, off.getDriverEmail());
    }

    @Test
    public void chooseOfferTest() throws Exception {
        ClientAgent ca = createTestClientAgent();
        List<PickupOffer> offers = ca.queryForPickupOffers();
        PickupOffer off = offers.get(0);
        ca.chooseOffer(off);

        // If above code throws, it won't enter the assert
        assertTrue(true);
    }

    @Test
    public void startRideTest() throws Exception {
        ClientAgent ca = createTestClientAgent();
        List<PickupOffer> offers = ca.queryForPickupOffers();
        PickupOffer off = offers.get(0);
        ca.chooseOffer(off);
        ca.startRide();

        // If above code throws, it won't enter the assert
        assertTrue(true);
    }

    @Test
    public void endRideTest() throws Exception {
        ClientAgent ca = createTestClientAgent();
        List<PickupOffer> offers = ca.queryForPickupOffers();
        PickupOffer off = offers.get(0);
        ca.chooseOffer(off);
        ca.startRide();
        ca.endRide();

        // If above code throws, it won't enter the assert
        assertTrue(true);
    }
}