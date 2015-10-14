package dat255.busster.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rasmus on 2015-10-13.
 */
public class Survey extends Post {


    private HashMap<String, Integer> result ;
    private int participants;
    private ArrayList<String> alternatives = new ArrayList<String>();
    private int options;



    public Survey(JSONObject survey) {
        super(survey);

        //alternatives =survey.get("alternatives");
        try {
            JSONObject meta = survey.getJSONObject("meta");
            JSONObject surveyData = meta.getJSONObject("survey");
            this.options = surveyData.getInt("options");
            JSONObject answers = surveyData.getJSONObject("answers");
            for(int i = 1; i <= this.options; i++){
                JSONObject option = answers.getJSONObject("option"+i);
                this.participants+=option.getInt("count");
                alternatives.add(option.getString("text"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        result = new  HashMap<String, Integer>() ;
    }


    public void addResult(String answer){
        if(result.get(answer) != null) {
            result.put(answer, result.get(answer) + 1);
            participants++;
        }
    }

    public ArrayList<String> getAlternatives() {
        return alternatives;
    }

    public int getOptions() {
        return options;
    }

    public int getCount(){
        return participants;
    }


    public String getType() {
        return "Survey";
    }





}
