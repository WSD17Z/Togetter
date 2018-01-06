package wsd17z.togetter.Activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.firebase.iid.FirebaseInstanceId;

import jadex.android.service.JadexPlatformBinder;
import jadex.bridge.IExternalAccess;
import jadex.commons.future.DefaultResultListener;
import jadex.commons.future.IResultListener;
import wsd17z.togetter.Agents.DbManagementAgent;
import wsd17z.togetter.MyJadexService;
import wsd17z.togetter.R;

public class MainActivity extends AppCompatActivity
{
    private static JadexPlatformBinder JADEX_PLATFORM;
    private static final String TAG = "REG_TOKEN";

    public static JadexPlatformBinder getPlatform() {
        return JADEX_PLATFORM;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ProgressBar spinner = findViewById(R.id.progressBar);
        spinner.setIndeterminate(true);

        DbManagementAgent.initDb(getApplicationContext());
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Token is = \'"+token+"\'");

    }

    @Override
    protected void onStart(){
        super.onStart();
        if (getPlatform() == null || !getPlatform().isBinderAlive()) {
            Intent serviceIntent = new Intent(this, MyJadexService.class);
            startService(serviceIntent);
            bindService(serviceIntent, mServiceConnection, BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getPlatform() != null && getPlatform().isBinderAlive()) {
            unbindService(mServiceConnection);
        }
    }

    private final IResultListener<IExternalAccess> mPlatformResultListener = new DefaultResultListener<IExternalAccess>() {
        @Override
        public void resultAvailable(IExternalAccess result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Log.d("MKK","Platform started: " + getPlatform().getPlatformId().toString());
                }
            });

            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // your code here
            JADEX_PLATFORM = (JadexPlatformBinder) service;
            JADEX_PLATFORM.startJadexPlatform().addResultListener(mPlatformResultListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };
}