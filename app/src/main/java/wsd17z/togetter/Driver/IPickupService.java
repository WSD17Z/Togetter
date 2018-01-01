package wsd17z.togetter.Driver;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kosmos on 05/12/2017.
 */

public interface IPickupService
{
    PickupOffer createPickupOffer(LatLng start, LatLng end, String email);
    PickupOffer createPickupOffer(double s1,double s2,double e1,double e2,String email);
    Intent realizePickup(String driverEmail, String email);
    void startPickup(String driverEmail, String email);
    void endPickup(String driverEmail, String email);
}