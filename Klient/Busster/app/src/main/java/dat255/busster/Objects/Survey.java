package dat255.busster.Objects;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rasmus on 2015-10-13.
 */
public class Survey extends Post {


    private HashMap<String, Integer> result ;
    private int participants;
    private ArrayList<String> alternatives;



    public Survey(JSONObject survey) {
        super(survey);

        //alternatives =survey.get("alternatives");


        result = new  HashMap<String, Integer>() ;


        participants = 0;

    }


    public void addResult(String answer){
        if(result.get(answer) != null) {
            result.put(answer, result.get(answer) + 1);
            participants++;
        }
    }

    public int getCount(){
        return participants;
    }


    public String getType() {
        return "Survey";
    }





}
