package dat255.busster;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dat255.busster.Adapters.CommentsAdapter;
import dat255.busster.Events.SendDataEvent;
import dat255.busster.Objects.Post;
import dat255.busster.Resources.Constants;
import dat255.busster.Resources.ServerQueries;
import de.greenrobot.event.EventBus;

public class ViewCommentsActivity extends AppCompatActivity {
    private final String TAG = "dat255.ViewComments";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout parentPostLayout;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private TextView body;
    private TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        body = (TextView) parentPostLayout.findViewById(R.id.body);
        user = (TextView) parentPostLayout.findViewById(R.id.user);
        body.setText(getIntent().getStringExtra("body"));
        user.setText(getIntent().getStringExtra("user"));
        parentPostLayout = (LinearLayout) mRecyclerView.findViewById(R.id.parent_post);

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
    }
    public RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int firstVisibleItem, visibleItemCount, totalItemCount;

            visibleItemCount = mLayoutManager.getChildCount();
            totalItemCount = mLayoutManager.getItemCount();
            firstVisibleItem = ((LinearLayoutManager)mLayoutManager).findFirstVisibleItemPosition();

            Log.i(TAG, "Loading:" + String.valueOf(loading));
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
    public void addCommentActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), AddCommentActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }
}
