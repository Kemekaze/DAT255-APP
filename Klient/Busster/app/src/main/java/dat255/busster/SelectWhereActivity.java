package dat255.busster;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import dat255.busster.DB.PreferencesDBHandler;
import dat255.busster.Events.SendDataEvent;
import dat255.busster.Events.StopsEvent;
import dat255.busster.Objects.Preference;
import dat255.busster.Resources.CharacterCountErrorWatcher;
import dat255.busster.Resources.Constants;
import dat255.busster.Services.SocketService;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
/**
 *      SelectWhereActivity is the first activity that shows. It is used to select 
 *      your username and which stop you gonna get off on.
 * 
 */

public class SelectWhereActivity extends AppCompatActivity {
    private final String TAG = "dat255.SelectWhere";
    private String[] busStopsName;
    private String[] busStopsid;
    PreferencesDBHandler preferencesDBHandler;
    
    /**
     *  Sets contentview and starts the socketservice
     * 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_select_where);
        preferencesDBHandler = new PreferencesDBHandler(this,null);
        new Thread(){
            public void run(){
                Intent socketServiceIntent = new Intent(getApplicationContext(),SocketService.class);
                startService(socketServiceIntent);
            }
        }.start();
        Toolbar toolbar = (Toolbar) findViewById(R.id.select_where_toolbar);
        setSupportActionBar(toolbar);
        TextInputLayout usernameBody = (TextInputLayout) findViewById(R.id.username_body);
        usernameBody.getEditText().addTextChangedListener(new CharacterCountErrorWatcher(usernameBody, 0, 12));
        Preference displayname = preferencesDBHandler.getPreference(Constants.DB.PREFERENCES.DISPLAY_NAME);
        usernameBody.getEditText().setText(displayname.get_value());
   }
    /**
     * Handles interaction with menuItems.
     * @param item MenuItem that was selected.
     * @return true if executed correctly, otherwise false.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.continue_to_feed:
                ContinueToMain();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * The function sets the displayname and the busstop you want to get of at
     * and then it starts the mainactivity
     */ 
    public void ContinueToMain(){
        String displayName = ((EditText) findViewById(R.id.username)).getText().toString();
        AutoCompleteTextView destinationACTV = (AutoCompleteTextView) findViewById(R.id.destination);
        String destination="";
        //Store the stopid in the database
        if(busStopsName != null) {
            for (int i = 0; i< busStopsName.length;i++) {
                if (destinationACTV.getText().toString().equals(busStopsName[i])) {
                    destination = busStopsName[i];
                    try {
                        EventBus.getDefault().post(new SendDataEvent(Constants.SocketEvents.SET_USER_STOP,new JSONObject().put("stopid",busStopsid[i])));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        if(displayName.equals(""))displayName = "Anonymous";
        preferencesDBHandler.addPreference(new Preference(Constants.DB.PREFERENCES.DISPLAY_NAME,displayName));
        preferencesDBHandler.addPreference(new Preference(Constants.DB.PREFERENCES.DESTINATION,destination));
        preferencesDBHandler.close();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        this.startActivity(intent);
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
        inflater.inflate(R.menu.select_where_actions, menu);

        return super.onCreateOptionsMenu(menu);
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
     * Unregisters the activity from the EventBus.
     */
    @Override
    protected void onStop() {
        Log.i(TAG,"onStop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    
    /**
     * is used for autocompleting the busstops when the users input the busstop he/she want to get of at.
     * @param event the type of event to be recieved
     */ 
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void recieveStops(StopsEvent event) {
        Log.i(TAG, "recieveStops");
        AutoCompleteTextView destinations = (AutoCompleteTextView) findViewById(R.id.destination);
        busStopsName = new String[event.stops.size()];
        busStopsid = new String[event.stops.size()];
        for(int i = 0; i < event.stops.size();i++){
            busStopsName[i] = event.stops.get(i).getName();
            busStopsid[i] = event.stops.get(i).getStopid();
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, busStopsName);
        destinations.setAdapter(adapter);

    }

}
