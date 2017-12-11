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
import wsd17z.togetter.DbManagement.DbUserObject;
import wsd17z.togetter.DbManagement.MySQLiteHelper;
import wsd17z.togetter.DbManagement.DbManagementService;
import wsd17z.togetter.DbManagement.IDbManagementInterface;

/**
 * Created by Kosmos on 05/12/2017.
 */

@Agent
@ProvidedServices({
        @ProvidedService(name="dbmanager", type=IDbManagementInterface.class, implementation = @Implementation(DbManagementService.class))
})

public class DbManagementAgent implements IDbManagementInterface
{
    private static MySQLiteHelper mDb;

    @AgentCreated
    public IFuture<Void> agentCreated() {
        return IFuture.DONE;
    }

    public static void initDb(Context ctx)
    {
        mDb = new MySQLiteHelper(ctx);
        try {
            mDb.addUser(new DbUserObject("michal", "Michaller", "mkk@test.com", "pass123$%".hashCode()));
        } catch (SQLiteConstraintException ex) {
            Log.d("DB DATA", "Database already initialized with debug data.");
        }
    }

    @Override
    public DbUserObject getUser(String email)
    {
        if (mDb != null) {
            return mDb.getUser(email);
        }

        return null;
    }

    @Override
    public void addUser(DbUserObject userObject)
    {
        if (mDb != null) {
            mDb.addUser(userObject);
        }
    }

    @Override
    public void deleteUser(DbUserObject userObject)
    {
        if (mDb != null) {
            mDb.deleteUser(userObject);
        }
    }
}
