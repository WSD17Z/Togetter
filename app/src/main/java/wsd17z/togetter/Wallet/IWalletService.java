package wsd17z.togetter.Wallet;

/**
 * Created by Kosmos on 05/12/2017.
 */

public interface IWalletService
{
    float getBalance(String email);
    boolean checkFunds(String email, float value);
    boolean transferFunds(String fromEmail, String toEmail, float value);
}