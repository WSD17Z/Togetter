package wsd17z.togetter.Driver;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Kosmos on 05/12/2017.
 */

public interface IUserService
{
    void setEndPoints(LatLng start, LatLng end);
    void setEndPoints(double start1, double start2, double end1, double end2);
    void setEmail(String email);
    void setCost(float costPerKm);
    void setDelay(int delayMin);
}