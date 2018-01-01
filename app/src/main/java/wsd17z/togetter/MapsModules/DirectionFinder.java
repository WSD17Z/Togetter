package wsd17z.togetter.MapsModules;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;


public class DirectionFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyDnwLF2-WfK8cVZt9OoDYJ9Y8kspXhEHfI";
    private DirectionFinderListener listener;
    private List<String> waypoints;
    private String origin;
    private String destination;

    public DirectionFinder(DirectionFinderListener listener, String origin, String destination) {
        this.listener = listener;
        this.origin = origin;
        this.destination = destination;
    }

    public DirectionFinder(DirectionFinderListener listener, String origin, String destination, List<String> waypoints) {
        this.listener = listener;
        this.waypoints = waypoints;
        this.origin = origin;
        this.destination = destination;
    }

    public void execute() throws UnsupportedEncodingException {
        listener.onDirectionFinderStart();
        new DownloadRawData().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        String urlDestination = URLEncoder.encode(destination, "utf-8");
        String urlWaypoints = "";
        if (waypoints != null) {
            for (String wp : waypoints) {
                urlWaypoints += URLEncoder.encode(wp, "utf-8") + "|";
            }
            if (urlWaypoints.endsWith("|")) {
                urlWaypoints = urlWaypoints.substring(0, urlWaypoints.length() - 2);
            }
            urlWaypoints = (waypoints.isEmpty() ? "" : "&waypoints=" + urlWaypoints);
        }
        return DIRECTION_URL_API + "origin=" + urlOrigin
                + "&destination=" + urlDestination
                + urlWaypoints
                + "&key=" + GOOGLE_API_KEY;
    }

    public Route executeOnThread() {
        String urlStr;
        try {
            urlStr = createUrl();

            URL url = new URL(urlStr);
            InputStream is = url.openConnection().getInputStream();
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            return new Route(buffer.toString());

        } catch (Exception ex) {
            Log.d("MKK", ex.getMessage());
        }
        return null;
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            try {
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            listener.onDirectionFinderSuccess(new Route(jsonString));
        }
    }
}
