package wsd17z.togetter.Agents;

import android.util.ArraySet;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.commons.Tuple2;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.IIntermediateFuture;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.Binding;
import jadex.micro.annotation.Implementation;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;
import wsd17z.togetter.DbManagement.DbOfferObject;
import wsd17z.togetter.DbManagement.IDbManagementService;
import wsd17z.togetter.Driver.IRideService;
import wsd17z.togetter.Driver.IUserService;
import wsd17z.togetter.Driver.IPickupService;
import wsd17z.togetter.Driver.PickupOffer;
import wsd17z.togetter.Driver.RideService;
import wsd17z.togetter.Driver.UserService;
import wsd17z.togetter.Wallet.IWalletService;

/**
 * Created by Kosmos on 05/12/2017.
 */

@Agent
@ProvidedServices({
        @ProvidedService(name="user", type=IRideService.class, implementation = @Implementation(RideService.class)),
        @ProvidedService(name="user", type=IUserService.class, implementation = @Implementation(UserService.class), scope = RequiredServiceInfo.SCOPE_COMPONENT)
})
@RequiredServices({
        @RequiredService(name="pickup", type=IPickupService.class, multiple=true, binding=@Binding(dynamic=true, scope=RequiredServiceInfo.SCOPE_GLOBAL))
})

public class ClientAgent implements IRideService, IUserService
{
    private Tuple2<LatLng, LatLng> mEndPoints;
    private PickupOffer mChosenOffer;
    private String mUserEmail;

    @AgentFeature
    private IRequiredServicesFeature requiredServicesFeature;
    private List<IPickupService> pickupServices = new ArrayList<>();

    public IFuture<Void> getDrivers() {
        final Future<Void> pickupServicesSearchDone = new Future<>();
        IIntermediateFuture<IPickupService> drivers = requiredServicesFeature.getRequiredServices("pickup");
        drivers.addIntermediateResultListener(new IntermediateDefaultResultListener<IPickupService>() {
            @Override
            public void intermediateResultAvailable(IPickupService result) {
                pickupServices.add(result);
            }
            @Override
            public void finished() {
                pickupServicesSearchDone.setResult(null);
            }
        });
        return pickupServicesSearchDone;
    }

    @AgentBody
    public void executeBody() {
        getDrivers();
    }

    public ClientAgent() {
    }

    @Override
    public void setEndPoints(LatLng start, LatLng end) {
        mEndPoints = new Tuple2<>(start, end);
    }

    @Override
    public void setEmail(String email) {
        if (mUserEmail == null) {
            mUserEmail = email;
        } else {
            Log.d("SECURITY WARNING", "Trying to reset users login!");
        }
    }

    @Override
    public List<PickupOffer> queryForPickupOffers() {
        if (mEndPoints == null || mUserEmail == null) {
            return null;
        }

        List<PickupOffer> offers = new ArrayList<>();
        for (IPickupService srv : pickupServices) {
            offers.add(srv.createPickupOffer(mEndPoints.getFirstEntity(), mEndPoints.getSecondEntity(), mUserEmail));
        }

        return offers;
    }

    @Override
    public void chooseOffer(PickupOffer offer) {
        mChosenOffer = offer;
        for (IPickupService srv : pickupServices) {
            srv.realizePickup(offer.getDriverEmail(), mUserEmail);
        }
    }

    @Override
    public void startRide() {
        for (IPickupService srv : pickupServices) {
            srv.startPickup(mChosenOffer.getDriverEmail(), mUserEmail);
        }
    }

    @Override
    public void endRide() {
        for (IPickupService srv : pickupServices) {
            srv.endPickup(mChosenOffer.getDriverEmail(), mUserEmail);
        }
        mChosenOffer = null;
    }
}