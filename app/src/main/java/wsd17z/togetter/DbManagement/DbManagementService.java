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
    public void updateUser(DbUserObject userObject)
    {
        agent.updateUser(userObject);
    }


    @Override
    public void deleteUser(DbUserObject userObject) {
        agent.deleteUser(userObject);
    }
}
