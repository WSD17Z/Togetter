package wsd17z.togetter.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import wsd17z.togetter.R;

/**
 * Created by Kosmos on 10/12/2017.
 */

public class ChooseRoleActivity extends AppCompatActivity {
    private String mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        mLogin = getIntent().getStringExtra("Login");

        findViewById(R.id.driverBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { changeUiDriver();}
            }
        );
        findViewById(R.id.riderBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { changeUiRider(); }
            }
        );
    }


    private void changeUiDriver() {
        //Intent intent = new Intent(getBaseContext(), SOME.class);
        //intent.putExtra("Login", mLogin);
        //startActivity(intent);
    }

    private void changeUiRider() {
        //Intent intent = new Intent(getBaseContext(), SOMEOTHER.class);
        //intent.putExtra("Login", mLogin);
        //startActivity(intent);
    }
}
