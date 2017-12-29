package wsd17z.togetter.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import jadex.bridge.IComponentIdentifier;
import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import wsd17z.togetter.Agents.ClientAgent;
import wsd17z.togetter.Agents.DriverAgent;
import wsd17z.togetter.Driver.IUserService;
import wsd17z.togetter.R;

/**
 * Created by Kosmos on 10/12/2017.
 */

public class ChooseRoleActivity extends AppCompatActivity {
    private String mLogin;
    private Button driverBtn, clientBtn;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        mLogin = getIntent().getStringExtra("Login");
        driverBtn = findViewById(R.id.driverBtn);
        clientBtn = findViewById(R.id.riderBtn);
        findViewById(R.id.driverBtn).setOnClickListener(buttonListener);
        findViewById(R.id.riderBtn).setOnClickListener(buttonListener);
        mDialog = ProgressDialog.show(this, "Please wait.",
                "Switching to selected mode...", true);
        mDialog.dismiss();
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(), ChooseRouteRiderActivity.class);
            Class agentClass = ClientAgent.class;

            if (v == driverBtn) {
                intent = new Intent(getBaseContext(), DriverSetParametersActivity.class);
                agentClass = DriverAgent.class;
            }
            final Intent finalIntent = intent;
            final Class finalAgentClass = agentClass;
            mDialog.show();

            IFuture<IComponentIdentifier> futureCid = MainActivity.getPlatform().startComponent("togetterUser123", agentClass);
            futureCid.addResultListener(new IResultListener<IComponentIdentifier>() {
                @Override
                public void exceptionOccurred(Exception exception) {
                    Log.d("ERR", exception.toString());
                }

                @Override
                public void resultAvailable(IComponentIdentifier result) {
                    IFuture<IUserService> userFuture = MainActivity.getPlatform().getService(MainActivity.getPlatform().getPlatformId(), IUserService.class);
                    userFuture.addResultListener(new IResultListener<IUserService>() {
                        @Override
                        public void exceptionOccurred(Exception exception) {
                            Log.d("ERR", exception.toString());
                        }

                        @Override
                        public void resultAvailable(IUserService result) {
                            result.setEmail(mLogin);
                            // FOR TESTING PURPOSES start: Pustułeczki, end: Kazury
                            result.setEndPoints(52.144956, 21.018605, 52.138400, 21.047565);
                            if (finalAgentClass == ClientAgent.class) {
                                final IFuture<IComponentIdentifier> futureCid1 = MainActivity.getPlatform().startComponent("togetterDriver1", DriverAgent.class);
                                futureCid1.addResultListener(new IResultListener<IComponentIdentifier>() {
                                    @Override
                                    public void exceptionOccurred(Exception exception) {
                                    }

                                    @Override
                                    public void resultAvailable(IComponentIdentifier result) {
                                        final IFuture<IComponentIdentifier> futureCid2 = MainActivity.getPlatform().startComponent("togetterDriver2", DriverAgent.class);
                                        futureCid2.addResultListener(new IResultListener<IComponentIdentifier>() {
                                            @Override
                                            public void exceptionOccurred(Exception exception) {
                                            }

                                            @Override
                                            public void resultAvailable(IComponentIdentifier result) {
                                                final IFuture<IComponentIdentifier> futureCid3 = MainActivity.getPlatform().startComponent("togetterDriver3", DriverAgent.class);
                                                futureCid3.addResultListener(new IResultListener<IComponentIdentifier>() {
                                                    @Override
                                                    public void exceptionOccurred(Exception exception) {
                                                    }

                                                    @Override
                                                    public void resultAvailable(IComponentIdentifier result) {
                                                        final IFuture<IComponentIdentifier> futureCid4 = MainActivity.getPlatform().startComponent("togetterDriver4", DriverAgent.class);
                                                        futureCid4.addResultListener(new IResultListener<IComponentIdentifier>() {
                                                            @Override
                                                            public void exceptionOccurred(Exception exception) {
                                                            }

                                                            @Override
                                                            public void resultAvailable(IComponentIdentifier result) {
                                                                final IFuture<IComponentIdentifier> futureCid5 = MainActivity.getPlatform().startComponent("togetterDriver5", DriverAgent.class);
                                                                futureCid5.addResultListener(new IResultListener<IComponentIdentifier>() {
                                                                    @Override
                                                                    public void exceptionOccurred(Exception exception) {
                                                                    }

                                                                    @Override
                                                                    public void resultAvailable(IComponentIdentifier result) {
                                                                        mDialog.dismiss();
                                                                        finalIntent.putExtra("Login", mLogin);
                                                                        startActivity(finalIntent);
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            });

            //intent.putExtra("Login", mLogin);
            //startActivity(intent);
        }
    };
}
