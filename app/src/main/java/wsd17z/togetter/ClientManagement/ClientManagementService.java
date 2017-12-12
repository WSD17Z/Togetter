package wsd17z.togetter.ClientManagement;

import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.annotation.ServiceComponent;
import wsd17z.togetter.Agents.ClientManagementAgent;

/**
 * Created by Kosmos on 07/12/2017.
 */

@Service
public class ClientManagementService implements IClientManagementService {
    @ServiceComponent
    private ClientManagementAgent clientManagementAgent;

    @Override
    public void reportError(String msg)
    {
        clientManagementAgent.reportError(msg);
    }
}
