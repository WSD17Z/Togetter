package wsd17z.togetter.DbManagement;


/**
 * Created by Kosmos on 05/12/2017.
 */

public interface IDbManagementService
{
    DbUserObject getUser(String email);
    void addUser(DbUserObject userObject);
    void updateUser(DbUserObject userObject);
    void deleteUser(DbUserObject userObject);
    DbOfferObject getPickupOffer(long offerId);
    long addPickupOffer(DbOfferObject offer);
    void updatePickupOffer(long offerId, DbOfferObject offer);
    void deleteUnstartedPickups(String clientEmail);
}