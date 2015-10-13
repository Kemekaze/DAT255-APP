package dat255.app.buzzter.Objects;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rasmus on 2015-10-10.
 */
public class GPS {

    private final String TAG = "dat255.app.buzzter.GPS";

    private String name;
    double longitude;
    private double latitude;
    private String speed;
    private String course;
    private String altitude;
    private String systemId;


    public GPS(JSONObject jsonObject) {

        try {
            String id = jsonObject.getString("id");
            systemId = jsonObject.getString("systemid");
            JSONObject gps = jsonObject.getJSONObject("gps");

            this.longitude =  Double.parseDouble(gps.getString("longitude"));
            this.latitude = Double.parseDouble(gps.getString("latitude"));

            this.name = "Svante";
            Log.i(TAG, gps.getString("longitude"));


        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    public String getName() {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getSpeed() {
        return speed;
    }

    public String getCourse() {
        return course;
    }

    public String getAltitude() {
        return altitude;
    }

    public String getSystemId() {
        return systemId;
    }
}
