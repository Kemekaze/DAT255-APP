

package dat255.busster;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import dat255.busster.Events.SavePostEvent;
import dat255.busster.Events.SendDataEvent;
import dat255.busster.Events.StatusEvent;
import dat255.busster.Resources.CharacterCountErrorWatcher;
import dat255.busster.Resources.Constants;
import dat255.busster.Resources.ServerQueries;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

import static dat255.busster.R.id.result_toolbar;


/**
 * Created by ido on 13/10/15.
 */
public class ShowResult extends AppCompatActivity  {
    private final String TAG = "dat255.ShowResult";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_shcdow_result);
        Toolbar toolbar = (Toolbar) findViewById(result_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        TextInputLayout body = (TextInputLayout) findViewById(R.id.post_body);
        //body.getEditText().addTextChangedListener(new CharacterCountErrorWatcher(body, 3, 180));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show_result_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.send_post_action:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        EventBus.getDefault().register(this);
        super.onStart();
    }
    @Override
    protected void onStop() {
        Log.i(TAG,"onStop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    //Eventbus events
    @Subscribe
    public void savePostStatus(SavePostEvent event){
        Log.i(TAG, "savePostStatus");
        if(event.getStatus().equals("ok")){
            EventBus.getDefault().postSticky(new StatusEvent("Post saved!"));
            this.finish();
        }
        //BEhandla error för fel.
        else Log.e(TAG, "PostSaveError");
    }



}
