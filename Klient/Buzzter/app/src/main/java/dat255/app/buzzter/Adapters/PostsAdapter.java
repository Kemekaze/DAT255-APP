package dat255.app.buzzter.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import dat255.app.buzzter.Objects.Post;


import dat255.app.buzzter.R;


public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private static  final String TAG = "dat255.app.buzzter.A";


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public TextView body;
        public TextView user;
        public TextView line;
        public TextView time;
        public TextView votes_up;
        public TextView votes_down;
        public viewHolderClicks viewHolderClicks;
        public RelativeLayout relativeLayout;

        public ViewHolder(View view, viewHolderClicks listner) {
            super(view);
            body = (TextView) view.findViewById(R.id.body);
            user = (TextView) view.findViewById(R.id.user);
            line = (TextView) view.findViewById(R.id.line);
            time = (TextView) view.findViewById(R.id.time);
            votes_up = (TextView) view.findViewById(R.id.votesUp);
            votes_down = (TextView) view.findViewById(R.id.votesDown);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.rel);
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

    public PostsAdapter(List<Post> posts) {
        this.posts = posts;
    }
    // Create new views (invoked by the layout manager)
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item1, parent, false);

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
        holder.body.setText(posts.get(position).getBody());
        holder.user.setText(posts.get(position).getUser());
        holder.line.setText(String.valueOf(posts.get(position).getBusLine()));
        holder.time.setText(posts.get(position).getRelativeTime());
        holder.votes_up.setText(String.valueOf(posts.get(position).getVotes()[0]));
        holder.votes_down.setText(String.valueOf(posts.get(position).getVotes()[1]));

        holder.votes_up.setTextColor(Color.GREEN);
        holder.votes_down.setTextColor(Color.RED);

        if ((position % 2) == 0){

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
