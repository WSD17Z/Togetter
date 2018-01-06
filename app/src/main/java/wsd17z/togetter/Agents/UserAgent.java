package wsd17z.togetter.Agents;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import jadex.commons.Tuple2;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.Implementation;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import wsd17z.togetter.Driver.IUserService;
import wsd17z.togetter.Driver.UserService;

/**
 * Created by Kosmos on 28/12/2017.
 */

@Agent
@ProvidedServices({
        @ProvidedService(name="user", type=IUserService.class, implementation = @Implementation(UserService.class))//, scope = RequiredServiceInfo.SCOPE_APPLICATION)
})
public class UserAgent implements IUserService {
    protected Tuple2<LatLng, LatLng> mEndPoints;
    protected float mPricePerKm;
    protected int mMaxDelayMin;
    protected String mUserEmail;
    protected Class mSuperAgentClass;

    public UserAgent() {
        Log.d("MKK", "user ctor");
    }

    @Override
    public void setEndPoints(LatLng start, LatLng end) {
        mEndPoints = new Tuple2<>(start, end);
    }

    @Override
    public void setEndPoints(double start1, double start2, double end1, double end2) {
        mEndPoints = new Tuple2<>(new LatLng(start1, start2), new LatLng(end1, end2));
    }

    @Override
    public void setCost(float costPerKm) {
        mPricePerKm = costPerKm;
    }

    @Override
    public void setDelay(int delayMin) {
        mMaxDelayMin = delayMin;
    }

    @Override
    public void setEmail(String email) {
        if (mUserEmail == null) {
            mUserEmail = email;
        } else {
            //Log.w("SECURITY WARNING", "Trying to reset users login!");
        }
    }
}
