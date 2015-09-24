package elias.testdbconnection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


public class MainActivity extends Activity {
    private static final String TAG = "App.Main";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,SocketService.class);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe
    public void onEvent(MessageEvent event){
        Log.i(TAG,"Event recieved");

        JSONArray arr = event.message;
        Log.i(TAG,"Array size: "+arr.length());
        ListView lw = (ListView) findViewById(R.id.posts);
        ListAdapter adapter= new CustomAdapter(this,arr);
        lw.setAdapter(adapter);
        lw.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

}
