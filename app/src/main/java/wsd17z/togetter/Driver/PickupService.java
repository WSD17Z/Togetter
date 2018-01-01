package wsd17z.togetter.Driver;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.annotation.ServiceComponent;
import wsd17z.togetter.Agents.DriverAgent;

/**
 * Created by Kosmos on 07/12/2017.
 */

@Service
public class PickupService implements IPickupService {
    @ServiceComponent
    private DriverAgent agent;

    @Override
    public PickupOffer createPickupOffer(LatLng start, LatLng end, String email) {
        return agent.createPickupOffer(start, end, email);
    }

    @Override
    public PickupOffer createPickupOffer(double s1,double s2,double e1,double e2,String email) {
        return agent.createPickupOffer(s1,s2,e1,e2, email);
    }

    @Override
    public Intent realizePickup(String driverEmail, String email) {
        return agent.realizePickup(driverEmail, email);
    }

    @Override
    public void startPickup(String driverEmail, String email) {
        agent.startPickup(driverEmail, email);
    }

    @Override
    public void endPickup(String driverEmail, String email) {
        agent.endPickup(driverEmail, email);
    }
}