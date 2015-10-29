package dat255.busster.Events;

/**
 * used for getting the status of an event.
 */
public class SavePostEvent {
    private  String status;


    public SavePostEvent(String status){

        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
