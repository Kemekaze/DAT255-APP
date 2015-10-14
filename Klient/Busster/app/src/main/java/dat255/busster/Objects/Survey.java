package dat255.busster.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rasmus on 2015-10-13.
 */
public class Survey extends Post {


    private ArrayList<Integer> result;
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
            result = new ArrayList<Integer>() ;
            for(int i = 1; i <= this.options; i++){
                JSONObject option = answers.getJSONObject("option"+i);
                this.participants+=option.getInt("count");
                alternatives.add(option.getString("text"));
                result.add(i-1,option.getInt("count"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void addResult(int pos){

            result.add(result.get(pos)+1);
            participants++;

    }

    public int getTotalCount(){

        return participants;
    }

    public int getCount(int pos){

        return result.get(pos);
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
