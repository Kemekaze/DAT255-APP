package dat255.busster.Events;

import java.util.List;

import dat255.busster.Objects.Survey;

/**
 * handling the events of typ survey.
 */
public class SurveyEvent {

    public List<Survey> surveys;

    public SurveyEvent(List<Survey> surveys){
        this.surveys = surveys;

    }

}
