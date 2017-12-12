package wsd17z.togetter.Agents;

import android.util.Log;

import jadex.micro.annotation.Agent;
import jadex.micro.annotation.Implementation;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import wsd17z.togetter.ClientManagement.ClientManagementService;
import wsd17z.togetter.ClientManagement.IClientManagementService;

/**
 * Created by Kosmos on 05/12/2017.
 */

@Agent
@ProvidedServices({
        @ProvidedService(name="clientmanagement", type=IClientManagementService.class, implementation=@Implementation(ClientManagementService.class))
})

public class ClientManagementAgent implements IClientManagementService
{
    public void reportError(String message) {
        Log.d("USER TROUBLE", "Error has been reported: '" + message + "'");
    }
}
