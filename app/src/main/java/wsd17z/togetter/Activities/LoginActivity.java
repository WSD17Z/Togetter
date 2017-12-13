package wsd17z.togetter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import wsd17z.togetter.DbManagement.DbUserObject;
import wsd17z.togetter.DbManagement.IDbManagementService;
import wsd17z.togetter.Driver.IDriverService;
import wsd17z.togetter.R;

import static jadex.bridge.service.RequiredServiceInfo.SCOPE_LOCAL;

public class LoginActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Button loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(mBtnOnClickListener);
    }


    private final View.OnClickListener mBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String login = ((EditText) findViewById(R.id.loginTxt)).getText().toString();
            // tymczasowo wykomentowane
//            if (login.isEmpty()) {
//                return;
//            }
            String pass = ((EditText) findViewById(R.id.passTxt)).getText().toString();
            int passHash = pass.hashCode();

            // tymczasowo wykomentowane
            //database, check login/pass
//            if(MainActivity.getPlatform() != null) {
//                IDbManagementService dbAgent = MainActivity.getPlatform().getService(IDbManagementService.class).get();
//                DbUserObject user = dbAgent.getUser(login);
//                if (user != null && user.getPassHash() == passHash) {
//                    // SUCCESS, do some success stuff
//                    Log.d("LOGIN", "success");
//
//                    // Set login for local driver agent
//                    IDriverService driver = MainActivity.getPlatform().getService(IDriverService.class, SCOPE_LOCAL).get();
//                    driver.setEmail(login);

                    // TODO: forward login through intent extras and set it after choosing a role
                    Intent intent = new Intent(getBaseContext(), ChooseRoleActivity.class);
                    intent.putExtra("Login", login);
                    startActivity(intent);
//                }
//            }
            Log.d("LOGIN", "end");
        }
    };
}