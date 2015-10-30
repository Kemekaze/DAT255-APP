package dat255.busster;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dat255.busster.Adapters.FeedAdapter;
import dat255.busster.DB.PreferencesDBHandler;
import dat255.busster.Events.CommentsEvent;
import dat255.busster.Events.PostsEvent;
import dat255.busster.Events.SendDataEvent;
import dat255.busster.Events.StatusEvent;
import dat255.busster.Objects.Post;
import dat255.busster.Resources.Constants;
import dat255.busster.Resources.Notifyer;
import dat255.busster.Resources.ServerQueries;
import dat255.busster.Services.SocketService;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * This Activity is the main Activity of the application.
 * It fetches the posts and surveys from the server and
 * displays them to the screen. It also has a menu drawer
 * where users can jump between specific Activities.
 */
public class MainActivity extends AppCompatActivity{
    private final String TAG = "dat255.MainActivity";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout swipeLayout;

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;

    private int menuFilterSelected=1;
    PreferencesDBHandler preferencesDBHandler;

    /**
     * Setting the start values of the feed and the layout of activity.
     * @param savedInstanceState state of the application.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        setContentView(R.layout.activity_main);
        preferencesDBHandler = new PreferencesDBHandler(this,null);
        // set Display name in top
        setTitle(preferencesDBHandler.getPreference(Constants.DB.PREFERENCES.DISPLAY_NAME).get_value());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!isMyServiceRunning(SocketService.class)){
            new Thread() {
                public void run() {
                    Intent socketServiceIntent = new Intent(getApplicationContext(), SocketService.class);
                    startService(socketServiceIntent);
                }
            }.start();
        }

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_feed_container);
        swipeLayout.setOnRefreshListener(refreshListener);
        swipeLayout.setColorSchemeResources(R.color.orange_600, R.color.green_600, R.color.blue_600, R.color.red_600);
        mRecyclerView = (RecyclerView) findViewById(R.id.post_feed);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(scrollListener);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        ArrayList<Post> mArray = new ArrayList<Post>();
        mAdapter = new FeedAdapter(this,mArray,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        Notifyer.setContext(this);

        EventBus.getDefault().post(new SendDataEvent(Constants.SocketEvents.GET_POSTS));
    }


    public SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshPosts();
        }

    };


    public RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int firstVisibleItem, visibleItemCount, totalItemCount;

            visibleItemCount = mLayoutManager.getChildCount();
            totalItemCount = mLayoutManager.getItemCount();
            firstVisibleItem = ((LinearLayoutManager)mLayoutManager).findFirstVisibleItemPosition();

            if (loading) {
                if ( (visibleItemCount + firstVisibleItem) >= totalItemCount) {
                    loading = false;
                    getMorePosts();
                }
            }
        }
    };

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * When a MenuItem is a selected, performs actions based
     * on its id.
     * @param item item that was selected
     * @return true if successful, else false.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_menu_filter_1) {
            if(item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);
            menuFilterSelected = id;
            getPosts(filter(),10,0);
            return true;
        }else if (id == R.id.main_menu_filter_2) {
            if(item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);
            menuFilterSelected = id;
            getPosts(filter(),10,0);
            return true;
        }else if (id == R.id.main_menu_filter_3) {
            if(item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);
            menuFilterSelected = id;
            getPosts(filter(),10,0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Registers this activity to the EventBus when started.
     */
    @Override
    protected void onStart() {
        Log.i(TAG,"onStart");
        EventBus.getDefault().register(this);
        PostsEvent pE= (EventBus.getDefault().getStickyEvent(PostsEvent.class));
        if(pE != null){
            updatePostsEvent(pE);
        }

        StatusEvent sE= (EventBus.getDefault().getStickyEvent(StatusEvent.class));
        if(sE != null){
            statusEvent(sE);
        }

        refreshPosts();
        super.onStart();
    }

    /**
     * Unregisters this activity from the EventBus when stopped.
     */
    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updatePostsEvent(PostsEvent event) {
        Log.i(TAG, "updatePostsEvent(PostsEvent)");
        //update post list
        FeedAdapter postsAdapter = (FeedAdapter) mAdapter;
        postsAdapter.addPosts(event.posts, event.eventType);
        loading = true;
        swipeLayout.setRefreshing(false);
    }


    /**
     * Posts a SnackBar message with information
     * from the passed event.
     * @param event StatusEvent containing information to be displayed
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void statusEvent(StatusEvent event) {
        Snackbar.make(this.getCurrentFocus(), event.getStatusText(), Snackbar.LENGTH_LONG).show();
    }


    /**
     * Gets more posts
     */
    public void getMorePosts(){
        Log.i(TAG, "getMorePosts");
        getPosts(filter(), 10, mAdapter.getItemCount());
    }


    /**
     * Gets posts
     * @param query
     * @param limit
     * @param skip
     */
    private void getPosts(JSONObject query,int limit, int skip) {
        Log.i(TAG, "getPosts");
        try {
            EventBus.getDefault().post(
                    new SendDataEvent(Constants.SocketEvents.GET_POSTS,
                            ServerQueries.getPosts(query, limit, skip, new JSONObject().put("date", -1))
                    )
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Checks if the user is filtering certain posts and
     * returns a JSONObject with that information.
     * @return JSONObject containing filter information.
     */
    private JSONObject filter(){
        try {
            if (menuFilterSelected == R.id.main_menu_filter_2) {
                return new JSONObject().put("meta.type", "post");
            } else if (menuFilterSelected == R.id.main_menu_filter_3) {
                return new JSONObject().put("meta.type", "survey");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //filter main_menu_filter_2 (no filter)
        return new JSONObject();
    }
    public void refreshPosts() {
        Log.i(TAG, "refreshPosts");
        getPosts(filter(), 10, 0);
    }

    /**
     * Starts AddPostActivity. Called from onClick() of
     * floatingActionButton.
     * @param view View that was pressed.
     */
    public void addPostActivity(View view){
        Intent intent = new Intent(getApplicationContext(), AddPostActivity.class);
        this.startActivity(intent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public RecyclerView.Adapter getmAdapter() {
        return mAdapter;
    }

    public RecyclerView getmRecyclerView() {
        return mRecyclerView;
    }

    public SwipeRefreshLayout.OnRefreshListener getRefreshListener() {
        return refreshListener;
    }
}
