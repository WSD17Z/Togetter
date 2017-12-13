package wsd17z.togetter.Wallet;

/**
 * Created by Kosmos on 05/12/2017.
 */

public interface IWalletService
{
    double getBalance(String email);
    boolean checkFunds(String email, double value);
    boolean transferFunds(String fromEmail, String toEmail, double value);
}