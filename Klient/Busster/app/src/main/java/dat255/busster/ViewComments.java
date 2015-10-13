/*package dat255.busster;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dat255.busster.Adapters.CommentsAdapter;
import dat255.busster.Adapters.FeedAdapter;
import dat255.busster.Events.PostsEvent;
import dat255.busster.Events.SendDataEvent;
import dat255.busster.Events.StatusEvent;
import dat255.busster.Objects.Post;
import dat255.busster.Resources.Constants;
import dat255.busster.Resources.ServerQueries;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class ViewComments extends AppCompatActivity {

    private static final String TAG = "dat255.ViewComments";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_view_comments);

        mRecyclerView = (RecyclerView) findViewById(R.id.comments_feed);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(scrollListener);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<Post.Comment> mArray = new ArrayList<Post.Comment>();
        mAdapter = new CommentsAdapter(mArray, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);


    }
    public RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int firstVisibleItem, visibleItemCount, totalItemCount;

            visibleItemCount = mLayoutManager.getChildCount();
            totalItemCount = mLayoutManager.getItemCount();
            firstVisibleItem = ((LinearLayoutManager)mLayoutManager).findFirstVisibleItemPosition();

            Log.i(TAG,"Loading:"+String.valueOf(loading) );
            Log.i(TAG,"visibleItemCount:"+String.valueOf(visibleItemCount) );
            Log.i(TAG,"totalItemCount:"+String.valueOf(totalItemCount) );
            Log.i(TAG,"firstVisibleItem:"+String.valueOf(firstVisibleItem) );
            if (loading) {
                if ( (visibleItemCount + firstVisibleItem) >= totalItemCount) {
                    loading = false;
                    getMoreComments();
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_comments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStart() {
        Log.i(TAG,"onStart");
        EventBus.getDefault().register(this);
        super.onStart();
    }
    @Override
    protected void onStop() {
        Log.i(TAG,"onStop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    // -------------------

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updatePostsEvent(PostsEvent event) {
        Log.i(TAG, "updatePostsEvent(PostsEvent)");
        //update post list
        FeedAdapter commentsAdapter = (FeedAdapter) mAdapter;
        // commentsAdapter.addComments(event.posts, event.eventType); // comment event???
        loading = true;
       // swipeLayout.setRefreshing(false);
    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void statusEvent(StatusEvent event) {
        Log.e(TAG, "statusEvent");
        Snackbar.make(this.getCurrentFocus(), event.getStatusText(), Snackbar.LENGTH_LONG).show();
    }
    public void getMoreComments(){
        Log.i(TAG, "getMorePosts");
        getPosts(10, mAdapter.getItemCount());
    }

    private void getPosts(int limit, int skip) {
        Log.i(TAG, "getPosts");
        try {
            EventBus.getDefault().post(
                    new SendDataEvent(Constants.SocketEvents.GET_POSTS,
                            ServerQueries.getPosts(new JSONObject(), limit, skip, new JSONObject().put("date", -1))
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void refreshPosts() {
        Log.i(TAG, "refreshPosts");
        getPosts(10, 0);
    }

}*/
