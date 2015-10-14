package dat255.busster;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dat255.busster.Adapters.AnswerAdapter;
import dat255.busster.Adapters.FeedAdapter;
import dat255.busster.DB.PreferencesDBHandler;
import dat255.busster.Events.SendDataEvent;
import dat255.busster.Objects.Post;
import dat255.busster.Objects.Survey;
import dat255.busster.R;
import dat255.busster.Resources.Constants;
import de.greenrobot.event.EventBus;

public class SurveyActivity extends AppCompatActivity {

    private final String TAG = "dat255.ServeyActivity";
    private String id = "0";
    private String body;
    private String user;
    private String time;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Survey survey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        id =  getIntent().getStringExtra("id");
        body =  getIntent().getStringExtra("body");
        user =  getIntent().getStringExtra("user");
        time =  getIntent().getStringExtra("time");


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
        ArrayList<String> mArray = new ArrayList<String>();
        mAdapter = new AnswerAdapter(this,mArray,mRecyclerView,survey);
        mRecyclerView.setAdapter(mAdapter);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


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


    public void onClick(View view){

        switch (view.getId()){
            case R.id.resOne:
                break;
            case R.id.resCross:
                break;
            case R.id.resTwo:
                break;
        }

    }

}
