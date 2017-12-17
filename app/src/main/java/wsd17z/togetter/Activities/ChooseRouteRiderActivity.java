package wsd17z.togetter.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;

import wsd17z.togetter.Adapters.PickupOfferAdapter;
import wsd17z.togetter.Driver.PickupOffer;
import wsd17z.togetter.R;

/**
 * Created by user on 2017-12-13.
 */

public class ChooseRouteRiderActivity extends AppCompatActivity {
    private ListView list;
    private PickupOfferAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_route_rider);
        PickupOffer test1 = new PickupOffer(new LatLng(0.123, 0.222),
                new LatLng(0.222, 0.555), 15.0, 28.0, 21.2,"lala@trala.com" );
        PickupOffer test2 = new PickupOffer(new LatLng(0.123, 0.222),
                new LatLng(0.222, 0.555), 20.0, 30.0, 35.0,"trilu@lilu.com" );
        list = findViewById(R.id.listViewCZ1);
        adapter = new PickupOfferAdapter(this, R.layout.pick_up_offer_adapter);
        adapter.addAll(test1, test2);
        list.setAdapter(adapter);

        Button buttonAcc = findViewById(R.id.buttonCZ4);
        final TextView chosenOffer = findViewById(R.id.textViewCZ8);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = list.getItemAtPosition(position);
                PickupOffer puo = (PickupOffer) obj;
                Log.d("LISTA_OFERT", "Kliknales " +  obj.toString());
                chosenOffer.setText(Long.toString(puo.getId()));

            }
        });
    }

}