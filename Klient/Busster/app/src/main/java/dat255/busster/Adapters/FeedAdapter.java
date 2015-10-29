package dat255.busster.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.Arrays;
import java.util.List;

import dat255.busster.DB.SurveyDBHandler;
import dat255.busster.DB.VoteDBHandler;
import dat255.busster.Events.SendDataEvent;
import dat255.busster.Events.StatusEvent;
import dat255.busster.Events.SurveyEvent;
import dat255.busster.Events.UserPostEvent;
import dat255.busster.Objects.Event;
import dat255.busster.Objects.Post;
import dat255.busster.Objects.Survey;
import dat255.busster.Objects.UserPost;
import dat255.busster.Objects.Vote;
import dat255.busster.R;
import dat255.busster.Resources.Constants;
import dat255.busster.Resources.ServerQueries;
import dat255.busster.Resources.DialogTool;
import dat255.busster.SurveyActivity;
import dat255.busster.ViewCommentsActivity;
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
        public TextView upArrow;
        public TextView downArrow;

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
            upArrow = (TextView) view.findViewById(R.id.upArrow);
            downArrow = (TextView) view.findViewById(R.id.downArrow);
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
    private Context context;

    public FeedAdapter(Context context, List<Post> posts, RecyclerView mRecyclerView) {
        this.context = context;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;
        holder.view.setClickable(true);
        holder.view.setOnClickListener(clickListener);
        holder.view.setOnLongClickListener(clickLongListener);
        holder.comment_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = pos;
                Log.i(TAG,"onClick : "+ position);
                Post p = getItem(position);

                //open comments
                UserPost userPost = (UserPost)p;
                goToComments(userPost);
            }
        });



        if(posts.get(position).getType().equals("userpost")) {
            UserPost userPost=  (UserPost) posts.get(position);
            holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            holder.swipeLayout.setRightSwipeEnabled(true);
            holder.swipeLayout.setLeftSwipeEnabled(true);
            holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.rightLayout);
            holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.leftLayout);
            holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {}

                @Override
                public void onOpen(SwipeLayout layout) {
                    layout.close();
                    boolean like = (layout.getDragEdge().name().equals("Left")) ? true : false;
                    voteUserPost(pos, like);
                }

                @Override
                public void onStartClose(SwipeLayout layout) {}

                @Override
                public void onClose(SwipeLayout layout) {

                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {}

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    layout.close();
                }
            });

            holder.service.setText(String.valueOf(userPost.getBusLine()));
            holder.comment_count.setText(String.valueOf(userPost.getCommentCount()));
            holder.votes.setText(String.valueOf(userPost.getVotes()[0] - userPost.getVotes()[1]));
            holder.votes.setVisibility(View.VISIBLE);
            holder.comment_count.setVisibility(View.VISIBLE);
            holder.upArrow.setVisibility(View.VISIBLE);
            holder.downArrow.setVisibility(View.VISIBLE);
        }else if(posts.get(position).getType().equals("event")) {
            Event eventPost=  (Event) posts.get(position);

            holder.service.setText(String.valueOf(eventPost.getBusLine()));
            holder.swipeLayout.setLeftSwipeEnabled(false);
            holder.swipeLayout.setRightSwipeEnabled(false);
            holder.votes.setVisibility(View.INVISIBLE);
            holder.comment_count.setVisibility(View.INVISIBLE);
            holder.upArrow.setVisibility(View.INVISIBLE);
            holder.downArrow.setVisibility(View.INVISIBLE);
        }else if(posts.get(position).getType().equals("survey")){

            holder.swipeLayout.setLeftSwipeEnabled(false);
            holder.swipeLayout.setRightSwipeEnabled(false);
            holder.service.setText("All");
            holder.votes.setVisibility(View.INVISIBLE);
            holder.comment_count.setVisibility(View.INVISIBLE);
            holder.upArrow.setVisibility(View.INVISIBLE);
            holder.downArrow.setVisibility(View.INVISIBLE);
        }


        holder.postLayout.setBackgroundColor(Color.parseColor(posts.get(position).getColor()));
        holder.body.setText(posts.get(position).getBody());
        holder.user.setText(posts.get(position).getUser());
        holder.time.setText(posts.get(position).getTimeSince());

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildAdapterPosition(v);
            Log.i(TAG,"onClick : "+ position);
            Post p = getItem(position);


            if(p instanceof Survey){

                SurveyDBHandler surveyHandler = new SurveyDBHandler(context,null);
                Survey survey = (Survey) p;
                if(surveyHandler.checkIfExists(survey.getId()).get(0) == 0) {

                    Intent intent = new Intent(context, SurveyActivity.class);
                    intent.putExtra("Body", p.getBody());
                    intent.putExtra("Answers", ((Survey) p).getAlternatives());
                    intent.putExtra("Count", ((Survey) p).getCount());
                    intent.putExtra("User", p.getUser());
                    intent.putExtra("Time", p.getTimeSince());

                    EventBus.getDefault().postSticky(new SurveyEvent(Arrays.asList(survey)));

                    context.startActivity(intent);

                }else{
                    DialogTool.resultDialog(context, survey);
                }

            }else if(p instanceof UserPost){
                //open comments


            }

        }
    };



    


    private View.OnLongClickListener clickLongListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int position = recyclerView.getChildAdapterPosition(v);
            Log.i(TAG,"onClick : "+ position);
            Post p = getItem(position);

            if(p instanceof UserPost){
                    //open comments
                UserPost userPost = (UserPost)p;
                goToComments(userPost);

                }
            return true;
            }

    };

    private  void goToComments(UserPost userPost){

        Activity activity = (Activity) context;
        Intent commentIntent = new Intent(activity, ViewCommentsActivity.class);
        EventBus.getDefault().postSticky(new UserPostEvent(Arrays.asList(userPost)));
        context.startActivity(commentIntent);
        activity.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }


    private void voteUserPost(int pos,boolean like){

        Post p= posts.get(pos);

        if(p instanceof UserPost) {
            UserPost post = (UserPost) p;
            List<Boolean> exists = votesHandler.checkIfExists(post.getId());
            Log.i(TAG, "onOpen e:" + exists.get(0) + " l:" + like);
            String eventType = "undefined";
            String msg;
            if (exists.get(0) == false) {
                votesHandler.addVote(new Vote(post.getId(), like));
                eventType = (like) ? Constants.SocketEvents.INC_VOTES_UP : Constants.SocketEvents.INC_VOTES_DOWN;
                if (like) post.incUpVotes();
                else post.incDownVotes();
                msg = (like) ? "Liked!" : "Disliked!";

            } else if (exists.get(1) != like) {
                votesHandler.removeVote(post.getId());
                eventType = (like) ? Constants.SocketEvents.DEC_VOTES_DOWN : Constants.SocketEvents.DEC_VOTES_UP;
                if (like) post.decDownVotes();
                else post.decUpVotes();
                msg = "Vote removed!";
            } else {
                //EventBus.getDefault().post(new StatusEvent("Already voted!"));
                return;
            }
            EventBus.getDefault().post(new StatusEvent(msg));
            EventBus.getDefault().post(new SendDataEvent(eventType, ServerQueries.query("post_id", post.getId())));
            refreshVotes(pos, post);
        }
    }

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
