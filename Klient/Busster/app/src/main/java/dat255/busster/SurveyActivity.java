package dat255.busster;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dat255.busster.Adapters.AnswerAdapter;
import dat255.busster.Events.SurveyEvent;
import dat255.busster.Objects.Survey;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

public class SurveyActivity extends AppCompatActivity {

    private final String TAG = "dat255.SurveyActivity";
    private String id = "0";
    private String body;
    private String user;
    private String time;
    private ArrayList<String> mArray;
    private int count;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Survey survey;
    private AlertDialog.Builder dialogBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Survey survey = getIntent().getParcelableExtra("survey");
        body = getIntent().getStringExtra("Body");
        user = getIntent().getStringExtra("User");
        time = getIntent().getStringExtra("Time");
        count = getIntent().getIntExtra("Count", 0);
        ArrayList<String> mArray = getIntent().getStringArrayListExtra("Answers");
        TextView bodyView = (TextView) findViewById(R.id.body);
        TextView userView = (TextView) findViewById(R.id.user);
        TextView timeView = (TextView) findViewById(R.id.time);

        bodyView.setText(body);
        userView.setText(user);
        timeView.setText(time);

        mRecyclerView = (RecyclerView) findViewById(R.id.post_feed);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AnswerAdapter(this, mArray, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        EventBus.getDefault().register(this);
        List<Survey> surveyEvents = (EventBus.getDefault().getStickyEvent(SurveyEvent.class)).surveys;
        survey = surveyEvents.get(0);
        ((AnswerAdapter) mAdapter).setSurvey(survey);

        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void surveyEvent(SurveyEvent surveyEvent) {
        Log.i(TAG, "SurveyEventDone");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public void resultDialog(Survey sur) {


        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);


        View v = getLayoutInflater().inflate(R.layout.dialog_result, null);
        dialogBuilder.setView(v);

        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_result, null));

        ArrayList<String> strs = new ArrayList<String>();
        for(int i = 0; i< sur.getOptions(); i++) {
            strs.add( sur.getCount(i) + " röstade på " + sur.getAlternatives().get(i));

        }

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs);
        ListView lw = (ListView) v.findViewById(R.id.listResultView);

        lw.setAdapter(listAdapter);


        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        dialogBuilder.create();
        dialogBuilder.show();

    }



}
