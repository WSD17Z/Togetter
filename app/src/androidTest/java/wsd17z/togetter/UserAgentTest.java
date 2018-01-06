package wsd17z.togetter;

import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.runner.RunWith;

import wsd17z.togetter.Agents.UserAgent;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UserAgentTest {
    @Test
    public void ctorTest() throws Exception {
        UserAgent ua = new UserAgent();
        assertNotNull(ua);
    }

    @Test
    public void setEndpointsTest() throws Exception {
        UserAgent ua = new UserAgent();
        ua.setEndPoints(new LatLng(0, 0), new LatLng(0, 0));
        ua.setEndPoints(null, null);

        // If above code throws, it won't enter the assert
        assertTrue(true);
    }

    @Test
    public void setEndpointsDoubleTest() throws Exception {
        UserAgent ua = new UserAgent();
        ua.setEndPoints(0d,0d,0d,0d);

        // If above code throws, it won't enter the assert
        assertTrue(true);
    }

    @Test
    public void setCostTest() throws Exception {
        UserAgent ua = new UserAgent();
        ua.setCost(0f);

        // If above code throws, it won't enter the assert
        assertTrue(true);
    }

    @Test
    public void setDelayTest() throws Exception {
        UserAgent ua = new UserAgent();
        ua.setDelay(0);

        // If above code throws, it won't enter the assert
        assertTrue(true);
    }

    @Test
    public void setEmailTest() throws Exception {
        UserAgent ua = new UserAgent();
        ua.setEmail("testString");

        // If above code throws, it won't enter the assert
        assertTrue(true);
    }

    @Test
    public void reSetEmailTest() throws Exception {
        UserAgent ua = new UserAgent();
        ua.setEmail("testString");
        ua.setEmail("testString2");

        // If above code throws, it won't enter the assert
        assertTrue(true);
    }
}
