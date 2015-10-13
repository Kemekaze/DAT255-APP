package dat255.busster.Objects;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rasmus on 2015-10-13.
 */
public class Survey extends Post {


    HashMap<Answer, Integer> result = new  HashMap<Answer, Integer>() ;


    private enum Answer{
        ONE,
        CROSS,
        TWO
    }

    public Survey(JSONObject post) {
        super(post);
    }




}
