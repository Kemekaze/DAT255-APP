package dat255.busster.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import dat255.busster.Objects.Post;
import dat255.busster.Objects.Survey;
import dat255.busster.Objects.UserPost;
import dat255.busster.R;
import dat255.busster.SurveyActivity;

/**
 * Created by Rasmus on 2015-10-14.
 */
public class AnswerAdapter extends RecyclerSwipeAdapter<AnswerAdapter.ViewHolder> {
    private  List<String> answers;
    private  Context context;
    private  RecyclerView recyclerView;
    public Survey survey;

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
            id = (TextView) view.findViewById(R.id.answerId);
            answer = (TextView) view.findViewById(R.id.swipe);

            this.view = view;
        }
    }

    public AnswerAdapter(Context context, List<String> answers, RecyclerView mRecyclerView,Survey survey) {
        this.context = context;
        this.answers = answers;
        this.recyclerView = mRecyclerView;
        this.survey = survey;

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildAdapterPosition(v);
           // Log.i(TAG, "onClick : " + position);
            String answer = getItem(position);
            survey.addResult(answer);

        }
    };

    @Override
    public void onBindViewHolder(AnswerAdapter.ViewHolder viewHolder, int position) {
        final int pos = position;

        viewHolder.id.setText(position+"");
        viewHolder.answer.setText(getItem(position));
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
}
