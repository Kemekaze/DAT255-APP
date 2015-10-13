package dat255.busster.Events;

/**
 * Created by ido on 30/09/15.
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
