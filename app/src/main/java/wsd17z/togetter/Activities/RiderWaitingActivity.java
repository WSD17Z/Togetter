package wsd17z.togetter.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import wsd17z.togetter.R;
/**
 * Created by user on 2017-12-16.
 */

public class RiderWaitingActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_waiting);
        Button btYesRW = findViewById(R.id.buttonYesRW);
        Button btNoRW = findViewById(R.id.buttonNoRW);
        btYesRW.setOnClickListener(btYesRWlist);
        btNoRW.setOnClickListener(btNoRWlist);

    }


    private final View.OnClickListener btYesRWlist = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        }
    };

    private final View.OnClickListener btNoRWlist = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        }
    };

}
