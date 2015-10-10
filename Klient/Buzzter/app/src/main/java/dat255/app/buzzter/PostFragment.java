package dat255.app.buzzter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dat255.app.buzzter.Adapters.PostsAdapter;
import dat255.app.buzzter.DB.VoteDBHandler;
import dat255.app.buzzter.Events.PostsEvent;
import dat255.app.buzzter.Events.SendDataEvent;
import dat255.app.buzzter.Events.StatusEvent;
import dat255.app.buzzter.Objects.Post;
import dat255.app.buzzter.Objects.Vote;
import dat255.app.buzzter.Resources.Constants;
import dat255.app.buzzter.Resources.ServerQueries;
import dat255.app.buzzter.Services.SocketService;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


public class PostFragment extends Fragment {



    private Button btn,btn2;
    private ListView lw;


    private SwipeRefreshLayout mSwipeRefreshLayout;

    private final String TAG = "dat255.app.buzzter.PF";
    private Intent socketServiceIntent;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Post> mArray;

    private VoteDBHandler votesHandler;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        votesHandler = new VoteDBHandler(getActivity().getApplicationContext(),null);
        new Thread(){
            public void run(){
                socketServiceIntent = new Intent(getActivity().getApplicationContext(),SocketService.class);
                getActivity().startService(socketServiceIntent);
            }
        }.start();
        /*new Thread(){
            public void run(){
                socketServiceIntent = new Intent(getActivity().getApplicationContext(),ChatHeadService.class);
                getActivity().startService(socketServiceIntent);
            }
        }.start();*/
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

                PostsAdapter postsAdapter = (PostsAdapter )mRecyclerView.getAdapter();
                Post post = postsAdapter.getItem(position);
                boolean like = (direction == 8 )? true:false;
                List<Boolean> exists = votesHandler.checkIfExists(post.getId());
                String eventType = "undefined";
                String msg;
                if(exists.get(0) == false){
                    votesHandler.addVote(new Vote(post.getId(),like));
                    eventType = (like)? Constants.SocketEvents.INC_VOTES_UP: Constants.SocketEvents.INC_VOTES_DOWN;
                    if(like) post.incUpVotes();
                    else post.incDownVotes();
                    msg = (like)?"Liked!":"Disliked!";

                }else if(exists.get(1) != like){
                    votesHandler.removeVote(post.getId());
                    eventType = (like)? Constants.SocketEvents.DEC_VOTES_DOWN: Constants.SocketEvents.DEC_VOTES_UP;
                    if(like) post.decDownVotes();
                    else post.decUpVotes();
                    msg = "Vote removed!";
                }else{
                    Toast.makeText(rootView.getContext(), "Already voted!", Toast.LENGTH_SHORT).show();
                    postsAdapter.updatedPosts();
                    return;
                }
                Toast.makeText(rootView.getContext(),msg, Toast.LENGTH_LONG).show();
                EventBus.getDefault().post(new SendDataEvent(eventType, ServerQueries.query("post_id", post.getId())));
                postsAdapter.refreshVotes(position, post);


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

        floatingActionButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{Color.rgb(170, 102, 204)}));

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
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple);
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

    private void getPosts(View view, int limit, int skip) {
        Log.i(TAG, String.valueOf(skip));

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
    public void refreshPosts(View view) {
        Log.i(TAG, "refreshPosts");
        getPosts(view, 10, 0);
    }





    @Subscribe(threadMode = ThreadMode.MainThread)
    public void statusEvent(StatusEvent event) {
        Log.e(TAG, "statusEvent");
        Toast.makeText(getActivity().getApplicationContext(), event.getStatusText(), Toast.LENGTH_SHORT).show();
    }



    public void showOtherFragment()
    {
        /*
        Fragment fr=new AddPostFragment();
        FragmentChangeListener fc=(FragmentChangeListener)getActivity();
        fc.replaceFragment(fr);
        */
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AddPostFragment fragment = new AddPostFragment();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }
}
