package wsd17z.togetter.MapsModules;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Kosmos on 27/12/2017.
 */

public class GMapsLauncher {
    private static final String GMAPS_URL_API = "https://www.google.com/maps/dir/?api=1";

    private static String buildUrl(String origin, String destination) {
        try {
            return GMAPS_URL_API
                    //+ "&origin=" + URLEncoder.encode(origin, "utf-8")
                    + "&destination=" + URLEncoder.encode(destination, "utf-8")
                    + "&travelmode=driving"
                    + "&dir_action=navigate";
        } catch (UnsupportedEncodingException ex) {
            return "";
        }
    }

    private static String buildUrl(String origin, String destination, List<String> waypoints) {
        try {
            String urlWaypoints = "";
            for (String wp : waypoints) {
                urlWaypoints += wp + "|";
            }
            if (urlWaypoints.endsWith("|")) {
                urlWaypoints = urlWaypoints.substring(0, urlWaypoints.length() - 1);
            }
            urlWaypoints = (waypoints.isEmpty() ? "" : "&waypoints=" + URLEncoder.encode(urlWaypoints, "utf-8"));
            String url = GMAPS_URL_API
                    //+ "&origin=" + URLEncoder.encode(origin, "utf-8")
                    + "&destination=" + URLEncoder.encode(destination, "utf-8")
                    + urlWaypoints
                    + "&travelmode=driving"
                    + "&dir_action=navigate";
            return url;
        } catch (UnsupportedEncodingException ex) {
            Log.e("ERR", ex.getMessage());
            return "";
        }
    }

    public static Intent getNavigationIntent(String origin, String destination) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(buildUrl(origin, destination)));
    }

    public static Intent getNavigationIntent(String origin, String destination, List<String> waypoints) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(buildUrl(origin, destination, waypoints)));
    }
}
