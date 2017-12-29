package wsd17z.togetter.Driver;

import com.google.android.gms.maps.model.LatLng;

import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.annotation.ServiceComponent;
import wsd17z.togetter.Agents.DriverAgent;
import wsd17z.togetter.Agents.UserAgent;

/**
 * Created by Kosmos on 07/12/2017.
 */

@Service
public class UserService implements IUserService {
    @ServiceComponent
    private UserAgent agent;

    @Override
    public void setEndPoints(LatLng start, LatLng end) {
        agent.setEndPoints(start, end);
    }
    @Override
    public void setEndPoints(double start1, double start2, double end1, double end2) {
        agent.setEndPoints(start1, start2, end1, end2);
    }

    @Override
    public void setEmail(String email) {
        agent.setEmail(email);
    }

    @Override
    public void setCost(float costPerKm) {
        agent.setCost(costPerKm);
    }

    @Override
    public void setDelay(int delayMin) {
        agent.setDelay(delayMin);
    }
}
