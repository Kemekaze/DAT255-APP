package dat255.busster;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dat255.busster.Adapters.CommentsAdapter;
import dat255.busster.Events.CommentsEvent;
import dat255.busster.Events.SendDataEvent;
import dat255.busster.Events.StatusEvent;
import dat255.busster.Events.UserPostEvent;
import dat255.busster.Objects.Post;
import dat255.busster.Objects.UserPost;
import dat255.busster.Resources.Constants;
import dat255.busster.Resources.Notifyer;
import dat255.busster.Resources.ServerQueries;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class ViewCommentsActivity extends AppCompatActivity {
    private final String TAG = "dat255.ViewComments";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private UserPost userPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        CollapsingToolbarLayout toolbar2 = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbar2.setTitleEnabled(false);
        toolbar2.setTitle("");
        mRecyclerView = (RecyclerView) findViewById(R.id.comments_feed);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(scrollListener);

        ArrayList<Post.Comment> data = new ArrayList<>();
        mAdapter = new CommentsAdapter(data, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCommentActivity(view);
            }
        });

        Notifyer.setContext(this);
    }

    public RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int firstVisibleItem, visibleItemCount, totalItemCount;

            visibleItemCount = mLayoutManager.getChildCount();
            totalItemCount = mLayoutManager.getItemCount();
            firstVisibleItem = ((LinearLayoutManager)mLayoutManager).findFirstVisibleItemPosition();
            Log.i(TAG,"dy: "+dy);
            if (loading) {
                if ( (visibleItemCount + firstVisibleItem) >= totalItemCount) {
                    loading = false;
                }
            }
        }
    };
    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
        EventBus.getDefault().register(this);
        userPost = (EventBus.getDefault().getStickyEvent(UserPostEvent.class)).userPosts.get(0);
        
        ((CommentsAdapter)mAdapter).addComments(userPost.getComments(),1);

        ((TextView) findViewById(R.id.comment_parent_body)).setText(userPost.getBody());
        ((TextView) findViewById(R.id.comment_parent_user)).setText("- "+userPost.getUser());
        ((TextView) findViewById(R.id.comment_parent_time)).setText(userPost.getTimeSince());



    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updateCommentsEvent(CommentsEvent event) {
        Log.i(TAG, "updateCommentsEvent(CommentsEvent)");
        //update post list
        CommentsAdapter postsAdapter = (CommentsAdapter) mAdapter;
        postsAdapter.addComments(event.comments, event.eventType);
    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void statusEvent(StatusEvent event) {
        Snackbar.make(this.getCurrentFocus(), event.getStatusText(), Snackbar.LENGTH_LONG).show();
    }
    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    public void getMoreComments(){
        Log.i(TAG, "getMorePosts");
        getComments(10, mAdapter.getItemCount());
    }


    private void getComments(int limit, int skip) {
        Log.i(TAG, "getPosts");
        try {
            EventBus.getDefault().post(
                    new SendDataEvent(Constants.SocketEvents.GET_COMMENTS,
                            ServerQueries.getPosts(new JSONObject(), limit, skip, new JSONObject().put("date", -1))
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void refreshPosts() {
        Log.i(TAG, "refreshPosts");
        getComments(10, 0);
    }
    public void addCommentActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), AddCommentActivity.class);
        intent.putExtra("postID", userPost.getId());
        this.startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }
    @Subscribe
    public void userPostEvent(UserPostEvent userPostEvent) {
        Log.i(TAG, "UserPostEvent");
    }
}
