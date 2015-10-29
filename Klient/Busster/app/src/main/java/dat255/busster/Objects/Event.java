package dat255.busster.Objects;

import org.json.JSONObject;

/**
 * used for getting the type and the color of an post.
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
