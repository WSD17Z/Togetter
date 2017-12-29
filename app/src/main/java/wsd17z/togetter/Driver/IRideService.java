package wsd17z.togetter.Driver;

import java.util.List;

import jadex.commons.future.IFuture;

/**
 * Created by Kosmos on 05/12/2017.
 */

public interface IRideService
{
    List<PickupOffer> queryForPickupOffers();
    void chooseOffer(PickupOffer offer);
    void startRide();
    IFuture<Void> refreshDrivers();
    void endRide();
}