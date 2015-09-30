package dat255.app.buzzter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dat255.app.buzzter.Resources.ServerQueries;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import elias.testdbconnection.Objects.Post;
import elias.testdbconnection.Resources.ServerQueries;


public class PostFragment extends Fragment {

    private static final String TAG = "App.Main";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView lw;

    protected SocketService socketService;
    boolean isBound = false;



    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread() {
            public void run() {
                Log.i(TAG, "thread " + getActivity().getComponentName());
                getActivity().getApplicationContext().bindService(
                        new Intent(getActivity().getApplicationContext(), SocketService.class),
                        socketConnection,
                        Context.BIND_AUTO_CREATE
                );


            }
        }.start();
        Log.i(TAG, "started");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_post, container, false);

        lw = (ListView) rootView.findViewById(R.id.posts);
        lw.setAdapter(
                new PostsAdapter(
                        getActivity(),
                        new ArrayList<Post>()
                )
        );
        lw.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Post p = (Post) parent.getItemAtPosition(position);
                        Toast.makeText(getActivity(), p.getId(), Toast.LENGTH_SHORT).show();
                    }
                }
        );


        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);

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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    public void refreshPosts(){
        Log.i(TAG, "refreshPosts");
        Log.i(TAG, Thread.currentThread().toString());
        //ListView lw = (ListView) getView().findViewById(R.id.posts);
        try {
            JSONArray query = ServerQueries.query(new JSONObject(), 10, 0, new JSONObject().putOpt("date", -1));
            socketService.getSocket().emit("get posts",query);
        } catch (JSONException e) {
            socketService.getSocket().emit("get posts",new JSONArray());
        }

    }

    @Subscribe
    public void onEventMainThread(List<Post> posts){

        Log.i(TAG, "onEventMainThread(Post[])");
        Log.i(TAG, Thread.currentThread().toString());
        //ListView lw = (ListView) getView().findViewById(R.id.posts);
        PostsAdapter adapter = (PostsAdapter)lw.getAdapter();
        adapter.addPosts(posts);
    }

    private ServiceConnection socketConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SocketService.SocketServiceBinder binder = (SocketService.SocketServiceBinder) service;
            socketService = binder.getService();

            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;

        }
    };

}
