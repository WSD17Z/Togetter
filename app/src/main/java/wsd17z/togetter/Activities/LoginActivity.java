package wsd17z.togetter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import wsd17z.togetter.DbManagement.DbUserObject;
import wsd17z.togetter.DbManagement.IDbManagementInterface;
import wsd17z.togetter.R;

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
            if (login.isEmpty()) {
                return;
            }
            String pass = ((EditText) findViewById(R.id.passTxt)).getText().toString();
            int passHash = pass.hashCode();

            //database, check login/pass
            if(MainActivity.getPlatform() != null) {
                IDbManagementInterface dbAgent = MainActivity.getPlatform().getService(IDbManagementInterface.class).get();
                DbUserObject user = dbAgent.getUser(login);
                if (user != null && user.getPassHash() == passHash) {
                    // SUCCESS, do some success stuff
                    Log.d("LOGIN", "success");
                    Intent intent = new Intent(getBaseContext(), ChooseRoleActivity.class);
                    startActivity(intent);
                }
            }
            Log.d("LOGIN", "end");
        }
    };
}