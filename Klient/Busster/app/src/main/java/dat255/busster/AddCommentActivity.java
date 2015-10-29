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
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * This activity is for adding comments to a post. The user
 * can write text in a textInputField and the text is then
 * packed into a JSONObject and sent over the dataBus to be sent
 * to the server by the SocketService.
 */
public class AddCommentActivity extends AppCompatActivity {
    private final String TAG = "dat255.AddComment";

    PreferencesDBHandler preferencesDBHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_add_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextInputLayout body = (TextInputLayout) findViewById(R.id.comment_body_edit);
        body.getEditText().addTextChangedListener(new CharacterCountErrorWatcher(body, 3, 180));
        preferencesDBHandler  = new PreferencesDBHandler(this,null);
    }

    /**
     * Called when optionsMenu is created. Decompresses the
     * data passed in.
     * @param menu the data to be inflated.
     * @return true if successful, else false.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_comment_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handles interaction with menuItems.
     * @param item MenuItem that was selected.
     * @return true if executed correctly, otherwise false.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.send_comment_action:
                saveComment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Registers the activity to the EventBus.
     */
    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        EventBus.getDefault().register(this);
        super.onStart();
    }

    /**
     * Uregisters the activity from the EventBus.
     */
    @Override
    protected void onStop() {
        Log.i(TAG,"onStop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Saves the body of the TextInputField together with username
     * and a post id to a JSONObject and sends it to the DataBus.
     */
    public void saveComment() {
        Log.i(TAG,"saveComment");
        TextInputLayout body = (TextInputLayout) findViewById(R.id.comment_body_edit);
        try {
            JSONObject query = new JSONObject();
            query.put("post_id", getIntent().getStringExtra("postID"));
            query.put("body", body.getEditText().getText().toString());
            query.put("user", preferencesDBHandler.getPreference(Constants.DB.PREFERENCES.DISPLAY_NAME).get_value());

            EventBus.getDefault().post(new SendDataEvent(Constants.SocketEvents.SAVE_COMMENT, query));
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a StatusEvent to the EventBus if comment was
     * successfully saved.
     * @param event SavePostEvent 
     */
    @Subscribe
    public void saveCommentStatus(SavePostEvent event){
        Log.i(TAG, "saveCommentStatus");
        if(event.getStatus().equals("ok")){
            EventBus.getDefault().postSticky(new StatusEvent("Comment saved!"));
            this.finish();
        }
        //Handle error
        else Log.e(TAG, "PostSaveError");
    }

}
