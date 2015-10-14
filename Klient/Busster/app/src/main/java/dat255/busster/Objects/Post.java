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
        /*try{
            this.color = Constants.COLORS.get(new Random().nextInt(Constants.COLORS.size()));
        }catch (Exception e){
            e.printStackTrace();
        }*/

        try {
            JSONObject meta = post.getJSONObject("meta");

            JSONObject bus = meta.getJSONObject("bus");

            this._id = post.getString("_id");
            this.body = post.getString("body");
            this.user = post.getString("user");
            this.busLine = bus.getInt("serviceid");
            this.type = meta.getString("type");

            this.time = post.getLong("date");

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

    public String getTimeSince() {
        long tTime = this.time;
        long diff = System.currentTimeMillis()-tTime;
        if(diff<0) tTime+=diff;
        //Log.i("dat255","P: "+tTime+" N: "+System.currentTimeMillis());
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

    public String getUser() {
        return user;
    }


    public String getColor() {
        return color;
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

        public String getTimeSince() {
            return "EMPTY";
        }

        public String getUser() {
            return user;
        }

        /*public int[] getVotes() {
            return votes;
        }*/
    }
}