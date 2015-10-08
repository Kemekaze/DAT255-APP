package dat255.app.buzzter.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dat255.app.buzzter.Objects.Post;
import dat255.app.buzzter.R;

/**
 * Created by Walle on 2015-10-06.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{
    private static  final String TAG = "dat255.app.buzzter.CA";

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView body;
        public TextView user;
        public TextView time;
    //    public TextView votes_up;
    //   public TextView votes_down;
        public viewHolderClicks viewHolderClicks;

        public ViewHolder(View view, viewHolderClicks listner) {
            super(view);
            body = (TextView) view.findViewById(R.id.body);
            user = (TextView) view.findViewById(R.id.user);
            time = (TextView) view.findViewById(R.id.time);
            //votes_up = (TextView) view.findViewById(R.id.votesUp);
            //votes_down = (TextView) view.findViewById(R.id.votesDown);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            viewHolderClicks.onclick(v);
        }

        public static interface viewHolderClicks {
            public void onclick(View v);
        }
    }
<<<<<<< HEAD

=======
>>>>>>> dev
    public List<Post.Comment> comments;

    public CommentsAdapter(List<Post.Comment> comments) { this.comments = comments; }

    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        ViewHolder vh = new ViewHolder(v, new ViewHolder.viewHolderClicks() {
            @Override
            public void onclick(View v) {
                Log.i(TAG, String.valueOf(v.getId()));
                //Toast.makeText(this.getApplicationContext(), v., Toast.LENGTH_SHORT).show();
            }
        });
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.body.setText(comments.get(position).getBody());
        holder.user.setText(comments.get(position).getUser());
        //holder.line.setText(String.valueOf(posts.get(position).getBusLine()));
        holder.time.setText(comments.get(position).getRelativeTime());
        // holder.votes_up.setText(String.valueOf(posts.get(position).getVotes()[0]));
        // holder.votes_down.setText(String.valueOf(posts.get(position).getVotes()[1]));

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
    public List<Post.Comment> getComments() { return comments; }

    public void addComment(Post.Comment comment) {
        comments.add(comment);
        this.notifyDataSetChanged();
    }
    public void addCommentBeginning(Post.Comment comment) {
        comments.add(0, comment);
        this.notifyDataSetChanged();
    }

    public void addPosts(List<Post.Comment> comments,int type) {
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
    private void addCommentsRefresh(List<Post.Comment> comments) {
        this.comments.addAll(comments);
        this.notifyDataSetChanged();
    }
    private void addCommentsBeginning(List<Post.Comment> comments) {
        this.comments.addAll(0,comments);
        this.notifyDataSetChanged();
    }
}
