package wsd17z.togetter.Agents;

import android.content.Intent;
import android.util.ArraySet;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import wsd17z.togetter.Driver.IPickupService;
import wsd17z.togetter.Driver.IUserService;
import wsd17z.togetter.Driver.PickupOffer;
import wsd17z.togetter.Driver.PickupService;
import wsd17z.togetter.Driver.UserService;
import wsd17z.togetter.MapsModules.DirectionFinder;
import wsd17z.togetter.MapsModules.GMapsLauncher;
import wsd17z.togetter.MapsModules.Route;
import wsd17z.togetter.Utils;
import wsd17z.togetter.Wallet.IWalletService;

/**
 * Created by Kosmos on 05/12/2017.
 */

@Agent
@ProvidedServices({
        @ProvidedService(name="pickup", type=IPickupService.class, implementation = @Implementation(PickupService.class)),
        @ProvidedService(name="user", type=IUserService.class, implementation = @Implementation(UserService.class), scope = RequiredServiceInfo.SCOPE_GLOBAL)
})
@RequiredServices({
        @RequiredService(name="wallet", type=IWalletService.class, binding=@Binding(scope= RequiredServiceInfo.SCOPE_GLOBAL)),
        @RequiredService(name="dbmanager", type=IDbManagementService.class, binding=@Binding(scope= RequiredServiceInfo.SCOPE_GLOBAL))
})

