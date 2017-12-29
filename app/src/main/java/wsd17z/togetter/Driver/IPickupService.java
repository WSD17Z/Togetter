package wsd17z.togetter.Driver;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kosmos on 05/12/2017.
 */

public interface IPickupService
{
    PickupOffer createPickupOffer(LatLng start, LatLng end, String email);
    PickupOffer createPickupOffer(double s1,double s2,double e1,double e2,String email);
    void realizePickup(String driverEmail, String email);
    void startPickup(String driverEmail, String email);
    void endPickup(String driverEmail, String email);
}