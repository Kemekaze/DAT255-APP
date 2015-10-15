package dat255.busster.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import dat255.busster.DB.VoteDBHandler;
import dat255.busster.Events.SendDataEvent;
import dat255.busster.Events.StatusEvent;
import dat255.busster.Objects.Post;
import dat255.busster.R;
import dat255.busster.Resources.Constants;
import dat255.busster.Resources.ServerQueries;
import de.greenrobot.event.EventBus;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private static  final String TAG = "dat255.CommentsAdapter";

    public List<Post.Comment> comments;
    public RecyclerView recyclerView;

    /*
     * Constructor
     */
    public CommentsAdapter(List<Post.Comment> comments, RecyclerView mRecyclerView) {
        this.comments = comments;
        this.recyclerView = mRecyclerView;

        this.comments.add(new Post.Comment(new JSONObject()));
        this.comments.add(new Post.Comment(new JSONObject()));
        this.comments.add(new Post.Comment(new JSONObject()));
        this.comments.add(new Post.Comment(new JSONObject()));
        this.comments.add(new Post.Comment(new JSONObject()));
        this.comments.add(new Post.Comment(new JSONObject()));
        this.comments.add(new Post.Comment(new JSONObject()));
        this.comments.add(new Post.Comment(new JSONObject()));

        notifyDataSetChanged();
    }

    /*
     * ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case

        public TextView body;
        public TextView user;
        public TextView time;
        public View view;


        public ViewHolder(View view) {
            super(view);

            body = (TextView) view.findViewById(R.id.body);
            user = (TextView) view.findViewById(R.id.user);
            time = (TextView) view.findViewById(R.id.time);
            this.view = view;
        }
    }
    // Create new views (invoked by the layout manager)
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.view.setClickable(true);
        //holder.view.setOnLongClickListener(clickListener);

        holder.body.setText(comments.get(position).getBody());
        holder.user.setText(comments.get(position).getUser());
        holder.time.setText(comments.get(position).getTimeSince());
    }

    private View.OnLongClickListener clickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            int position = recyclerView.getChildAdapterPosition(v);
            Log.i(TAG,"onClick : "+ position);
            Post.Comment p = getItem(position);
            return true;
        }
    };


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return comments.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public Post.Comment getItem(int position){
        return comments.get(position);
    }

    public List<Post.Comment> getPosts() {
        return comments;
    }

    public void refreshVotes(int position, Post.Comment comment) {
        comments.set(position, comment);
        this.notifyDataSetChanged();
    }

    public void updateComments(){
        this.notifyDataSetChanged();
    }
    public void addComments(List<Post.Comment> comments,int type) {
        if(comments.size() == 0) return;
        switch(type){
            case 1:
                addCommentsRefresh(comments);
                break;
            case 2:
                addCommentsBeginning(comments);
                break;
            default:
                this.comments.addAll(comments);
                break;
        }
        this.notifyDataSetChanged();
    }
    private void addCommentsBeginning(List<Post.Comment> comments) {
        this.comments.addAll(0, comments);
    }
    private void addCommentsRefresh(List<Post.Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Post.Comment comment) {
        comments.add(comment);
        this.notifyDataSetChanged();
    }
    public void addCommentBeginning(Post.Comment comment) {
        comments.add(0, comment);
        this.notifyDataSetChanged();
    }



}