public class DriverAgent extends UserAgent implements IPickupService, IUserService
{
    private Set<LatLng> mWaypoints = new ArraySet<>();
    private static final double DISTANCE_THRESHOLD = 10d;
    private static final double SAME_DIST_THRESHOLD = 1e-4;
    private Map<String, PickupOffer> mClients = new HashMap<>();
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
        mSuperAgentClass = this.getClass();
        Random rnd = new Random();
        mPricePerKm = rnd.nextInt(1000) / 100f;
        mMaxDelayMin = 30;
        mEndPoints = new Tuple2<>(
                new LatLng(getRandomLat(rnd), getRandomLng(rnd)),
                new LatLng(getRandomLat(rnd), getRandomLng(rnd))
        );
        mUserInstanceID = FirebaseInstanceId.getInstance().getToken();
        mUserEmail = "randomDriver" + String.valueOf(rnd.nextInt(1000)) + "@rnd" + String.valueOf(rnd.nextInt(1000)) + ".com";
    }

    public DriverAgent(float price, int delay, String email, IWalletService wallet, IDbManagementService db) {
        mSuperAgentClass = this.getClass();
        mPricePerKm = price;
        mMaxDelayMin = delay;
        mUserInstanceID = FirebaseInstanceId.getInstance().getToken();
        mUserEmail = email;
        walletService = wallet;
        dbManagementService = db;
    }

    private double getRandomLat(Random rnd) {
        return (52128266 + rnd.nextInt(46947)) / 1000000d;
    }

    private double getRandomLng(Random rnd) {
        return (21017841 + rnd.nextInt(61138)) / 1000000d;
    }

    /*
        Creates pickup offer to be sent to client. It contains all the important info.
        Also users login along with offer are added to the drivers map.
     */
    @Override
    public PickupOffer createPickupOffer(double s1,double s2,double e1,double e2,String email) {
        return createPickupOffer(new LatLng(s1, s2), new LatLng(e1, e2), email);
    }

    @Override
    public PickupOffer createPickupOffer(LatLng start, LatLng end, String email) {
        if (getDistance(start, end) > DISTANCE_THRESHOLD) {
            return null;
        }

        // build waypoints list
        List<String> wps = new ArrayList<String>();
        for (LatLng loc : mWaypoints) {
            wps.add(Utils.latLngToString(loc));
        }

        // get old route info
        DirectionFinder directionFinderOld = new DirectionFinder(null, Utils.latLngToString(mEndPoints.getFirstEntity()),
                Utils.latLngToString(mEndPoints.getSecondEntity()), wps);
        Route routeOld = directionFinderOld.executeOnThread();

        // add offers points and get new route info
        wps.add(Utils.latLngToString(start));
        wps.add(Utils.latLngToString(end));
        DirectionFinder directionFinderNew = new DirectionFinder(null, Utils.latLngToString(mEndPoints.getFirstEntity()),
                Utils.latLngToString(mEndPoints.getSecondEntity()), wps);
        Route routeNew = directionFinderNew.executeOnThread();

        if (routeOld.endLocation == null || routeNew.endLocation == null) {
            return null;
        }

        // if routes were found, extract informations
        int pickupDelay = 0;
        double pickupDistance = 0d;
        if (routeOld != null && routeNew != null) {
            pickupDelay = (routeNew.duration.value - routeOld.duration.value) / 60;
            pickupDistance = (routeNew.distance.value - routeOld.distance.value) / 1000f;
        }

        if (pickupDelay < 0 || pickupDistance < 0) {
            return null;
        }

        // check delay
        if (pickupDelay > mMaxDelayMin) {
            return null;
        }

        // calculate cost
        double cost = pickupDistance * mPricePerKm;

        int startEta = 0;
        boolean startFound = false;
        int endEta = 0;
        if (routeNew != null && routeNew.legs != null) {
            for (Route subRoute : routeNew.legs) {
                if (!startFound) {
                    startEta += subRoute.duration.value;
                }
                if (!startFound && sameLocation(subRoute.endLocation,start)) {
                    startFound = true;
                }

                endEta += subRoute.duration.value;
                if (sameLocation(subRoute.endLocation,end)) {
                    break;
                }
            }
        }

        startEta /= 60;
        endEta /= 60;

        // build and return offer
        PickupOffer offer = new PickupOffer(start, end, startEta, endEta, cost, email, mUserEmail, mUserInstanceID);
        mClients.put(email, offer);
        return offer;
    }

    private boolean sameLocation(LatLng start, LatLng end) {
        double dist = getDistance(start,end);
        return dist < SAME_DIST_THRESHOLD;
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
    public Intent realizePickup(String driverEmail, String email) {
        if (!driverEmail.equals(mUserEmail)) {
            return null;
        }
        if (mClients.containsKey(email)) {
            PickupOffer offer = mClients.get(email);
            DbOfferObject dbOffer = new DbOfferObject(offer, mUserEmail);

            // Change addPickupOffer, so it takes all fields of dbOffer, because
            // if argument is not a POD type, it gets cleared/nullified (dunno why)
            long id = dbManagementService.addPickupOffer(dbOffer);

            if (id >= 0) {
                offer.setId(id);
                mClients.put(email, offer);

                //mWaypoints.add(offer.getEndPoints().getFirstEntity());
                //mWaypoints.add(offer.getEndPoints().getSecondEntity());
            }

            mWaypoints.add(offer.getEndPoints().getFirstEntity());
            mWaypoints.add(offer.getEndPoints().getSecondEntity());

            return GMapsLauncher.getNavigationIntent(
                    Utils.latLngToString(mEndPoints.getFirstEntity()),
                    Utils.latLngToString(mEndPoints.getSecondEntity()),
                    Utils.latLngToString(mWaypoints));
        }
        return null;
    }

    /*
        Updates offer in db to indicate, that it started.
     */
    @Override
    public void startPickup(String driverEmail, String email) {
        if (!driverEmail.equals(mUserEmail)) {
            return;
        }
        if (mClients.containsKey(email)) {
            PickupOffer offer = mClients.get(email);
            DbOfferObject dbOffer = dbManagementService.getPickupOffer(offer.getId());
            dbOffer.setStarted(true);
            dbManagementService.updatePickupOffer(offer.getId(), dbOffer);
            dbManagementService.deleteUnstartedPickups(email);
            // First waypoint reached, go further
            mWaypoints.remove(offer.getEndPoints().getFirstEntity());
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
        if (!driverEmail.equals(mUserEmail)) {
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
