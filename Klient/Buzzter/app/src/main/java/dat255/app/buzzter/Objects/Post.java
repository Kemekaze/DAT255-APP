package dat255.app.buzzter.Objects;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

public class Post {

    private String _id;
    private String body;
    private String user;
    private Comment[] comments;
    private long time;
    private int[] votes;
    private int busLine;



    public Post(JSONObject post) {

        try {
            JSONObject meta = post.getJSONObject("meta");
            JSONObject votes = meta.getJSONObject("votes");
            JSONObject bus = meta.getJSONObject("bus");

            this._id = post.getString("_id");
            this.body = post.getString("body");
            this.user = post.getString("user");
            this.busLine = bus.getInt("line");
            this.votes = new int[]{
                    votes.getInt("up"),
                    votes.getInt("down")
            };
            this.time = post.getLong("date");
            JSONArray comments = post.getJSONArray("comments");
            int commentsSize = comments.length();
            this.comments = new Comment[commentsSize];
            for (int i = 0; i < commentsSize; i++) {
                this.comments[i] = new Comment(comments.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
    public String getId() {
        return _id;
    }
    public String getBody() {
        return body;
    }

    public int getBusLine() {
        return busLine;
    }

    public Comment[] getComments() {
        return comments;
    }

    public long getTime() {
        return time;
    }

    public String getRelativeTime() {
        PrettyTime p = new PrettyTime();
        return p.format(new Date(time));
    }

    public String getUser() {
        return user;
    }

    public int[] getVotes() {
        return votes;
    }


    public class Comment{
        private String _id;
        private String body;
        private String user;
        private long time;
        //private int[] votes;

        public Comment(JSONObject comment){
            try {
                //JSONObject meta = post.getJSONObject("meta");
                //JSONObject votes = meta.getJSONObject("votes");

                this._id = comment.getString("_id");
                this.body = comment.getString("body");
                this.user = comment.getString("user");
                this.time = comment.getLong("date");

                /*this.votes = new int[]{
                        votes.getInt("up"),
                        votes.getInt("down")
                };*/

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getBody() {
            return body;
        }

        public long getTime() {
            return time;
        }

        public String getRelativeTime() {
            PrettyTime p = new PrettyTime();
            return p.format(new Date(time));
        }

        public String getUser() {
            return user;
        }

        /*public int[] getVotes() {
            return votes;
        }*/
    }
}
