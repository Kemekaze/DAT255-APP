package dat255.app.buzzter.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import dat255.app.buzzter.Objects.Post;
import dat255.app.buzzter.R;


public class PostsAdapter extends BaseAdapter{

    private final Context context;
    public List<Post> posts;
    public PostsAdapter(Context context, List<Post> posts) {
        super();
        this.posts = posts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Post getItem(int position) {
        return posts.get(position);
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
        posts.add(0,post);
        this.notifyDataSetChanged();
    }
    public void addPosts(List<Post> posts) {
        this.posts.addAll(posts);
        this.notifyDataSetChanged();
    }
    public void addPostsBegining(List<Post> posts) {
        this.posts.addAll(0,posts);
        this.notifyDataSetChanged();
    }
    public void addPostsRefresh(List<Post> posts) {
        this.posts = posts;
        this.notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.custom_row, parent, false);

        Post p = getItem(position);

        TextView body = (TextView) customView.findViewById(R.id.body);
        TextView user = (TextView) customView.findViewById(R.id.user);
        TextView votesUp = (TextView) customView.findViewById(R.id.votesUp);
        TextView votesDown = (TextView) customView.findViewById(R.id.votesDown);
        TextView line = (TextView) customView.findViewById(R.id.line);
        TextView time = (TextView) customView.findViewById(R.id.time);

        body.setText(p.getBody());
        user.setText(p.getUser());
        votesUp.setText(String.valueOf(p.getVotes()[0]));
        votesDown.setText(String.valueOf(p.getVotes()[1]));
        line.setText(String.valueOf(p.getBusLine()));
        time.setText(p.getRelativeTime());
        return customView;
    }
}
