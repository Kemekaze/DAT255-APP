package dat255.busster.Objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * used for getting the different fields of a busstop and setting it for an object.
 */
public class Stop {
    private String _id ="";
    private String name = "";
    private String stopid = "";

    public Stop(JSONObject stop) {
        try {
            this._id = stop.getString("_id");
            this.name = stop.getString("name");
            this.stopid = stop.getString("stopid");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getStopid() {
        return stopid;
    }
}
