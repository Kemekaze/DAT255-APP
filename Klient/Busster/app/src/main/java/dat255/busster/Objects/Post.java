package dat255.busster.Objects;


import org.joda.time.Interval;
import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;


public class Post {

    private String _id ="";
    private String body = "";
    private String user = "";
    private long time = -1;
    private int busLine = -1;
    private String type = "post";
    private String color = "#039BE5";



    public Post(JSONObject post) {

        try {
            this._id = post.getString("_id");
            this.body = post.getString("body");
            this.user = post.getString("user");
            this.time = post.getLong("date");
            JSONObject meta = post.getJSONObject("meta");
            this.type = meta.getString("type");

            JSONObject bus = meta.getJSONObject("bus");
            this.busLine = bus.getInt("serviceid");


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



    public long getTime() {
        return time;
    }

    public String getType() {
        return type;
    }


    public String getUser() {
        return user;
    }


    public String getColor() {
        return color;
    }
    public String getTimeSince() {
        return getTimeSince(this.time);
    }
    public class Comment{
        private String _id;
        private String body;
        private String user;
        private long time;
        //private int[] votes;

        public Comment(JSONObject comment){
            try {

                this._id = comment.getString("_id");
                this.body = comment.getString("body");
                this.user = comment.getString("user");
                this.time = comment.getLong("date");


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

        public String getTimeSince() {
            return Post.getTimeSince(this.time);
        }

        public String getUser() {
            return user;
        }


    }
    public static String getTimeSince(long t) {
        long tTime = t;
        long diff = System.currentTimeMillis()-tTime;
        if(diff<0) tTime+=diff;

        Interval interval = new Interval(tTime,System.currentTimeMillis());
        Period p = interval.toPeriod();
        String time;
        if(p.getYears() > 0)
            time = p.getYears()+"Y";
        else if(p.getMonths() > 0)
            time = p.getMonths()+"M";
        else if(p.getDays() > 0)
            time = p.getDays()+"d";
        else if(p.getHours() > 0)
            time = p.getHours()+"h";
        else if(p.getMinutes() > 0)
            time = p.getMinutes()+"m";
        else
            time = p.getSeconds()+"s";

        return time+"";
    }
}
