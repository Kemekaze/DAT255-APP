package dat255.busster;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import dat255.busster.DB.PreferencesDBHandler;
import dat255.busster.Events.SavePostEvent;
import dat255.busster.Events.SendDataEvent;
import dat255.busster.Events.StatusEvent;
import dat255.busster.Resources.CharacterCountErrorWatcher;
import dat255.busster.Resources.Constants;
import dat255.busster.Resources.Notifyer;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * This activity is for adding a new post to the feed. It lets the
 * user input text to a TextInputLayout and when finished packs
 * the body, user and timeSince data into a JSONObject and
 * sends it over the DataBus to be sent to the server by
 * SocketService.
 */
public class AddPostActivity extends AppCompatActivity {
    
    private final String TAG = "dat255.AddPostActivity";
    PreferencesDBHandler preferencesDBHandler;


    /**
     * Sets content view, sets action bar parameters and
     * initializes TextInputLayout and adds TextChangedListener
     * to it.
     * @param savedInstanceState containing application state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_add_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        TextInputLayout body = (TextInputLayout) findViewById(R.id.post_body);
        body.getEditText().addTextChangedListener(new CharacterCountErrorWatcher(body, 3, 180));

        Notifyer.setContext(this);
        preferencesDBHandler = new PreferencesDBHandler(this,null);

    }


    /**
     * Registers this activity to the EventBus when started.
     */
    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        EventBus.getDefault().register(this);
        super.onStart();
    }

    /**
     * Unregisters this activity from the EventBus when stopped.
     */
    @Override
    protected void onStop() {
        Log.i(TAG,"onStop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Inflates the menu items for use in the action bar.
     * @param menu item to be inflated
     * @return true if successful, false if unsuccessful
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_post_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Performs actions based on a selected MenuItem.
     * @param item selected MenuItem
     * @return true of successful, false if unsuccessful.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.send_post_action:
                savePost();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Saves the body of the TextInputLayout together with username and
     * current time to a JSONObject and posts it to the EventBus.
     */
    public void savePost() {
        Log.i(TAG,"savePost");
        TextInputLayout body = (TextInputLayout) findViewById(R.id.post_body);

        // set Display name in top
        try {
            JSONObject query = new JSONObject();
            query.put("body", body.getEditText().getText().toString());
            query.put("user", preferencesDBHandler.getPreference(Constants.DB.PREFERENCES.DISPLAY_NAME).get_value());

            EventBus.getDefault().post(new SendDataEvent(Constants.SocketEvents.SAVE_POST, query));
        }catch (JSONException e) {
            e.printStackTrace();
        }
        preferencesDBHandler.close();
    }

    /**
     * Checks SavePostEvent if it was successful and posts
     * a sticky Event.
     * @param event
     */
    @Subscribe
    public void savePostStatus(SavePostEvent event){
        Log.i(TAG, "savePostStatus");
        if(event.getStatus().equals("ok")){
            EventBus.getDefault().postSticky(new StatusEvent("Post saved!"));
            this.finish();
        }
        // handle errors.
        else Log.e(TAG, "PostSaveError");
    }
}
