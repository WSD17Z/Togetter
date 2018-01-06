package wsd17z.togetter;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import wsd17z.togetter.Agents.ClientManagementAgent;
import wsd17z.togetter.Agents.DbManagementAgent;
import wsd17z.togetter.DbManagement.DbOfferObject;
import wsd17z.togetter.DbManagement.DbUserObject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static wsd17z.togetter.TestUtils.CLIENT_EMAIL;
import static wsd17z.togetter.TestUtils.DRIVER_EMAIL;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ClientManagementAgentTest {
    @Test
    public void ctorTest() throws Exception {
        ClientManagementAgent ca = new ClientManagementAgent();
        assertNotNull(ca);
    }

    @Test
    public void reportErrorTest() throws Exception {
        ClientManagementAgent ca = new ClientManagementAgent();
        ca.reportError("testString");

        // If above code throws, it won't enter the assert
        assertTrue(true);
    }
}