package elias.testdbconnection;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import elias.testdbconnection.Objects.Post;


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
    public void addPosts(List<Post> posts) {
        this.posts.addAll(posts);
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

        body.setText(p.getBody());
        user.setText(p.getUser());
        Log.i("Object.Post", "UP: " + String.valueOf(p.getVotes()[0]) + " DOWN: " + String.valueOf(p.getVotes()[1]));
        votesUp.setText(String.valueOf(p.getVotes()[0]));
        votesDown.setText(String.valueOf(p.getVotes()[1]));
        line.setText(String.valueOf(p.getBusLine()));

        return customView;
    }
}
