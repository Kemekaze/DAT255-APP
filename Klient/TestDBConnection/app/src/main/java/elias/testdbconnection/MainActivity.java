package elias.testdbconnection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


import elias.testdbconnection.Objects.Post;
import elias.testdbconnection.Resources.ServerQueries;
import elias.testdbconnection.events.PostsEvent;




public class MainActivity extends Activity {
    private static final String TAG = "App.Main";

    //protected SocketService socketService;
    boolean isBound = false;
    EventBus eventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread() {
            public void run() {
                Intent i = new Intent(getApplicationContext(),SocketService.class);
                startService(i);
            }
        }.start();
        /*new Thread() {
            public void run() {
                getApplicationContext().bindService(
                        new Intent(getApplicationContext(), SocketService.class),
                        socketConnection,
                        Context.BIND_AUTO_CREATE
                );

            }
        }.start();*/

        ListView lw = (ListView) findViewById(R.id.posts);
        lw.setAdapter(
                new PostsAdapter(
                        this,
                        new ArrayList<Post>()
                )
        );
        lw.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Post p = (Post) parent.getItemAtPosition(position);
                        Toast.makeText(MainActivity.this, p.getId(), Toast.LENGTH_SHORT).show();
                    }
                }
        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        eventBus.register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        eventBus.unregister(this);
        super.onStop();
    }
    public void refreshPosts(View view){
        Log.i(TAG, "refreshPosts");
        Log.i(TAG, String.valueOf(Thread.currentThread().getId()));
        ListView lw = (ListView) findViewById(R.id.posts);
        try {
            JSONArray query = ServerQueries.query(new JSONObject(),10,0,new JSONObject().putOpt("date", -1));
            SocketService.send("getPosts", query);
            //socketService.getSocket().emit("getPosts",query);
        } catch (JSONException e) {
            //socketService.getSocket().emit("getPosts",new JSONArray());
        }

    }
    @Subscribe
     public void onEventMainThread(PostsEvent posts){

        Log.i(TAG, "onEventMainThread(Post[])");
        Log.i(TAG, String.valueOf(Thread.currentThread().getId()));
        ListView lw = (ListView) findViewById(R.id.posts);
        PostsAdapter adapter = (PostsAdapter)lw.getAdapter();
        adapter.addPosts(posts.posts);
    }
    /*private ServiceConnection socketConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SocketServiceBinder binder = (SocketServiceBinder) service;
            socketService = binder.getService();

            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;

        }
    };*/


}
