package dat255.app.buzzter.Events;

/**
 * Created by ido on 30/09/15.
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
