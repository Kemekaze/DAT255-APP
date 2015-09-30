package dat255.app.buzzter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import dat255.app.buzzter.Adapters.PostsAdapter;
import dat255.app.buzzter.Events.PostsEvent;
import dat255.app.buzzter.Events.SendDataEvent;
import dat255.app.buzzter.Events.StatusEvent;
import dat255.app.buzzter.Objects.Post;
import dat255.app.buzzter.Resources.Constants;
import dat255.app.buzzter.Resources.ServerQueries;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MainActivity extends Activity {
    private final String TAG = "dat255.app.buzzter.Main";
    private Intent socketServiceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(){
            public void run(){
                socketServiceIntent = new Intent(getApplicationContext(),SocketService.class);
                startService(socketServiceIntent);
            }
        }.start();

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


    public void savePost(View v) {
        Intent myIntent = new Intent(MainActivity.this, AddPost.class);
        MainActivity.this.startActivity(myIntent);
    }



    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        EventBus.getDefault().register(this);
     StatusEvent e = EventBus.getDefault().getStickyEvent(StatusEvent.class);
        if(e != null)statusEvent(e);
        super.onStart();

    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        EventBus.getDefault().unregister(this);
       // stopService(socketServiceIntent);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updatePostsEvent(PostsEvent event){
        Log.i(TAG, "updatePostsEvent(PostsEvent)");
        //update post list
        ListView lw = (ListView) findViewById(R.id.posts);
        PostsAdapter adapter = (PostsAdapter)lw.getAdapter();
        adapter.addPosts(event.posts);
    }

    public void getPosts(View view){
        Log.i(TAG, "refreshPosts");
        ListView lw = (ListView) findViewById(R.id.posts);
        int limit = 10;
        int skip = lw.getAdapter().getCount();
        Log.i(TAG, String.valueOf(skip));
        EventBus.getDefault().post(
                new SendDataEvent(Constants.SocketEvents.GET_POSTS,
                        ServerQueries.getPosts(new JSONObject(), limit, skip, new JSONObject())
                )
        );

    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void statusEvent(StatusEvent event){
        Log.e(TAG, "statusEvent");
        Toast.makeText(this.getApplicationContext(), event.getStatusText(), Toast.LENGTH_SHORT).show();

    }

}
