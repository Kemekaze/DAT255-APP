package elias.testdbconnection;


import org.json.JSONArray;

public class MessageEvent {

    public final JSONArray message;

    public MessageEvent(JSONArray message){
        this.message = message;
    }
}
