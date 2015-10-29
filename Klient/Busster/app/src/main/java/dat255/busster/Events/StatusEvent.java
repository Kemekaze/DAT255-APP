package dat255.busster.Events;

/**
 * used for getting the statustext of an event.
 */
public class StatusEvent {

    private String statusText;


    public StatusEvent(String statusText){

        this.statusText = statusText;
    }

    public String getStatusText() {
        return statusText;
    }

}
