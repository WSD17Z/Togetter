package wsd17z.togetter.MapsModules;

import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import wsd17z.togetter.Utils;

/**
 * Created by Kosmos on 27/12/2017.
 */

public class GMapsLauncher {
    private static final String GMAPS_URL_API = "https://www.google.com/maps/dir/?api=1";

    public static String buildUrl(String origin, String destination) {
        try {
            return GMAPS_URL_API
                    + "&origin=" + URLEncoder.encode(origin, "utf-8")
                    + "&destination=" + URLEncoder.encode(destination, "utf-8")
                    + "&travelmode=driving"
                    + "&dir_action=navigate";
        } catch (UnsupportedEncodingException ex) {
            return "";
        }
    }

    public static Intent getNavigationIntent(String origin, String destination) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(buildUrl(origin, destination)));
    }
}
