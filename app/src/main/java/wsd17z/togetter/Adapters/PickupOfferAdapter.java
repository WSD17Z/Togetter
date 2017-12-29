package wsd17z.togetter.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import wsd17z.togetter.Driver.PickupOffer;
import wsd17z.togetter.R;

/**
 * Created by user on 2017-12-15.
 */

public class PickupOfferAdapter extends ArrayAdapter<PickupOffer>{

    public  PickupOfferAdapter(Context context, int textViewResourceId) {
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
            TextView driver = v.findViewById(R.id.offerDriver);
            TextView eta = v.findViewById(R.id.offerEta);
            TextView etd = v.findViewById(R.id.offerEtd);
            TextView cost = v.findViewById(R.id.offerPrice);
            TextView id = v.findViewById(R.id.offerId);

            id.setText(String.format(Locale.getDefault(),"%1$d.", position + 1));
            eta.setText(String.format(Locale.getDefault(),"ETA: %1$.0f min", offer.getStartEta()));
            etd.setText(String.format(Locale.getDefault(),"ETD: %1$.0f min", offer.getEndEta()));
            cost.setText(String.format(Locale.getDefault(),"Price: %1$.2f zl", offer.getTotalCost()));
            driver.setText(String.format(Locale.getDefault(),"Driver: %1$s", offer.getDriverEmail()));
        }

        return v;
    }
}
