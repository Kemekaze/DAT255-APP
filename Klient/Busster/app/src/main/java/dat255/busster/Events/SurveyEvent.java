package dat255.busster.Events;

import java.util.List;

import dat255.busster.Objects.Survey;

/**
 * Created by Rasmus on 2015-10-14.
 */
public class SurveyEvent {

    public List<Survey> surveys;

    public SurveyEvent(List<Survey> surveys){
        this.surveys = surveys;

    }

}
