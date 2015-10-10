package dat255.app.buzzter.Events;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import dat255.app.buzzter.Objects.GPS;
import dat255.app.buzzter.Objects.Post;

/**
 * Created by Rasmus on 2015-10-10.
 */
public class GPSEvent {

    public List<GPS> gpsList;
    public int eventType = 0;
    /* 0 = (add) add to end of list and keep existing
     * 1 = (refresh) add new ones and delete existing
     * 2 = (load new) add to beginning of list and keep existing
     *
     */
    public GPSEvent(List<GPS> gpsList){
        this.gpsList  = gpsList;

    }


}
