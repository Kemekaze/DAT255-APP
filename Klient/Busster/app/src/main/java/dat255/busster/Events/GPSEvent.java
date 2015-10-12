package dat255.busster.Events;

import java.util.List;

import dat255.busster.Objects.GPS;

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
