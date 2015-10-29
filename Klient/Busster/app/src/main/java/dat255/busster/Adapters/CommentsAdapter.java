package dat255.busster.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;

import dat255.busster.Objects.Post;
import dat255.busster.R;

/**
 * This class extends android class android.widget.Adapter providing
 * access to the data items for the underlying AdapterView. The dataset
 * used by this class is List<Post.Comment> comments, containing the
 * comments of the post selected by the user (parent post).
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private static  final String TAG = "dat255.CommentsAdapter";

    public List<Post.Comment> comments;
    public RecyclerView recyclerView;

    /**
     * Initializes the list of comments and the recyclerView
     * and notifies that the DataSet has changed.
     * @param comments new comments to be put in the dataset.
     * @param mRecyclerView RecyclerView to be used.
     */
    public CommentsAdapter(List<Post.Comment> comments, RecyclerView mRecyclerView) {
        this.comments = comments;
        this.recyclerView = mRecyclerView;
        notifyDataSetChanged();
    }

    /**
     * This internal class creates views of the items in the
     * dataset. Every comment item will have three TextViews
     * containing the users name (who posted the comment), the
     * comment body and timeSince it was posted.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case

        public TextView body;
        public TextView user;
        public TextView time;
        public View view;

        /**
         * Initiates the TextViews to the corresponding layout
         * in layout/comment.xml
         * @param view
         */
        public ViewHolder(View view) {
            super(view);

            body = (TextView) view.findViewById(R.id.comment_body);
            user = (TextView) view.findViewById(R.id.comment_user);
            time = (TextView) view.findViewById(R.id.comment_time);
            this.view = view;
        }
    }

    /**
     * Creates new views, invoked by the layout manager when ViewHolder
     * is created.
     * @param parent Parent ViewGroup
     * @param viewType
     * @return ViewHolder vh
     */
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    /**
     * Sets the textfields of ViewHolder holder to that of the
     * comment in position's position.
     * @param holder the holder which textfields will be set
     * @param position the position (in List comments) of the comment to be used
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.view.setClickable(true);
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

    /**
     * returns the size of the dataset comments (invoked by
     * the layout manager)
     * @return size of List commments
     */
    @Override
    public int getItemCount() {
        return comments.size();
    }

    /**
     * Returns item id of comment in the given position.
     * @param position position of the requested comment
     *                 in List comments
     * @return long id of comment in position
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * Returns the comment at a given position in List comments.
     * @param position position of the requested comment
     * @return requested comment.
     */
    public Post.Comment getItem(int position){
        return comments.get(position);
    }

    /**
     * Returns the comment list of this adapter.
     * @return the whole comment List.
     */
    public List<Post.Comment> getComments() {
        return comments;
    }

    /**
     * Adds a List of comments to current list of comments.
     * @param comments the comments to be added.
     * @param type the way the list will be added.
     */
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
                this.notifyDataSetChanged();
                break;
        }
    }

    /**
     * Adds new list of comments to beginning of comment list.
     * @param comments list of new comments to be added
     */
    private void addCommentsBeginning(List<Post.Comment> comments) {
        this.comments.addAll(0, comments);
        this.notifyDataSetChanged();
    }

    /**
     * Removes all items in current list of comments and adds
     * the items from the new list of comments to it.
     * @param comments new List of comments to be added.
     */
    private void addCommentsRefresh(List<Post.Comment> comments) {

        Log.i(TAG, comments.size() + "");
        this.comments.clear();
        this.comments.addAll(comments);
        Log.i(TAG, comments.size() + "");
        this.notifyDataSetChanged();
    }

    /**
     * Adds a single comment to the comment list.
     * @param comment Comment to be added.
     */
    public void addComment(Post.Comment comment) {
        comments.add(comment);
        this.notifyDataSetChanged();
    }

    /**
     * Adds a single comment to the beginning of list comments.
     * @param comment comment to be added
     */
    public void addCommentBeginning(Post.Comment comment) {
        comments.add(0, comment);
        this.notifyDataSetChanged();
    }
}
