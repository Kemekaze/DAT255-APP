package dat255.busster.Events;

/**
 * used for carrying data information.
 */

public class RecieveDataEvent {
    private Object data = null;
    public RecieveDataEvent(Object data) {
        this.data = data;
    }
    public Object getData() {
        return data;
    }
}
