package wsd17z.togetter.Wallet;

import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.annotation.ServiceComponent;
import wsd17z.togetter.Agents.WalletAgent;

/**
 * Created by Kosmos on 07/12/2017.
 */

@Service
public class WalletService implements IWalletService {
    @ServiceComponent
    private WalletAgent agent;

    @Override
    public double getBalance(String email) {
        return agent.getBalance(email);
    }

    @Override
    public boolean checkFunds(String email, double value) {
        return agent.checkFunds(email, value);
    }

    @Override
    public boolean transferFunds(String fromEmail, String toEmail, double value) {
        return agent.transferFunds(fromEmail, toEmail, value);
    }
}
