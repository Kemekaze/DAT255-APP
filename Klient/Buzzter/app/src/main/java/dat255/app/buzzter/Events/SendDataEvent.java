package dat255.app.buzzter.Events;

/**
 * Created by Kemekaze on 2015-09-29.
 */
public class SendDataEvent {

    private String eventName;
    private Object data;
    public SendDataEvent(String eventName, Object data) {
        this.eventName = eventName;
        this.data = data;
    }
    public SendDataEvent(String data) {
        this.eventName = data;
    }

    public String getEventName() {
        return eventName;
    }

    public Object getData() {
        return data;
    }
}