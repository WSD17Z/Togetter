package wsd17z.togetter.DbManagement;

import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.annotation.ServiceComponent;
import wsd17z.togetter.Agents.DbManagementAgent;

/**
 * Created by Kosmos on 07/12/2017.
 */

@Service
public class DbManagementService implements IDbManagementService {
    @ServiceComponent
    private DbManagementAgent agent;

    public DbManagementService(DbManagementAgent clientAgent) {
        agent = clientAgent;
    }

    @Override
    public DbUserObject getUser(String email)
    {
        return agent.getUser(email);
    }

    @Override
    public void addUser(DbUserObject userObject)
    {
        agent.addUser(userObject);
    }

    @Override
    public void updateUser(DbUserObject userObject) {
        agent.updateUser(userObject);
    }

    @Override
    public void deleteUser(DbUserObject userObject) {
        agent.deleteUser(userObject);
    }

    @Override
    public DbOfferObject getPickupOffer(long offerId) {
        return agent.getPickupOffer(offerId);
    }

    @Override
    public long addPickupOffer(DbOfferObject offer) {
        return agent.addPickupOffer(offer);
    }

    @Override
    public void updatePickupOffer(long id, DbOfferObject offer) {
        agent.updatePickupOffer(id, offer);
    }

    @Override
    public void deleteUnstartedPickups(String clientEmail) {
        agent.deleteUnstartedPickups(clientEmail);
    }

}
