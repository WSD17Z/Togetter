package wsd17z.togetter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        mLogin = getIntent().getStringExtra("Login");
        driverBtn = findViewById(R.id.driverBtn);
        clientBtn = findViewById(R.id.riderBtn);
        findViewById(R.id.driverBtn).setOnClickListener(buttonListener);
        findViewById(R.id.riderBtn).setOnClickListener(buttonListener);
    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

           // Intent intent = new Intent(getBaseContext(), ChooseRouteRiderActivity.class);
            Intent intent = new Intent(getBaseContext(), ChooseRouteRiderActivity.class);
            Class agentClass = ClientAgent.class;

            if (v == driverBtn) {
                intent = new Intent(getBaseContext(), DriverSetParametersActivity.class);
                agentClass = DriverAgent.class;
            }

            IFuture<IComponentIdentifier> futureCid = MainActivity.getPlatform().startComponent("togetterUser", agentClass);
            futureCid.addResultListener(new IResultListener<IComponentIdentifier>() {
                @Override
                public void exceptionOccurred(Exception exception) {
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
                        }
                    });
                }
            });

            intent.putExtra("Login", mLogin);
            startActivity(intent);
        }
    };
}
