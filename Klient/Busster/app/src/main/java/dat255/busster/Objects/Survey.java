package dat255.busster.Objects;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Survey is a subclass to post. the exception for surveys is that it has
 * alternatives which can be chosen and will contain a result and amount
 * of participants. when people are voting in a survey result will change
 * dependent on what alternatives the users are voting for.
 */
public class Survey extends Post {



    private ArrayList<Integer> result;
    private ArrayList<String> alternatives = new ArrayList<String>();

    private int participants;
    private int options;

    private String color = "#43a047";
    private String type = "survey";

    /**
     *Creating a new survey with given alternatives, result and porticipatns.
     * the values collected from a database.
     * @param survey object with values from the database.
     */
    public Survey(JSONObject survey) {
        super(survey);

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

    /**
     * Gets amount of participants.
     * @return this amount.
     */
    public int getCount(){
        return participants;
    }

    /**
     * Gets amount of participants of each alternative.
     * @param pos position of alternative.
     * @return this amount.
     */
    public int getCount(int pos){

        return result.get(pos);

    }

    /**
     * Gets list of the diffrent alternatives.
     * @return this list.
     */
    public ArrayList<String> getAlternatives() {
        return alternatives;
    }

    /**
     * Gets amount of alternatives
     * @return this amount
     */
    public int getOptions() {
        return options;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getColor() {
        return this.color;
    }

}
