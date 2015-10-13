package dat255.busster.Resources;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by elias on 2015-09-25.
 */
public class ServerQueries {

    public static <T> JSONObject query(String key, T value){
        try {
            return new JSONObject().put(key,value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> JSONObject add(JSONObject obj, String key, T value){
        try {
            return obj.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T,T2> JSONObject getPosts(T query, int limit, int skip, T2 sort){
        JSONObject q = new JSONObject();
        try {
            q.put("query",query);
            q.put("limit",limit);
            q.put("skip",skip);
            q.put("sort",sort);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return q;
    }



}
