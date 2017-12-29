package wsd17z.togetter.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import jadex.commons.concurrent.TimeoutException;
import jadex.commons.future.IFuture;
import jadex.commons.future.IResultListener;
import wsd17z.togetter.Adapters.PickupOfferAdapter;
import wsd17z.togetter.Driver.IRideService;
import wsd17z.togetter.Driver.PickupOffer;
import wsd17z.togetter.R;

/**
 * Created by user on 2017-12-13.
 */

public class ChooseRouteRiderActivity extends AppCompatActivity {
    private ListView mList;
    private PickupOfferAdapter mAdapter;
    private ProgressDialog mDialog;
    private PickupOffer mChosenOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_route_rider);
        mList = findViewById(R.id.listViewCZ1);
        mAdapter = new PickupOfferAdapter(this, R.layout.pick_up_offer_adapter);
        mDialog = ProgressDialog.show(this, "Please wait.",
                "Searching for offers...", true);

        IFuture<IRideService> clientFuture = MainActivity.getPlatform().getService(MainActivity.getPlatform().getPlatformId(), IRideService.class);
        clientFuture.addResultListener(new IResultListener<IRideService>() {
            @Override
            public void exceptionOccurred(Exception exception) {
                Log.d("ERR", exception.toString());
            }

            @Override
            public void resultAvailable(IRideService rideSrc) {
                final IFuture<Void> refresh = rideSrc.refreshDrivers();
                try {
                    refresh.get(10000);
                } catch (TimeoutException ex) {
                    Log.d("MKK", "ended beforetime");
                }

                List<PickupOffer> offers = rideSrc.queryForPickupOffers();
                if (offers != null) {
                    final List<PickupOffer> offersFinal = offers;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mAdapter.addAll(offersFinal);
                                mList.setAdapter(mAdapter);
                            } catch (Exception ex) {
                                Log.d("ERR", ex.getMessage());
                            }
                        }
                    });
                }
                mDialog.dismiss();
                /*
                refresh.addResultListener(new IResultListener<Void>() {
                    @Override
                    public void exceptionOccurred(Exception exception) {
                        Log.d("ERR", exception.toString());
                    }

                    @Override
                    public void resultAvailable(Void result) {
                        final IFuture<List<PickupOffer>> offersFut = rideSrcFinal.queryForPickupOffers();
                        List<PickupOffer> offers = offersFut.get();
                        if (offers != null) {
                            final List<PickupOffer> offersFinal = offers;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        mAdapter.addAll(offersFinal);
                                        mList.setAdapter(mAdapter);
                                    } catch (Exception ex) {
                                        Log.d("ERR", ex.getMessage());
                                    }
                                }
                            });
                        }
                        mDialog.dismiss();
                    }
                });*/
/*
                final IFuture<List<PickupOffer>> offersFut = result.queryForPickupOffers();
                offersFut.addResultListener(new IResultListener<List<PickupOffer>>() {
                    @Override
                    public void exceptionOccurred(Exception exception) {
                        Log.d("ERR", exception.toString());
                    }

                    @Override
                    public void resultAvailable(List<PickupOffer> offers) {
                        if (offers != null) {
                            final List<PickupOffer> offersFinal = offers;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        mAdapter.addAll(offersFinal);
                                        mList.setAdapter(mAdapter);
                                    } catch (Exception ex) {
                                        Log.d("ERR", ex.getMessage());
                                    }
                                }
                            });
                        }
                        mDialog.dismiss();
                    }
                });
*/
            }
        });

        Button buttonAcc = findViewById(R.id.buttonCZ4);
        final TextView chosenOffer = findViewById(R.id.textViewCZ8);

        mList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mChosenOffer = (PickupOffer) mList.getItemAtPosition(position);
                //Log.d("LISTA_OFERT", "Kliknales " +  mChosenOffer.toString());
                chosenOffer.setText(String.format(Locale.getDefault(),"OFFER %1$d FROM: %2$s", position + 1, mChosenOffer.getDriverEmail()));
            }
        });

        buttonAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IFuture<IRideService> clientFuture = MainActivity.getPlatform().getService(MainActivity.getPlatform().getPlatformId(), IRideService.class);
                clientFuture.addResultListener(new IResultListener<IRideService>() {
                    @Override
                    public void exceptionOccurred(Exception exception) {
                        Log.d("ERR", exception.toString());
                    }

                    @Override
                    public void resultAvailable(IRideService result) {
                        result.chooseOffer(mChosenOffer);
                        Intent intent = new Intent(getBaseContext(), RiderWaitingActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

}