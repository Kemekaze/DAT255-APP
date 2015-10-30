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

/**
 * This activity is for viewing comments from a selected post.
 * It fetches the selected post and its comments from the DataBus
 * and presents the comments in a RecyclerView.
 */
public class ViewCommentsActivity extends AppCompatActivity {
    private final String TAG = "dat255.ViewComments";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private UserPost userPost;

    /**
     * Sets content view to layout activity_view_comments and binds a
     * CommentsAdapter to the RecyclerView.
     * Changes toolbar and its title to display the parent
     * Post information (body, user and timeSince() it was posted).
     */
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


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addCommentActivity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCommentActivity(view);
            }
        });

        Notifyer.setContext(this);
    }

    // scrollListener
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

    /**
     * Gets the relevant post and its comments from the DataBus and
     * sets the head TextFields to the matching posts strings.
     */
    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
        EventBus.getDefault().register(this);
        userPost = (EventBus.getDefault().getStickyEvent(UserPostEvent.class)).userPosts.get(0);
        ((CommentsAdapter)mAdapter).addComments(userPost.getComments(),1);

        // sets the textfields to the corresponding fields from the selected post
        ((TextView) findViewById(R.id.comment_parent_body)).setText(userPost.getBody());
        ((TextView) findViewById(R.id.comment_parent_user)).setText("- "+userPost.getUser());
        ((TextView) findViewById(R.id.comment_parent_time)).setText(userPost.getTimeSince());



    }

    /**
     * Adds new comments to mAdapter from CommentsEvent.
     * @param event CommentsEvent containing new comments.
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updateCommentsEvent(CommentsEvent event) {
        Log.i(TAG, "updateCommentsEvent(CommentsEvent)");
        //update post list
        CommentsAdapter postsAdapter = (CommentsAdapter) mAdapter;
        postsAdapter.addComments(event.comments, event.eventType);
    }

    /**
     * Displays a SnackBar message on the screen.
     * @param event StatusEvent containing the information to be displayed.
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void statusEvent(StatusEvent event) {
        Snackbar.make(this.getCurrentFocus(), event.getStatusText(), Snackbar.LENGTH_LONG).show();
    }

    /**
     * unregisters this activity from the EventBus when stopped.
     */
    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Starts AddCommentActivity to add a new comment
     * to the relevant post.
     * @param view
     */
    public void addCommentActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), AddCommentActivity.class);
        intent.putExtra("postID", userPost.getId());
        this.startActivity(intent);
    }

    /**
     * Handles interaction with menuItems. If home button was pressed
     * finish this activity and go back to previous activity.
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * When android BackButton is pressed this activity
     * will finish and go back to previous activity.
     */
    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }
    @Subscribe
    public void userPostEvent(UserPostEvent userPostEvent) {
        Log.i(TAG, "UserPostEvent");
    }

    /**
     * Returns the adapter used to create views for the comments.
     * @return adapter
     */
    public RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }

    /**
     * Returns RecyclerView containing the views for the comments.
     * @return RecyclerView
     */
    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }
}
