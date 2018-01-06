package wsd17z.togetter;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import wsd17z.togetter.Agents.DbManagementAgent;
import wsd17z.togetter.Agents.WalletAgent;
import wsd17z.togetter.DbManagement.DbUserObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static wsd17z.togetter.TestUtils.CLIENT_EMAIL;
import static wsd17z.togetter.TestUtils.DRIVER_EMAIL;
import static wsd17z.togetter.TestUtils.createTestWalletAgent;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class WalletAgentTest {
    @Test
    public void ctorTest() throws Exception {
        WalletAgent ua = createTestWalletAgent();
        assertNotNull(ua);
    }

    @Test
    public void getBalanceTest() throws Exception {
        WalletAgent ua = createTestWalletAgent();
        assertEquals(0d, ua.getBalance(CLIENT_EMAIL + CLIENT_EMAIL), 0.01d);

        DbManagementAgent ca = new DbManagementAgent();
        DbUserObject usr = ca.getUser(CLIENT_EMAIL);
        assertEquals(usr.getBalance(), ua.getBalance(CLIENT_EMAIL), 0.01d);
    }

    @Test
    public void checkFundsTest() throws Exception {
        WalletAgent ua = createTestWalletAgent();
        assertFalse(ua.checkFunds(CLIENT_EMAIL + CLIENT_EMAIL, 10));

        DbManagementAgent ca = new DbManagementAgent();
        DbUserObject usr = ca.getUser(CLIENT_EMAIL);
        assertFalse(ua.checkFunds(CLIENT_EMAIL, usr.getBalance() + 1));
        assertTrue(ua.checkFunds(CLIENT_EMAIL, usr.getBalance()));
    }

    @Test
    public void transferFundsTest() throws Exception {
        WalletAgent ua = createTestWalletAgent();

        DbManagementAgent ca = new DbManagementAgent();
        DbUserObject usr = ca.getUser(CLIENT_EMAIL);
        usr.setBalance(10d);
        ca.updateUser(usr);

        usr = ca.getUser(DRIVER_EMAIL);
        usr.setBalance(10d);
        ca.updateUser(usr);

        ua.transferFunds(CLIENT_EMAIL, DRIVER_EMAIL, 10d);
        assertEquals(0d, ua.getBalance(CLIENT_EMAIL), 0.01d);
        assertEquals(20d, ua.getBalance(DRIVER_EMAIL), 0.01d);
    }
}
