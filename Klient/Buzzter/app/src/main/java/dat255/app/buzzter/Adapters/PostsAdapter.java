package dat255.app.buzzter.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import dat255.app.buzzter.Objects.Post;
import dat255.app.buzzter.R;


public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView body;
        public TextView user;
        public TextView line;
        public TextView time;
        public TextView votes_up;
        public TextView votes_down;
        public ViewHolder(View view) {
            super(view);
            body = (TextView) view.findViewById(R.id.body);
            user = (TextView) view.findViewById(R.id.user);
            line = (TextView) view.findViewById(R.id.line);
            time = (TextView) view.findViewById(R.id.time);
            votes_up = (TextView) view.findViewById(R.id.votesUp);
            votes_down = (TextView) view.findViewById(R.id.votesDown);
        }
    }


    public List<Post> posts;
    public PostsAdapter(List<Post> posts) {
        this.posts = posts;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.body.setText(posts.get(position).getBody());
        holder.user.setText(posts.get(position).getUser());
        holder.line.setText(String.valueOf(posts.get(position).getBusLine()));
        holder.time.setText(posts.get(position).getRelativeTime());
       // holder.votes_up.setText(String.valueOf(posts.get(position).getVotes()[0]));
       // holder.votes_down.setText(String.valueOf(posts.get(position).getVotes()[1]));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return posts.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public List<Post> getPosts() {
        return posts;
    }
    public void addPost(Post post) {
       posts.add(post);
       this.notifyDataSetChanged();
    }
    public void addPostBegining(Post post) {
        posts.add(0, post);
        this.notifyDataSetChanged();
    }
    public void addPosts(List<Post> posts,int type) {
        switch(type){
            case 1:
                addPostsRefresh(posts);
                break;
            case 2:
                addPostsBegining(posts);
                break;
            default:
                this.posts.addAll(posts);
                break;
        }
        this.notifyDataSetChanged();
    }
    private void addPostsBegining(List<Post> posts) {
        this.posts.addAll(0,posts);
        this.notifyDataSetChanged();
    }
    private void addPostsRefresh(List<Post> posts) {
        this.posts = posts;
        this.notifyDataSetChanged();
    }

}
