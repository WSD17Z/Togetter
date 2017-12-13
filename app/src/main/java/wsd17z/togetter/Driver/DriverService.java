package wsd17z.togetter.Driver;

import com.google.android.gms.maps.model.LatLng;

import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.annotation.ServiceComponent;
import wsd17z.togetter.Agents.DriverAgent;

/**
 * Created by Kosmos on 07/12/2017.
 */

@Service
public class DriverService implements IDriverService {
    @ServiceComponent
    private DriverAgent agent;

    @Override
    public void setEndPoints(LatLng start, LatLng end) {
        agent.setEndPoints(start, end);
    }

    @Override
    public void setEmail(String email) {
        agent.setEmail(email);
    }
}
