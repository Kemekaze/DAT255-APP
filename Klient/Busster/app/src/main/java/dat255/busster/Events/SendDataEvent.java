package dat255.busster.Events;

/**
 * Created by Kemekaze on 2015-09-29.
 */
public class SendDataEvent {

    private String eventName;
    private Object data = null;
    public SendDataEvent(String eventName, Object data) {
        this.eventName = eventName;
        this.data = data;
    }
    public SendDataEvent(String eventName) {
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public Object getData() {
        return data;
    }
}
