package dat255.busster.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dat255.busster.Objects.Post;
import dat255.busster.R;


public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private static  final String TAG = "dat255.FeedAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView body;
        public TextView user;
        public TextView service;
        public TextView time;
        public TextView votes_up;
        public TextView votes_down;
        public TextView comment_count;
        public TextView votes;
        public viewHolderClicks viewHolderClicks;
        public View view;

        public ViewHolder(View view, viewHolderClicks listner) {
            super(view);
            body = (TextView) view.findViewById(R.id.body);
            user = (TextView) view.findViewById(R.id.user);
            service = (TextView) view.findViewById(R.id.service);
            time = (TextView) view.findViewById(R.id.time);
            votes = (TextView) view.findViewById(R.id.votes);
            votes_up = (TextView) view.findViewById(R.id.votesUp);
            votes_down = (TextView) view.findViewById(R.id.votesDown);
            comment_count = (TextView) view.findViewById(R.id.comments);
            this.view = view;
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


    public List<Post> posts;

    public FeedAdapter(List<Post> posts) {
        this.posts = posts;

    }
    // Create new views (invoked by the layout manager)
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_alt, parent, false);

        ViewHolder vh = new ViewHolder(v, new ViewHolder.viewHolderClicks() {
            @Override
            public void onclick(View v) {
                Log.i(TAG, String.valueOf(v.getId()));
                //Toast.makeText(this.getApplicationContext(), v., Toast.LENGTH_SHORT).show();
            }
        });
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.view.setBackgroundColor(Color.parseColor(posts.get(position).getColor()));
        holder.body.setText(posts.get(position).getBody());
        holder.user.setText(posts.get(position).getUser());
        holder.service.setText(String.valueOf(posts.get(position).getBusLine()));
        holder.time.setText(posts.get(position).getTimeSince());
        //holder.votes_up.setText(String.valueOf(posts.get(position).getVotes()[0]));
        //holder.votes_down.setText(String.valueOf(posts.get(position).getVotes()[1]));
        //holder.votes_up.setTextColor(R.color.green_600);
        //holder.votes_down.setTextColor(R.color.red_600);
        holder.comment_count.setText(String.valueOf(posts.get(position).getCommentCount()));
        holder.votes.setText(String.valueOf(posts.get(position).getVotes()[0] - posts.get(position).getVotes()[1]));

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
