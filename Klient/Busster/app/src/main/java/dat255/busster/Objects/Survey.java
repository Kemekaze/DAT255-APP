package dat255.busster.Objects;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rasmus on 2015-10-13.
 */
public class Survey extends Post {


    private HashMap<Answer, Integer> result ;
    private int participants;


    private enum Answer{
        ONE,
        CROSS,
        TWO;
    }

    public Survey(JSONObject post) {
        super(post);

        result = new  HashMap<Answer, Integer>() ;

        result.put(Answer.ONE,0);
        result.put(Answer.CROSS,0);
        result.put(Answer.TWO,0);

        participants = 0;

    }


    public void addResult(Answer answer){

        result.put(answer, result.get(answer) + 1);
        participants++;
    }

    public int getCount(){
        return participants;
    }


    public String getType() {
        return "Survey";
    }





}
