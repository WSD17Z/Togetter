package wsd17z.togetter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import wsd17z.togetter.Driver.IUserService;
import wsd17z.togetter.R;

/**
 * Created by user on 2017-12-16.
 */

public class
DriverSetParametersActivity extends AppCompatActivity {
    private float mPrice;
    private int mDelay;
    private TextView mPriceText;
    private TextView mDelayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_set_parameters);

        mPriceText = findViewById(R.id.textViewDP1);
        mDelayText = findViewById(R.id.textViewDP2);
        findViewById(R.id.buttonDPconf).setOnClickListener(btnConfList);

        SeekBar seekbarPrice = findViewById(R.id.seekBarDP1);
        seekbarPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                mPrice = progress / 10f;
                updateTextBoxes();
            }
        });

        SeekBar seekbarDelay = findViewById(R.id.seekBarDP2);
        seekbarDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                mDelay = progress;
                updateTextBoxes();
            }
        });
    }

    private void updateTextBoxes() {
        mPriceText.setText(String.format(getResources().getString(R.string.setParams_price), mPrice));
        mDelayText.setText(String.format(getResources().getString(R.string.setParams_time), mDelay));
    }

    private final View.OnClickListener btnConfList = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            IFuture<IUserService> userFuture = MainActivity.getPlatform().getService(MainActivity.getPlatform().getPlatformId(), IUserService.class);
            userFuture.addResultListener(new IResultListener<IUserService>() {
                @Override
                public void exceptionOccurred(Exception exception) {
                    Log.d("ERR", exception.toString());
                }

                @Override
                public void resultAvailable(IUserService result) {
                    result.setCost(mPrice);
                    result.setDelay(mDelay);
                }
            });

            Intent intent = new Intent(getBaseContext(), DriverSetRoadActivity.class);
            startActivity(intent);
        }
    };

}
