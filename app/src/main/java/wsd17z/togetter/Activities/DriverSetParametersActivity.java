package wsd17z.togetter.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import wsd17z.togetter.R;

/**
 * Created by user on 2017-12-16.
 */

public class DriverSetParametersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_set_parameters);
        SeekBar sb1 = findViewById(R.id.seekBarDP1);
        SeekBar sb2 = findViewById(R.id.seekBarDP2);
        Button btnConf = findViewById(R.id.buttonDPconf);
        final TextView txtDP1 = findViewById(R.id.textViewDP1);
        final TextView txtDP2 = findViewById(R.id.textViewDP2);
        btnConf.setOnClickListener(btnConfList);

        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                double x = ((double) progress) / 10;
                txtDP1.setText(Double.toString(x) + "0zl");

            }
        });

        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub
                txtDP2.setText(Integer.toString(progress) + "min");

            }
        });


    }

    private final View.OnClickListener btnConfList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {


        }
    };

}
