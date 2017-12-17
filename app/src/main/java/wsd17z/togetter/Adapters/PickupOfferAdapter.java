package wsd17z.togetter.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import wsd17z.togetter.Driver.PickupOffer;
import wsd17z.togetter.R;

/**
 * Created by user on 2017-12-15.
 */

public class PickupOfferAdapter extends ArrayAdapter<PickupOffer>{

    public PickupOfferAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public PickupOfferAdapter(Context context, int resource, List<PickupOffer> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.pick_up_offer_adapter, null);
        }

        PickupOffer offer = getItem(position);

        if (offer != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.mDbIdView);



            if (tt1 != null) {
                tt1.setText("OFFERT " + Long.toString(offer.getId()) + " START TIME: " + Double.toString(offer.getStartEta()) +
                        " ARRIVAL TIME: " + Double.toString(offer.getEndEta()) + " TOTAL COST: "
                        + Double.toString(offer.getTotalCost()) + " DRIVER EMAIL: " + offer.getDriverEmail());

            }


        }

        return v;
    }
}
