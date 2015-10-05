package dat255.app.buzzter;

import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

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



    private Button btn,btn2;
    private ListView lw;


    private SwipeRefreshLayout mSwipeRefreshLayout;

    private final String TAG = "dat255.app.buzzter.Main";
    private Intent socketServiceIntent;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Post> mArray;





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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.posts);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mArray = new ArrayList<Post>();
        mAdapter = new PostsAdapter(mArray);
        mRecyclerView.setAdapter(mAdapter);



// Extend the Callback class
        ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                //Toast.makeText(rootView.getContext(),""+ direction, Toast.LENGTH_LONG).show();
                //refreshPosts(rootView);
                PostsAdapter postsAdapter = (PostsAdapter )mRecyclerView.getAdapter();
                boolean like;
                if(direction == 8){
                    Toast.makeText(rootView.getContext(),"VoteUp", Toast.LENGTH_LONG).show();
                    like = true;
                    vote(postsAdapter.getItem(position),like);
                }

                if(direction == 4){
                    Toast.makeText(rootView.getContext(),"VoteDown", Toast.LENGTH_LONG).show();
                    like = false;
                    vote(postsAdapter.getItem(position),like);
                }

                refreshPosts(rootView);
                //mArray.remove(position);
               // mRecyclerView.getAdapter().notifyItemRemoved(position);
            }
        };


        // Create an `ItemTouchHelper` and attach it to the `RecyclerView`
        ItemTouchHelper ith = new ItemTouchHelper(_ithCallback);
        ith.attachToRecyclerView(mRecyclerView);

        FloatingActionButton floatingActionButton = (FloatingActionButton)  rootView.findViewById(R.id.addPost);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOtherFragment();
            }
        });

        btn = (Button) rootView.findViewById(R.id.getMorePosts);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMorePosts(rootView);
            }
        });





        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        refreshPosts(rootView);

                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });
        //mSwipeRefreshLayout.setColorSchemeColors(android.R.color.holo_purple,android.R.color.holo_red_dark);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple,android.R.color.holo_red_dark);
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
        if (status != null)statusEvent(status);
        if(posts != null)updatePostsEvent(posts);
        super.onStart();

    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void updatePostsEvent(PostsEvent event){
        Log.i(TAG, "updatePostsEvent(PostsEvent)");
        //update post list
        PostsAdapter postsAdapter = (PostsAdapter) mAdapter;
        postsAdapter.addPosts(event.posts, event.eventType);
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
    public void refreshPosts(View view) {
        Log.i(TAG, "refreshPosts");
        getPosts(view, 10, 0);
    }



    @Subscribe(threadMode = ThreadMode.MainThread)
    public void statusEvent(StatusEvent event){
        Log.e(TAG, "statusEvent");
        Toast.makeText(getActivity().getApplicationContext(), event.getStatusText(), Toast.LENGTH_SHORT).show();

    }


    private void vote(Post p, boolean like){
        JSONObject data = new JSONObject();
        try {
            data.put("post_id",p.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SendDataEvent ev;
        if(like) {
             ev = new SendDataEvent(Constants.SocketEvents.VOTE_UP, data);
        }else{
            ev = new SendDataEvent(Constants.SocketEvents.VOTE_DOWN, data);
        }
        EventBus.getDefault().post(ev);
    }


    public void showOtherFragment()
    {
        Fragment fr=new AddPostFragment();
        FragmentChangeListener fc=(FragmentChangeListener)getActivity();
        fc.replaceFragment(fr);
    }
}
