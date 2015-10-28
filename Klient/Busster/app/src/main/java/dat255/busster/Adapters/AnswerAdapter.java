package dat255.busster.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import dat255.busster.DB.SurveyDBHandler;
import dat255.busster.Events.SendDataEvent;
import dat255.busster.Objects.Survey;
import dat255.busster.Objects.SurveyVote;
import dat255.busster.R;
import dat255.busster.Resources.Constants;
import dat255.busster.Resources.DialogTool;
import dat255.busster.SurveyActivity;
import de.greenrobot.event.EventBus;

/**
 * An adapter for the list of alternatives in the survey
 */
public class AnswerAdapter extends RecyclerSwipeAdapter<AnswerAdapter.ViewHolder> {

    public static final String TAG = "dat255.AnswerAdapter";
    private List<String> answers;
    private Context context;
    private RecyclerView recyclerView;
    public Survey survey;
    private SurveyDBHandler surveyHandler;

    private static RadioButton lastChecked = null;


    @Override
    public AnswerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView id;
        public RadioButton option;
        public View view;


        public ViewHolder(View view) {
            super(view);

            option = (RadioButton) view.findViewById(R.id.option);

            this.view = view;
        }
    }

    public AnswerAdapter(Context context, List<String> answers, RecyclerView mRecyclerView) {
        this.context = context;
        this.answers = answers;
        this.recyclerView = mRecyclerView;
        surveyHandler = new SurveyDBHandler(context, null);

    }

    /**
     * Setting the layout for each alternative i the list.
     * @param viewHolder which defines what the view will contain
     * @param position the position of the alternative
     */
    @Override
    public void onBindViewHolder(final AnswerAdapter.ViewHolder viewHolder, int position) {

        viewHolder.option.setText(getItem(position));
        List<Integer> exists = surveyHandler.checkIfExists(survey.getId());
        Log.i(TAG, "0: " + exists.get(0));
        if (exists.get(0) == 1) {
            Log.i(TAG, "0: " + exists.get(0) + " 1: " + exists.get(1) + " pos: " + position);
            if (position == exists.get(1)) {
                viewHolder.option.setChecked(true);
                lastChecked = viewHolder.option;
            }
        }

        viewHolder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton checked = (RadioButton) v;
                if (lastChecked != null) {
                    if (lastChecked != checked)
                        checked.setChecked(false);
                    return;
                }
                lastChecked = checked;
                int position = recyclerView.getChildAdapterPosition(viewHolder.view);
                Log.i("Survey", "onClick : " + position);


                if (surveyHandler.checkIfExists(survey.getId()).get(0) == 0) {
                    JSONObject query = new JSONObject();
                    try {
                        query.put("option", position + 1);
                        query.put("post_id", survey.getId());
                        showResult();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    surveyHandler.addVote(new SurveyVote(survey.getId(), position));
                    EventBus.getDefault().post(new SendDataEvent(Constants.SocketEvents.UPDATE_SURVEY, query));


                    Log.i("Survey", survey.getCount() + "");
                }

            }
        });

        if (isVoted()) {
            showResult();
        }
    }



    private void showResult() {
        SurveyActivity surveyActivity = (SurveyActivity) context;
        DialogTool.resultDialog(surveyActivity, survey);
    }


    private boolean isVoted() {
        return surveyHandler.checkIfExists(survey.getId()).get(0) == 1;
    }

    
    @Override
    public int getItemCount() {
        return answers.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public String getItem(int position) {
        return answers.get(position);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return 0;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }


}