package wsd17z.togetter.Driver;

import java.util.List;

import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.annotation.ServiceComponent;
import wsd17z.togetter.Agents.ClientAgent;

/**
 * Created by Kosmos on 07/12/2017.
 */

@Service
public class RideService implements IRideService {
    @ServiceComponent
    private ClientAgent agent;

    @Override
    public List<PickupOffer> queryForPickupOffers() {
        return agent.queryForPickupOffers();
    }

    @Override
    public void chooseOffer(PickupOffer offer) {
        agent.chooseOffer(offer);
    }

    @Override
    public void startRide() {
        agent.startRide();
    }

    @Override
    public void endRide() {
        agent.endRide();
    }
}