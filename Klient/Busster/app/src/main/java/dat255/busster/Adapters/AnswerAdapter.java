package dat255.busster.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import dat255.busster.DB.SurveyDBHandler;
import dat255.busster.Events.SendDataEvent;
import dat255.busster.Objects.Survey;
import dat255.busster.R;
import dat255.busster.Resources.Constants;
import de.greenrobot.event.EventBus;

/**
 * Created by Rasmus on 2015-10-14.
 */
public class AnswerAdapter extends RecyclerSwipeAdapter<AnswerAdapter.ViewHolder> {
    private  List<String> answers;
    private  Context context;
    private  RecyclerView recyclerView;
    public Survey survey;
    private SurveyDBHandler surveyHandler;

    @Override
    public AnswerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case

        public TextView id;
        public TextView answer;

        public View view;


        public ViewHolder(View view) {
            super(view);

            answer = (TextView) view.findViewById(R.id.answer);

            this.view = view;
        }
    }

    public AnswerAdapter(Context context, List<String> answers, RecyclerView mRecyclerView) {
        this.context = context;
        this.answers = answers;
        this.recyclerView = mRecyclerView;
        surveyHandler = new SurveyDBHandler(context,null);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildAdapterPosition(v);
            Log.i("Survey", "onClick : " + position);
            String answer = getItem(position);
            //survey.addResult(position);

            if(surveyHandler.checkIfExists(survey.getId()).get(0) == 0) {
                JSONObject query = new JSONObject();
                try {
                    query.put("option", position+1);
                    query.put("post_id", survey.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                EventBus.getDefault().post(new SendDataEvent(Constants.SocketEvents.UPDATE_SURVEY, query));


                Log.i("Survey", survey.getCount() + "");
            }

        }
    };

    @Override
    public void onBindViewHolder(AnswerAdapter.ViewHolder viewHolder, int position) {
        viewHolder.answer.setText(getItem(position));
        viewHolder.view.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public String getItem(int position){
        return answers.get(position);
    }

    public List<String> getAnswers() {
        return answers;
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return 0;
    }

    public void setSurvey(Survey survey){
        this.survey = survey;
    }
}
