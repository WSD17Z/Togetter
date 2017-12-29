package wsd17z.togetter;

import android.content.Context;
import android.content.pm.PackageManager;

import com.google.android.gms.maps.model.LatLng;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

/**
 * Created by Kosmos on 27/12/2017.
 */

public class Utils {

    public static String latLngToString(LatLng location) {
        return location.latitude + "," + location.longitude;
    }

    public static boolean canAccessLocation(Context ctx) {
        return hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, ctx);
    }

    public static boolean hasPermission(String perm, Context ctx) {
        return PackageManager.PERMISSION_GRANTED == checkSelfPermission(ctx,perm);
    }
}
