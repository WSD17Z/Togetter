package wsd17z.togetter.Driver;

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
    public void realizePickup(String driverEmail, String email) {
        agent.realizePickup(driverEmail, email);
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