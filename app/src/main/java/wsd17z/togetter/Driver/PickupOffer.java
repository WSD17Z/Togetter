package wsd17z.togetter.Driver;

import com.google.android.gms.maps.model.LatLng;
import jadex.commons.Tuple2;

/**
 * Created by Kosmos on 13/12/2017.
 */

public class PickupOffer {
    private long mDbId;
    private Tuple2<LatLng, LatLng> mEndPoints;
    private double mStartEta, mEndEta, mTotalCost;
    private String mClientEmail;

    public PickupOffer(LatLng start, LatLng end, double startEta, double endEta, double totalCost, String email) {
        mEndPoints = new Tuple2<>(start, end);
        mStartEta = startEta;
        mEndEta = endEta;
        mTotalCost = totalCost;
        mClientEmail = email;
    }

    public PickupOffer(Tuple2<LatLng, LatLng> endpoints, double startEta, double endEta, double totalCost, String email) {
        mEndPoints = endpoints;
        mStartEta = startEta;
        mEndEta = endEta;
        mTotalCost = totalCost;
        mClientEmail = email;
    }

    public Tuple2<LatLng, LatLng> getEndPoints() {
        return mEndPoints;
    }

    public double getStartEta() {
        return mStartEta;
    }

    public double getEndEta() {
        return mEndEta;
    }

    public double getTotalCost() {
        return mTotalCost;
    }

    public String getEmail() {
        return mClientEmail;
    }

    public void setId(long id) {
        mDbId = id;
    }

    public long getId() {
        return mDbId;
    }
}
