package dat255.app.buzzter.Resources;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by elias on 2015-09-25.
 */
public class ServerQueries {
    /*public static <T> JSONArray query(T arg){
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(arg);
        return jsonArray;
    }
    public static <T,T2> JSONArray query(T arg, T2 arg2 ){
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(arg);
        jsonArray.put(arg2);
        return jsonArray;
    }
    public static <T,T2,T3> JSONArray query(T arg,T2 arg2,T3 arg3){
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(arg);
        jsonArray.put(arg2);
        jsonArray.put(arg3);
        return jsonArray;
    }
    public static <T,T2,T3,T4> JSONArray query(T arg,T2 arg2,T3 arg3,T4 arg4){
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(arg);
        jsonArray.put(arg2);
        jsonArray.put(arg3);
        jsonArray.put(arg4);
        return jsonArray;
    }*/
    public static <T> JSONObject query(String key, T value){
        try {
            return new JSONObject().put(key,value);
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
