package dat255.app.buzzter;

import android.app.Activity;

/**
 * Created by ido on 30/09/15.
 */




import android.app.Activity;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import dat255.app.buzzter.Adapters.PostsAdapter;
import dat255.app.buzzter.Events.PostsEvent;
import dat255.app.buzzter.Events.SavePostEvent;
import dat255.app.buzzter.Events.SendDataEvent;
import dat255.app.buzzter.Events.StatusEvent;
import dat255.app.buzzter.Objects.Post;
import dat255.app.buzzter.Resources.Constants;
import dat255.app.buzzter.Resources.ServerQueries;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class AddPost extends Activity {
    private final String TAG = "dat255.app.buzzter.AP";
    private EditText edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);


    }



    public void getEditText(View v) {
        EditText edittext = (EditText)findViewById(R.id.editText);
        Log.e(TAG, edittext.getText().toString());
        EventBus.getDefault().post(new SendDataEvent(Constants.SocketEvents.SAVE_POST, ServerQueries.query("body",edittext.getText().toString())));
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        EventBus.getDefault().register(this);
        super.onStart();
    }


    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    //Eventbus events
    @Subscribe
    public void savePostStatus(SavePostEvent event){
        Log.i(TAG, "savePostStatus");
        if(event.getStatus().equals("ok")){
            Log.i(TAG, "savePostStatus");
            EventBus.getDefault().postSticky(new StatusEvent("Post saved!"));
            finish();
        }
        //BEhandla error för fel.
        else Log.e(TAG, "PostSaveError");
    }


}




