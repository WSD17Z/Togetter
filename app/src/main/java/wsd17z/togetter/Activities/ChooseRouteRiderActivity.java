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

import java.util.ArrayList;
import java.util.Arrays;

import wsd17z.togetter.R;

/**
 * Created by user on 2017-12-13.
 */

public class ChooseRouteRiderActivity extends AppCompatActivity {
    private ListView list;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_route_rider);

        list = findViewById(R.id.listViewCZ1);

        String offers[] = {"Offert1", "Offert2", "Offert3", "Offert4"};

        ArrayList<String> offersL = new ArrayList<>();
        offersL.addAll(Arrays.asList(offers));

        adapter = new ArrayAdapter<>(this, R.layout.row, offersL);
        list.setAdapter(adapter);
        Button buttonAcc = findViewById(R.id.buttonCZ4);
        final TextView chosenOffer = findViewById(R.id.textViewCZ8);


        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object o = list.getItemAtPosition(position);
                Log.d("LISTA_OFERT", "Kliknales " +  o.toString());
                chosenOffer.setText(o.toString());

            }
        });
    }
}
