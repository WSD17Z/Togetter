package wsd17z.togetter.Agents;

import android.util.ArraySet;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.commons.Tuple2;
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
import wsd17z.togetter.Driver.UserService;
import wsd17z.togetter.Driver.IUserService;
import wsd17z.togetter.Driver.IPickupService;
import wsd17z.togetter.Driver.PickupService;
import wsd17z.togetter.Driver.PickupOffer;
import wsd17z.togetter.Wallet.IWalletService;

/**
 * Created by Kosmos on 05/12/2017.
 */

@Agent
@ProvidedServices({
        @ProvidedService(name="pickup", type=IPickupService.class, implementation = @Implementation(PickupService.class)),
        @ProvidedService(name="user", type=IUserService.class, implementation = @Implementation(UserService.class), scope = RequiredServiceInfo.SCOPE_COMPONENT)
})
@RequiredServices({
        @RequiredService(name="wallet", type=IWalletService.class, binding=@Binding(scope= RequiredServiceInfo.SCOPE_GLOBAL)),
        @RequiredService(name="dbmanager", type=IDbManagementService.class, binding=@Binding(scope= RequiredServiceInfo.SCOPE_GLOBAL))
})

public class DriverAgent implements IPickupService, IUserService
{
    private Tuple2<LatLng, LatLng> mEndPoints;
    private Set<LatLng> mWaypoints;
    private float mPricePerKm;
    private int mMaxDelayMin;
    private static final double DISTANCE_THRESHOLD = 10d;
    private Map<String, PickupOffer> mClients;
    private String mUserEmail;
    private String mUserInstanceID;

    @AgentFeature
    private IRequiredServicesFeature requiredServicesFeature;
    private IWalletService walletService;
    private IDbManagementService dbManagementService;

    @AgentBody
    public void executeBody() {
        walletService = (IWalletService) requiredServicesFeature.getRequiredService("wallet").get();
        dbManagementService = (IDbManagementService) requiredServicesFeature.getRequiredService("dbmanager").get();
    }

    public DriverAgent() {
        mPricePerKm = 0;
        mMaxDelayMin = 0;
        mWaypoints = new ArraySet<>();
        mClients = new HashMap<>();
        mUserInstanceID = FirebaseInstanceId.getInstance().getToken();
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

    /*
        Creates pickup offer to be sent to client. It contains all the important info.
        Also users login along with offer are added to the drivers map.
     */
    @Override
    public PickupOffer createPickupOffer(LatLng start, LatLng end, String email) {
        if (getDistance(start, end) > DISTANCE_THRESHOLD) {
            return null;
        }
        //TODO: get from gmap distance & eta's
        int pickupDelay = 123;
        if (pickupDelay > mMaxDelayMin) {
            return null;
        }
        double pickupDist = 5d;
        double cost = pickupDist * mPricePerKm;

        PickupOffer offer = new PickupOffer(start, end, 0, 0, cost, email, mUserEmail, mUserInstanceID);
        mClients.put(email, offer);
        return offer;
    }

    private double getDistance(LatLng start, LatLng end) {
        return Math.sqrt(Math.pow(start.latitude - end.latitude, 2.0) + Math.pow(start.longitude - end.longitude, 2.0));
    }

    /*
        If pickup offer for given email has been created, it can be realized.
        It will be added to the database, to store the offer.
        Id of the element in db will be added to offer in the map.
     */
    @Override
    public void realizePickup(String driverEmail, String email) {
        if (driverEmail != mUserEmail) {
            return;
        }
        if (mClients.containsKey(email)) {
            PickupOffer offer = mClients.get(email);
            DbOfferObject dbOffer = new DbOfferObject(offer, email);
            long id = dbManagementService.addPickupOffer(dbOffer);

            if (id >= 0) {
                offer.setId(id);
                mClients.put(email, offer);

                mWaypoints.add(offer.getEndPoints().getFirstEntity());
                mWaypoints.add(offer.getEndPoints().getSecondEntity());
            }
        }
        //TODO: relay start, end & waypoints to the navigation

    }

    /*
        Updates offer in db to indicate, that it started.
     */
    @Override
    public void startPickup(String driverEmail, String email) {
        if (driverEmail != mUserEmail) {
            return;
        }
        if (mClients.containsKey(email)) {
            PickupOffer offer = mClients.get(email);
            DbOfferObject dbOffer = dbManagementService.getPickupOffer(offer.getId());
            dbOffer.setStarted(true);
            dbManagementService.updatePickupOffer(offer.getId(), dbOffer);
            dbManagementService.deleteUnstartedPickups(email);
        }

        //TODO: go back to navigation
    }

    /*
        Updates offer in db to indicate, that it ended.
        Also calls WalletService to transfer the funds for the drive - if it succeeds, info is stored
        in the database. Client along with his offer is removed from the drivers map.
     */
    @Override
    public void endPickup(String driverEmail, String email) {
        if (driverEmail != mUserEmail) {
            return;
        }
        if (mClients.containsKey(email)) {
            PickupOffer offer = mClients.get(email);
            DbOfferObject dbOffer = dbManagementService.getPickupOffer(offer.getId());
            dbOffer.setEnded(true);

            if (walletService.transferFunds(dbOffer.getClientEmail(), dbOffer.getDriverEmail(), dbOffer.getPrice())) {
                dbOffer.setPaid(true);
            }
            dbManagementService.updatePickupOffer(offer.getId(), dbOffer);
            mClients.remove(email);
            mWaypoints.remove(offer.getEndPoints().getFirstEntity());
            mWaypoints.remove(offer.getEndPoints().getSecondEntity());
        }

        //TODO: go back to navigation
    }
}
