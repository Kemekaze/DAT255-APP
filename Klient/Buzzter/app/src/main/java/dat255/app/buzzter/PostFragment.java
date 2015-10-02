package dat255.app.buzzter;

import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;

import android.support.v4.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class PostFragment extends Fragment {


    private final String TAG = "dat255.app.buzzter.Main";
    private Intent socketServiceIntent;
    private Button btn,btn2;
    private ListView lw;


    private SwipeRefreshLayout mSwipeRefreshLayout;




    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(){
            public void run(){
                socketServiceIntent = new Intent(getActivity().getApplicationContext(),SocketService.class);
                getActivity().startService(socketServiceIntent);
            }
        }.start();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_post, container, false);

        btn = (Button) rootView.findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOtherFragment();
            }
        });

        btn2 = (Button) rootView.findViewById(R.id.btn);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPosts(rootView);
            }
        });

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

                        getPosts(rootView);

                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
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
        Log.i(TAG, "onStart");
        EventBus.getDefault().register(this);
        StatusEvent status = EventBus.getDefault().getStickyEvent(StatusEvent.class);
        PostsEvent posts = EventBus.getDefault().getStickyEvent(PostsEvent.class);
        if(status != null)statusEvent(status);
        if(posts != null)updatePostsEvent(posts);
        super.onStart();

    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        EventBus.getDefault().unregister(this);
        // stopService(socketServiceIntent);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updatePostsEvent(PostsEvent event){
        Log.i(TAG, "updatePostsEvent(PostsEvent)");
        //update post list
        ListView lw = (ListView) getView().findViewById(R.id.posts);
        PostsAdapter adapter = (PostsAdapter)lw.getAdapter();
        adapter.addPosts(event.posts, event.eventType);
    }

    public void getPosts(View view){
        Log.i(TAG, "refreshPosts");
        ListView lw = (ListView) view.findViewById(R.id.posts);
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
        Toast.makeText(getActivity().getApplicationContext(), event.getStatusText(), Toast.LENGTH_SHORT).show();

    }



    public void showOtherFragment()
    {
        Fragment fr=new AddPostFragment();
        FragmentChangeListener fc=(FragmentChangeListener)getActivity();
        fc.replaceFragment(fr);
    }
}
