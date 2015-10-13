package dat255.busster.Objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rasmus on 2015-10-10.
 */
public class GPS {

    private String name;
    private int longitude;
    private int latitude;
    private String speed;
    private String course;
    private String altitude;
    private String systemId;

    public GPS(JSONObject jsonObject) {

        try {
            String id = jsonObject.getString("id");
            systemId = jsonObject.getString("systemid");
            JSONObject gps = jsonObject.getJSONObject("gps");
            this.longitude =  Integer.parseInt(gps.getString("longitude"));
            gps.getInt("longitude");
            this.latitude = Integer.parseInt(gps.getString("latitude"));
            this.name = "Svante";


        } catch (JSONException e) {
            e.printStackTrace();
        }




    }

    public String getName() {
        return name;
    }

    public int getLongitude() {
        return longitude;
    }

    public int getLatitude() {
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
