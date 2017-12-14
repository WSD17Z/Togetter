package wsd17z.togetter.Driver;

import java.util.List;

/**
 * Created by Kosmos on 05/12/2017.
 */

public interface IRideService
{
    List<PickupOffer> queryForPickupOffers();
    void chooseOffer(PickupOffer offer);
    void startRide();
    void endRide();
}