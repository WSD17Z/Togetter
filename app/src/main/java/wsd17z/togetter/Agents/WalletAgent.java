package wsd17z.togetter.Agents;

import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.Binding;
import jadex.micro.annotation.Implementation;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;
import wsd17z.togetter.DbManagement.DbUserObject;
import wsd17z.togetter.DbManagement.IDbManagementService;
import wsd17z.togetter.Wallet.IWalletService;
import wsd17z.togetter.Wallet.WalletService;

/**
 * Created by Kosmos on 05/12/2017.
 */

@Agent
@ProvidedServices({
        @ProvidedService(name="wallet", type=IWalletService.class, implementation = @Implementation(WalletService.class))
})
@RequiredServices({
        @RequiredService(name="dbmanager", type=IDbManagementService.class, binding=@Binding(scope= RequiredServiceInfo.SCOPE_GLOBAL))
})

public class WalletAgent implements IWalletService
{
    @AgentFeature
    private IRequiredServicesFeature requiredServicesFeature;
    private IDbManagementService dbManagementService;

    @AgentBody
    public void executeBody() {
        dbManagementService = (IDbManagementService) requiredServicesFeature.getRequiredService("dbmanager").get();
    }

    @Override
    public double getBalance(String email) {
        DbUserObject user = dbManagementService.getUser(email);
        return user != null ? user.getBalance() : 0f;
    }

    @Override
    public boolean checkFunds(String email, double value) {
        DbUserObject user = dbManagementService.getUser(email);
        return checkFunds(user, value);
    }

    private boolean checkFunds(DbUserObject user, double value) {
        return user != null && user.getBalance() >= value;
    }

    @Override
    public boolean transferFunds(String fromEmail, String toEmail, double value) {
        DbUserObject userFrom = dbManagementService.getUser(fromEmail);
        if (checkFunds(userFrom, value)) {
            DbUserObject userTo = dbManagementService.getUser(fromEmail);
            if (userTo != null) {
                userFrom.subtractBalance(value);
                userTo.addBalance(value);
                dbManagementService.updateUser(userFrom);
                dbManagementService.updateUser(userTo);
                return true;
            }
        }
        return false;
    }
}
