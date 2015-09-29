package elias.testdbconnection;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import elias.testdbconnection.Objects.Post;
import elias.testdbconnection.Resources.ServerQueries;
import elias.testdbconnection.SocketService.SocketServiceBinder;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener{
    private static final String TAG = "App.Main";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private String[] planets;
    private ActionBarDrawerToggle drawerToggle;


    protected SocketService socketService;
    boolean isBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread() {
            public void run() {
                Log.i(TAG,"thread");
                getApplicationContext().bindService(
                        new Intent(getApplicationContext(), SocketService.class),
                        socketConnection,
                        Context.BIND_AUTO_CREATE
                );

            }
        }.start();

       // DrawerLayout section start
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        planets = getResources().getStringArray(R.array.planets);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        drawerListView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, planets));
        drawerListView.setOnItemClickListener(this);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.drawable.ic_drawer,
                R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
            }


        };
        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        // DrawerLayout section end



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

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        refreshPosts();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration configuration){
     super.onConfigurationChanged(configuration);
        drawerToggle.onConfigurationChanged(configuration);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
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
    public void refreshPosts(){
        Log.i(TAG, "refreshPosts");
        Log.i(TAG, Thread.currentThread().toString());
        ListView lw = (ListView) findViewById(R.id.posts);
        try {
            JSONArray query = ServerQueries.query(new JSONObject(),10,0,new JSONObject().putOpt("date", -1));
            socketService.getSocket().emit("get posts",query);
        } catch (JSONException e) {
            socketService.getSocket().emit("get posts",new JSONArray());
        }

    }

    @Subscribe
     public void onEventMainThread(List<Post> posts){

        Log.i(TAG, "onEventMainThread(Post[])");
        Log.i(TAG, Thread.currentThread().toString());
        ListView lw = (ListView) findViewById(R.id.posts);
        PostsAdapter adapter = (PostsAdapter)lw.getAdapter();
        adapter.addPosts(posts);
    }
    private ServiceConnection socketConnection = new ServiceConnection() {
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
    };


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, planets[position] + "was selected", Toast.LENGTH_LONG).show();
        selectItem(position);
    }

    private void selectItem(int position) {
        drawerListView.setItemChecked(position,true);
        setTitle(planets[position]);
    }

    public void setTitle(String title){
       getActionBar().setTitle(title);
    }
}
