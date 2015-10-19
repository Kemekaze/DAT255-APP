package dat255.busster;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new Thread(){
            public void run(){
                Intent socketServiceIntent = new Intent(getApplicationContext(),SocketService.class);
                startService(socketServiceIntent);
            }
        }.start();

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
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

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
    @Override
    protected void onStart() {
        Log.i(TAG,"onStart");
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new SendDataEvent(Constants.SocketEvents.GET_POSTS));
        super.onStart();
    }
    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent = dat255.busster.Menu.Menu.getNavigationItem(this,item);
        startActivity(intent);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updatePostsEvent(PostsEvent event) {
        Log.i(TAG, "updatePostsEvent(PostsEvent)");
        //update post list
        FeedAdapter postsAdapter = (FeedAdapter) mAdapter;
        postsAdapter.addPosts(event.posts, event.eventType);
        loading = true;
        swipeLayout.setRefreshing(false);
    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void statusEvent(StatusEvent event) {
        Snackbar.make(this.getCurrentFocus(), event.getStatusText(), Snackbar.LENGTH_LONG).show();
    }
    public void getMorePosts(){
        Log.i(TAG, "getMorePosts");
        getPosts(filter(), 10, mAdapter.getItemCount());
    }
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
        getPosts(filter(),10, 0);
    }
    public void addPostActivity(View view){
        Intent intent = new Intent(getApplicationContext(), AddPostActivity.class);
        this.startActivity(intent);
    }


}
