package dat255.busster.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import dat255.busster.DB.VoteDBHandler;
import dat255.busster.Events.SendDataEvent;
import dat255.busster.Events.StatusEvent;
import dat255.busster.Objects.Post;

import dat255.busster.Objects.UserPost;

import dat255.busster.Objects.Vote;

import dat255.busster.R;
import dat255.busster.Resources.Constants;
import dat255.busster.Resources.ServerQueries;
import de.greenrobot.event.EventBus;


public class FeedAdapter extends RecyclerSwipeAdapter<FeedAdapter.ViewHolder> {
    private static  final String TAG = "dat255.FeedAdapter";



    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case

        public TextView body;
        public TextView user;
        public TextView service;
        public TextView time;
        public TextView votes_up;
        public TextView votes_down;
        public TextView comment_count;
        public TextView votes;
        public View view;
        public SwipeLayout swipeLayout;
        public RelativeLayout postLayout;

        public LinearLayout rightLayout;
        public LinearLayout leftLayout;

        public ViewHolder(View view) {
            super(view);
            postLayout = (RelativeLayout) view.findViewById(R.id.post);
            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
            leftLayout = (LinearLayout) view.findViewById(R.id.post_bottom_wrapper_left);
            rightLayout = (LinearLayout) view.findViewById(R.id.post_bottom_wrapper_right);

            body = (TextView) view.findViewById(R.id.body);
            user = (TextView) view.findViewById(R.id.user);
            service = (TextView) view.findViewById(R.id.service);
            time = (TextView) view.findViewById(R.id.time);
            votes = (TextView) view.findViewById(R.id.votes);
            votes_up = (TextView) view.findViewById(R.id.votesUp);
            votes_down = (TextView) view.findViewById(R.id.votesDown);
            comment_count = (TextView) view.findViewById(R.id.comments);
            this.view = view;
        }
    }


    public List<Post> posts;
    public RecyclerView recyclerView;
    private Context mContext;
    private VoteDBHandler votesHandler;

    public FeedAdapter(Context context, List<Post> posts, RecyclerView mRecyclerView) {
        this.posts = posts;
        this.mContext = context;
        this.recyclerView = mRecyclerView;
        votesHandler = new VoteDBHandler(context,null);
    }
    // Create new views (invoked by the layout manager)
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_alt, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.swipeLayout.setRightSwipeEnabled(true);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.rightLayout);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.leftLayout);

        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {

            @Override
            public void onClose(SwipeLayout layout) {
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {
            }

            @Override
            public void onOpen(SwipeLayout layout) {

                layout.close(true);
                UserPost post = (UserPost)posts.get(pos);
                boolean like = (layout.getDragEdge().name().equals("Left"))? true:false;
                List<Boolean> exists = votesHandler.checkIfExists(post.getId());
                Log.i(TAG,"onOpen e:"+exists.get(0)+" l:"+like);
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
                    //EventBus.getDefault().post(new StatusEvent("Already voted!"));
                    //updatedPosts();
                    return;
                }
                EventBus.getDefault().post(new StatusEvent(msg));
                EventBus.getDefault().post(new SendDataEvent(eventType, ServerQueries.query("post_id", post.getId())));
                refreshVotes(pos, post);
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                layout.close(true);
            }
        });

        holder.view.setClickable(true);
        holder.view.setOnLongClickListener(clickListener);

        holder.postLayout.setBackgroundColor(Color.parseColor(posts.get(position).getColor()));
        holder.body.setText(posts.get(position).getBody());
        holder.user.setText(posts.get(position).getUser());
        holder.service.setText(String.valueOf(posts.get(position).getBusLine()));
        holder.time.setText(posts.get(position).getTimeSince());


        //holder.votes_up.setText(String.valueOf(posts.get(position).getVotes()[0]));
        //holder.votes_down.setText(String.valueOf(posts.get(position).getVotes()[1]));
        //holder.votes_up.setTextColor(R.color.green_600);
        //holder.votes_down.setTextColor(R.color.red_600);




        holder.comment_count.setText(String.valueOf(((UserPost) posts.get(position)).getCommentCount()));
        holder.votes.setText(String.valueOf(((UserPost) posts.get(position)).getVotes()[0] - ((UserPost) posts.get(position)).getVotes()[1]));




    }

    private View.OnLongClickListener clickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int position = recyclerView.getChildAdapterPosition(v);
            Log.i(TAG,"onClick : "+ position);
            Post p = getItem(position);
            return true;
        }
    };


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return posts.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public Post getItem(int position){
        return posts.get(position);
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void refreshVotes(int position, Post post) {
        posts.set(position, post);
        this.notifyDataSetChanged();
    }

    public void updatedPosts(){
        this.notifyDataSetChanged();
    }
    public void addPosts(List<Post> posts,int type) {
        if(posts.size() == 0) return;
        switch(type){
            case 1:
                addPostsRefresh(posts);
                break;
            case 2:
                addPostsBeginning(posts);
                break;
            default:
                this.posts.addAll(posts);
                break;
        }
        this.notifyDataSetChanged();
    }
    private void addPostsBeginning(List<Post> posts) {
        this.posts.addAll(0, posts);
    }
    private void addPostsRefresh(List<Post> posts) {
        this.posts = posts;
    }
    public void addPost(Post post) {
        posts.add(post);
        this.notifyDataSetChanged();
    }
    public void addPostBeginning(Post post) {
        posts.add(0, post);
        this.notifyDataSetChanged();
    }



}
