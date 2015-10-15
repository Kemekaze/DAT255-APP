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

import dat255.busster.DB.PreferencesDBHandler;
import dat255.busster.Events.StopsEvent;
import dat255.busster.Objects.Preference;
import dat255.busster.Resources.CharacterCountErrorWatcher;
import dat255.busster.Resources.Constants;
import dat255.busster.Services.SocketService;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class SelectWhereActivity extends AppCompatActivity {
    private final String TAG = "dat255.SelectWhere";
    private String[] busStops;
    PreferencesDBHandler preferencesDBHandler;

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
    public void ContinueToMain(){
        String displayName = ((EditText) findViewById(R.id.username)).getText().toString();
        AutoCompleteTextView destinationACTV = (AutoCompleteTextView) findViewById(R.id.destination);
        String destination="";
        if(busStops != null) {
            for (String stop : busStops) {
                if (destinationACTV.getText().toString().equals(stop)) {
                    destination = stop;
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.select_where_actions, menu);

        return super.onCreateOptionsMenu(menu);
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
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void recieveStops(StopsEvent event) {
        Log.i(TAG, "recieveStops");
        AutoCompleteTextView destinations = (AutoCompleteTextView) findViewById(R.id.destination);
        busStops = new String[event.stops.size()];
        for(int i = 0; i < event.stops.size();i++){
            busStops[i] = event.stops.get(i).getName();
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, busStops);
        destinations.setAdapter(adapter);

    }

}
