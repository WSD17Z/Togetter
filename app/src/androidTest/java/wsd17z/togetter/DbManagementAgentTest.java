package wsd17z.togetter;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import wsd17z.togetter.Agents.ClientAgent;
import wsd17z.togetter.Agents.DbManagementAgent;
import wsd17z.togetter.DbManagement.DbOfferObject;
import wsd17z.togetter.DbManagement.DbUserObject;
import wsd17z.togetter.Driver.PickupOffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static wsd17z.togetter.TestUtils.CLIENT_EMAIL;
import static wsd17z.togetter.TestUtils.DRIVER_EMAIL;
import static wsd17z.togetter.TestUtils.createTestClientAgent;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DbManagementAgentTest {
    @Test
    public void ctorTest() throws Exception {
        DbManagementAgent.initDb(InstrumentationRegistry.getTargetContext());
        DbManagementAgent ca = new DbManagementAgent();
        assertNotNull(ca);
    }

    @Test
    public void getUserTest() throws Exception {
        DbManagementAgent.initDb(InstrumentationRegistry.getTargetContext());
        DbManagementAgent ca = new DbManagementAgent();
        DbUserObject usr = ca.getUser(CLIENT_EMAIL);
        assertNotNull(usr);
    }

    @Test
    public void addUserTest() throws Exception {
        DbManagementAgent.initDb(InstrumentationRegistry.getTargetContext());
        DbManagementAgent ca = new DbManagementAgent();
        DbUserObject usr = ca.getUser(CLIENT_EMAIL);
        usr.setEmail(CLIENT_EMAIL + CLIENT_EMAIL);

        assertNull(ca.getUser(CLIENT_EMAIL + CLIENT_EMAIL));
        ca.addUser(usr);
        assertNotNull(ca.getUser(CLIENT_EMAIL + CLIENT_EMAIL));
    }

    @Test
    public void deleteUserTest() throws Exception {
        DbManagementAgent.initDb(InstrumentationRegistry.getTargetContext());
        DbManagementAgent ca = new DbManagementAgent();

        DbUserObject usr = ca.getUser(CLIENT_EMAIL + CLIENT_EMAIL);
        assertNotNull(usr);

        ca.deleteUser(usr);
        assertNull(ca.getUser(CLIENT_EMAIL + CLIENT_EMAIL));
    }

    @Test
    public void updateUserTest() throws Exception {
        DbManagementAgent.initDb(InstrumentationRegistry.getTargetContext());
        DbManagementAgent ca = new DbManagementAgent();

        DbUserObject usr = ca.getUser(CLIENT_EMAIL);
        usr.setBalance(usr.getBalance() + 1);
        ca.updateUser(usr);
    }

    @Test
    public void addPickupOfferTest() throws Exception {
        DbManagementAgent.initDb(InstrumentationRegistry.getTargetContext());
        DbManagementAgent ca = new DbManagementAgent();
        long id = 0;
        while (true) {
            DbOfferObject off = ca.getPickupOffer(id);
            if (off == null) {
                break;
            }
            id++;
        }

        assertNull(ca.getPickupOffer(id));
        //double price, boolean started, boolean ended, boolean paid, String clientEmail, String driverEmail
        DbOfferObject off = new DbOfferObject(10, false, false, false, CLIENT_EMAIL, DRIVER_EMAIL);
        long id2 = ca.addPickupOffer(off);
        assertTrue (id2 > id);
        assertNotNull (ca.getPickupOffer(id2));
    }

    @Test
    public void getPickupOfferTest() throws Exception {
        DbManagementAgent.initDb(InstrumentationRegistry.getTargetContext());
        DbManagementAgent ca = new DbManagementAgent();

        long id = 0;
        int timeout = 250;
        while (timeout > 0) {
            DbOfferObject off = ca.getPickupOffer(id);
            if (off != null) {
                break;
            }
            id++;
            timeout--;
        }

        if (timeout <= 0) {
            assertTrue(false);
        }
    }

    @Test
    public void updatePickupOfferTest() throws Exception {
        DbManagementAgent.initDb(InstrumentationRegistry.getTargetContext());
        DbManagementAgent ca = new DbManagementAgent();

        long id = 0;
        int timeout = 250;
        DbOfferObject off = null;
        while (timeout > 0) {
            off = ca.getPickupOffer(id);
            if (off != null) {
                break;
            }
            id++;
            timeout--;
        }

        if (timeout <= 0) {
            assertTrue(false);
        }

        off.setStarted(!off.getStarted());
        ca.updatePickupOffer(id, off);
    }

    @Test
    public void deleteUnstartedPickupsTest() throws Exception {
        DbManagementAgent.initDb(InstrumentationRegistry.getTargetContext());
        DbManagementAgent ca = new DbManagementAgent();

        DbOfferObject off = new DbOfferObject(10, false, false, false, CLIENT_EMAIL, DRIVER_EMAIL);
        long id = ca.addPickupOffer(off);

        ca.deleteUnstartedPickups(CLIENT_EMAIL);
        assertNull(ca.getPickupOffer(id));
    }
}