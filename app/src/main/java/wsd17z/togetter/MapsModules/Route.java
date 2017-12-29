package wsd17z.togetter.MapsModules;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Route {
    public Distance distance;
    public Duration duration;
    public String startAddress;
    public LatLng startLocation;
    public String endAddress;
    public LatLng endLocation;

    // points of the whole routes polyline
    public List<LatLng> points;
    // parts of the whole route
    public List<Route> legs;

    public Route() {}

    public Route(String jsonString) {
        JSONObject jsonData;
        int totalDistance = 0;
        int totalDuration = 0;
        try {
            jsonData = new JSONObject(jsonString);
            JSONArray jsonRoutes = jsonData.getJSONArray("routes");
            JSONObject jsonRoute = jsonRoutes.getJSONObject(0);
            legs = new ArrayList<>();
            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            this.points = decodePolyLine(overview_polylineJson.getString("points"));

            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            for (int i = 0; i < jsonLegs.length(); i++) {
                JSONObject jsonLeg = jsonLegs.getJSONObject(i);
                Route legRoute = new Route();

                JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
                JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
                JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
                JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

                legRoute.distance = new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
                totalDistance += jsonDistance.getInt("value");

                legRoute.duration = new Duration(jsonDuration.getString("text"), jsonDuration.getInt("value"));
                totalDuration += jsonDuration.getInt("value");

                legRoute.startAddress = jsonLeg.getString("start_address");
                legRoute.endAddress = jsonLeg.getString("end_address");

                legRoute.startLocation = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
                legRoute.endLocation = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));

                // start location of the whole route is a start of the first leg
                if (this.startAddress == null) {
                    this.startAddress = legRoute.startAddress;
                    this.startLocation = legRoute.startLocation;
                }

                // end location of the whole route is an end of the last leg
                this.endAddress = legRoute.endAddress;
                this.endLocation = legRoute.endLocation;

                this.legs.add(legRoute);
            }
        } catch (Exception ex) {}

        this.distance = new Distance(String.format(Locale.getDefault(),"%1$.1f km", totalDistance/1000f), totalDistance);
        this.duration = new Duration(String.format(Locale.getDefault(),"%1$d min", totalDuration/60), totalDuration);
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }
}
