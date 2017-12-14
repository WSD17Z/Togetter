package wsd17z.togetter.Driver;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kosmos on 05/12/2017.
 */

public interface IUserService
{
    void setEndPoints(LatLng start, LatLng end);
    void setEmail(String email);
}