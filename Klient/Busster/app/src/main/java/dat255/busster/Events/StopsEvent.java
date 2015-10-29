package dat255.busster.Events;

import java.util.List;

import dat255.busster.Objects.Stop;

/**
 * handling the busstop events 
 */
public class StopsEvent {

    public List<Stop> stops;

    public StopsEvent(List<Stop> stops){
        this.stops = stops;
    }

}
