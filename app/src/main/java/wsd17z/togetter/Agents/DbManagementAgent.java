package wsd17z.togetter.Agents;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentCreated;
import jadex.micro.annotation.Implementation;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import wsd17z.togetter.DbManagement.DbOfferObject;
import wsd17z.togetter.DbManagement.DbUserObject;
import wsd17z.togetter.DbManagement.MySQLiteHelper;
import wsd17z.togetter.DbManagement.DbManagementService;
import wsd17z.togetter.DbManagement.IDbManagementService;

/**
 * Created by Kosmos on 05/12/2017.
 */

@Agent
@ProvidedServices({
        @ProvidedService(name="dbmanager", type=IDbManagementService.class, implementation = @Implementation(DbManagementService.class))
})

public class DbManagementAgent implements IDbManagementService
{
    private static MySQLiteHelper mDb;

    @AgentCreated
    public IFuture<Void> agentCreated() {
        return IFuture.DONE;
    }

    public static void initDb(Context ctx) {
        mDb = new MySQLiteHelper(ctx);
        if (mDb.getAllUsers().size() == 0) {
            try {
                mDb.addUser(new DbUserObject("michal", "Michaller", "mkk@test.com", "pass123$%".hashCode()));
                mDb.addUser(new DbUserObject("kuba", "Chicago", "kuba@test.com", "pass123$%".hashCode()));
                mDb.addUser(new DbUserObject("ania", "Drawers", "ania@test.com", "pass123$%".hashCode()));
                mDb.addUser(new DbUserObject("brunhilda", "Giggity", "brudzia@test.com", "pass123$%".hashCode()));
            } catch (Exception ex) {
                Log.d("DB DATA", "Database already initialized with debug data.");
            }
        }
    }

    @Override
    public DbUserObject getUser(String email) {
        if (mDb != null) {
            return mDb.getUser(email);
        }

        return null;
    }

    @Override
    public void addUser(DbUserObject userObject) {
        if (mDb != null) {
            mDb.addUser(userObject);
        }
    }

    @Override
    public void deleteUser(DbUserObject userObject) {
        if (mDb != null) {
            mDb.deleteUser(userObject);
        }
    }

    @Override
    public void updateUser(DbUserObject userObject) {
        if (mDb != null) {
            mDb.updateUser(userObject);
        }
    }

    @Override
    public DbOfferObject getPickupOffer(long id) {
        if (mDb != null) {
            return mDb.getOffer(id);
        }

        return null;
    }

    @Override
    public long addPickupOffer(DbOfferObject offer) {
        if (mDb != null) {
            return mDb.addOffer(offer);
        }

        return -1;
    }

    @Override
    public void updatePickupOffer(long id, DbOfferObject offer) {
        if (mDb != null) {
            mDb.updateOffer(id, offer);
        }
    }

    @Override
    public void deleteUnstartedPickups(String clientEmail) {
        if (mDb != null) {
            mDb.deleteUnstartedOffers(clientEmail);
        }
    }

}
