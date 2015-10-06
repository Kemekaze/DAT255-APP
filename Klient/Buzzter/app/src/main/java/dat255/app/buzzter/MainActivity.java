package dat255.app.buzzter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
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


public class MainActivity extends AppCompatActivity {
    private final String TAG = "dat255.app.buzzter.Main";
    private Intent socketServiceIntent;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
        mRecyclerView = (RecyclerView) findViewById(R.id.comments);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PostsAdapter(new ArrayList<Post>());
        mRecyclerView.setAdapter(mAdapter);
    }
    private void voteUp(Post p){
        JSONObject data = new JSONObject();
        try {
            data.put("post_id",p.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SendDataEvent ev = new SendDataEvent(Constants.SocketEvents.VOTE_UP,data);
        EventBus.getDefault().post(ev);
    }

    public void addPost(View v) {
        Intent myIntent = new Intent(MainActivity.this, AddPost.class);
        MainActivity.this.startActivity(myIntent);
    }



    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        EventBus.getDefault().register(this);
        StatusEvent status = EventBus.getDefault().getStickyEvent(StatusEvent.class);
        PostsEvent posts = EventBus.getDefault().getStickyEvent(PostsEvent.class);
        if (status != null)statusEvent(status);
        if(posts != null)updatePostsEvent(posts);
        super.onStart();

    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        EventBus.getDefault().unregister(this);
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
        PostsAdapter postsAdapter = (PostsAdapter) mAdapter;
        postsAdapter.addPosts(event.posts,event.eventType);
    }

    public void getMorePosts(View view){
        Log.i(TAG, "getMorePosts");
        getPosts(view, 10, mAdapter.getItemCount());
    }
    private void getPosts(View view, int limit, int skip){
        Log.i(TAG, String.valueOf(skip));

        try {
            EventBus.getDefault().post(
                    new SendDataEvent(Constants.SocketEvents.GET_POSTS,
                            ServerQueries.getPosts(new JSONObject(), limit, skip, new JSONObject().put("date",-1))
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void refreshPosts(View view){
        Log.i(TAG, "refreshPosts");
        getPosts(view,10,0);
    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void statusEvent(StatusEvent event){
        Log.e(TAG, "statusEvent");
        Toast.makeText(this.getApplicationContext(), event.getStatusText(), Toast.LENGTH_SHORT).show();

    }

}
