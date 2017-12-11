package wsd17z.togetter;

import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.annotation.ServiceComponent;
import wsd17z.togetter.Agents.MyAgent;

/**
 * Created by Kosmos on 07/12/2017.
 */

@Service
public class AgentInterfaceService implements IAgentInterface {
    @ServiceComponent
    private MyAgent myAgent;

    @Override
    public void callAgent(String msg)
    {
        myAgent.callAgent(msg);
    }
}
