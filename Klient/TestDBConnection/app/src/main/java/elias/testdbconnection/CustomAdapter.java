package elias.testdbconnection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;


public class CustomAdapter extends BaseAdapter implements ListAdapter{

    private final Context context;
    private final JSONArray jsonArray;
    public CustomAdapter(Context context, JSONArray array) {
        this.jsonArray = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(jsonArray == null)return 0;
        else return jsonArray.length();
    }

    @Override
    public JSONObject getItem(int position) {
        if(jsonArray == null) return null;
        else return jsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        JSONObject jsonObject = getItem(position);
        return jsonObject.optLong("id");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.custom_row, parent, false);


        /*JSONObject obj = getItem(position);
        TextView body = (TextView) customView.findViewById(R.id.body);
        TextView user = (TextView) customView.findViewById(R.id.user);
        TextView votesUp = (TextView) customView.findViewById(R.id.votesUp);
        TextView votesDown = (TextView) customView.findViewById(R.id.votesDown);
        TextView line = (TextView) customView.findViewById(R.id.line);


        try {
            JSONObject meta = obj.getJSONObject("meta");
            JSONObject votes = meta.getJSONObject("votes");
            JSONObject bus = meta.getJSONObject("votes");

            body.setText(obj.getString("body"));
            user.setText(obj.getString("user"));
            votesUp.setText(votes.getString("up"));
            votesDown.setText(votes.getString("down"));
            line.setText(bus.getString("line"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/



        return customView;
    }
}
