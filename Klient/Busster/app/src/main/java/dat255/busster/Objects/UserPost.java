package dat255.busster.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The subclass to the type Post used for all userposts in the feed
 * 
 */
public class UserPost extends Post{

    private List<Comment> comments = new ArrayList<Comment>();
    private int[] votes = {-1,-1};
    private String color = "#039BE5";
    private String type = "userpost";

    public UserPost(JSONObject post) {
        super(post);
        try {
            JSONObject meta = post.getJSONObject("meta");
            JSONObject votes = meta.getJSONObject("votes");

            this.votes = new int[]{
                    votes.getInt("up"),
                    votes.getInt("down")
            };

            JSONArray comments = post.getJSONArray("comments");
            int commentsSize = comments.length();
            for (int i = 0; i < commentsSize; i++) {
                this.comments.add(new Comment(comments.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getColor() {
        return this.color;
    }

    public int[] getVotes() {
        return votes;
    }

    public void incUpVotes(){
        this.votes[0]++;
    }
    public void decUpVotes(){
        this.votes[0]--;
    }
    public void incDownVotes(){
        this.votes[1]++;
    }
    public void decDownVotes(){
        this.votes[1]--;
    }


    public List<Comment> getComments() {
        return comments;
    }

    public int getCommentCount() {
        return comments.size();
    }



}
