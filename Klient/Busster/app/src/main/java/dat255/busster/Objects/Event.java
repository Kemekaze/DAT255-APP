package dat255.busster.Objects;

import org.json.JSONObject;

/**
 * Created by elias on 2015-10-15.
 */
public class Event extends  Post{

    private String color = "#ffb300";
    private String type = "event";

    public Event(JSONObject post) {
        super(post);
    }

    @Override
    public String getColor() {
        return this.color;
    }

    @Override
    public String getType() {
        return this.type;
    }
}
