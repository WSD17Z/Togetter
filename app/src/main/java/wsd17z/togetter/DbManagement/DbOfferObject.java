package wsd17z.togetter.DbManagement;

import wsd17z.togetter.Driver.PickupOffer;

/**
 * Created by Kosmos on 10/12/2017.
 */

public class DbOfferObject {
    private double mPrice;
    private String mDriverEmail, mClientEmail;
    private boolean mStarted, mEnded, mPaid;

    public DbOfferObject(PickupOffer offer, boolean started, boolean ended, boolean paid, String email) {
        super();
        mPrice = offer.getTotalCost();
        mStarted = started;
        mEnded = ended;
        mPaid = paid;
        mClientEmail = offer.getEmail();
        mDriverEmail = email;
    }

    public DbOfferObject(double price, boolean started, boolean ended, boolean paid, String clientEmail, String driverEmail) {
        super();
        mPrice = price;
        mStarted = started;
        mEnded = ended;
        mPaid = paid;
        mClientEmail = clientEmail;
        mDriverEmail = driverEmail;
    }

    public DbOfferObject(PickupOffer offer, String email) {
        super();
        mPrice = offer.getTotalCost();
        mClientEmail = offer.getEmail();
        mDriverEmail = email;
        mStarted = false;
        mEnded = false;
        mPaid = false;
    }

    public double getPrice() {
        return mPrice;
    }

    public String getClientEmail() {
        return mClientEmail;
    }

    public String getDriverEmail() {
        return mDriverEmail;
    }

    public boolean getStarted() {
        return mStarted;
    }

    public boolean getEnded() {
        return mEnded;
    }

    public boolean getPaid() {
        return mPaid;
    }

    public void setStarted(boolean started) {
        mStarted = started;
    }

    public void setEnded(boolean ended) {
        mEnded = ended;
    }

    public void setPaid(boolean paid) {
        mPaid = paid;
    }
}
