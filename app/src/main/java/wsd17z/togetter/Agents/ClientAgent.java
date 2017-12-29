package wsd17z.togetter.Agents;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IComponentStep;
import jadex.bridge.IExternalAccess;
import jadex.bridge.InternalAccessAdapter;
import jadex.bridge.TimeoutIntermediateResultListener;
import jadex.bridge.modelinfo.ComponentInstanceInfo;
import jadex.bridge.modelinfo.IModelInfo;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.types.monitoring.IMonitoringEvent;
import jadex.bridge.service.types.monitoring.IMonitoringService;
import jadex.commons.IFilter;
import jadex.commons.Tuple2;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.IIntermediateFuture;
import jadex.commons.future.IResultListener;
import jadex.commons.future.ISubscriptionIntermediateFuture;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.kernelbase.ExternalAccess;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.Binding;
import jadex.micro.annotation.Implementation;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;
import wsd17z.togetter.Driver.IRideService;
import wsd17z.togetter.Driver.IUserService;
import wsd17z.togetter.Driver.IPickupService;
import wsd17z.togetter.Driver.PickupOffer;
import wsd17z.togetter.Driver.RideService;
import wsd17z.togetter.Driver.UserService;

/**
 * Created by Kosmos on 05/12/2017.
 */

@Agent
@ProvidedServices({
        @ProvidedService(name="rider", type=IRideService.class, implementation = @Implementation(RideService.class)),
        @ProvidedService(name="user", type=IUserService.class, implementation = @Implementation(UserService.class))//, scope = RequiredServiceInfo.SCOPE_APPLICATION)
})
@RequiredServices({
        @RequiredService(name="pickup", type=IPickupService.class, multiple=true, binding=@Binding(dynamic=true, scope=RequiredServiceInfo.SCOPE_GLOBAL))
})

public class ClientAgent extends UserAgent implements IRideService, IUserService
{
    private PickupOffer mChosenOffer;

    @AgentFeature
    private IRequiredServicesFeature requiredServicesFeature;
    private List<IPickupService> pickupServices = new ArrayList<>();

    public IFuture<Void> getDrivers() {
        final Future<Void> pickupServicesSearchDone = new Future<>();
        pickupServices.clear();

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
        mSuperAgentClass = this.getClass();
    }

    @Override
    public IFuture<Void> refreshDrivers() {
        return getDrivers();
    }

    @Override
    public List<PickupOffer> queryForPickupOffers() {
        if (mEndPoints == null || mUserEmail == null) {
            return null;
        }

        List<PickupOffer> offers = new ArrayList<>();
        for (IPickupService srv : pickupServices) {
            double s1 = mEndPoints.getFirstEntity().latitude;
            double s2 = mEndPoints.getFirstEntity().longitude;
            double e1 = mEndPoints.getSecondEntity().latitude;
            double e2 = mEndPoints.getSecondEntity().longitude;
            offers.add(srv.createPickupOffer(s1,s2,e1,e2, mUserEmail));
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