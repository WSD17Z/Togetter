package wsd17z.togetter.Agents;

import android.util.Log;

import jadex.micro.annotation.Agent;
import jadex.micro.annotation.Implementation;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import wsd17z.togetter.AgentInterfaceService;
import wsd17z.togetter.IAgentInterface;

/**
 * Created by Kosmos on 05/12/2017.
 */

@Agent
@ProvidedServices({
        @ProvidedService(name="agentinterface", type=IAgentInterface.class, implementation = @Implementation(AgentInterfaceService.class))
})

public class MyAgent implements IAgentInterface
{
    public void callAgent(String message) {
        Log.d("MKK", "Agent called with msg: '" + message + "'");
    }
}
