package dat255.busster.Events;

import java.util.List;

import dat255.busster.Objects.Stop;

public class StopsEvent {

    public List<Stop> stops;

    public StopsEvent(List<Stop> stops){
        this.stops = stops;
    }

}