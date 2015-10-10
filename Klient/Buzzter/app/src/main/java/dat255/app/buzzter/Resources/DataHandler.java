package dat255.app.buzzter.Resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dat255.app.buzzter.Objects.GPS;
import dat255.app.buzzter.Objects.Post;

/**
 * Created by Costas Pappas on 2015-09-30.
 */
public class DataHandler {

    public static List<Post> jsonToPostArr(JSONArray jsonArray){
        List<Post> posts = new ArrayList<>();
        for(int i = 0; i< jsonArray.length();i++){
            try {
                posts.add(new Post(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }
    public static Post jsonToPostObj(JSONObject jsonObject){
      return new Post(jsonObject);
    }
    public static List<Post> jsonToPostArr(JSONObject jsonObject){
        List<Post> posts = new ArrayList<>();

        posts.add(new Post(jsonObject));
        return posts;
    }

    public static List<GPS> GPS (JSONArray jsonArray){
        List<GPS> posts = new ArrayList<>();
        for(int i = 0; i< jsonArray.length();i++){
            try {
                posts.add(new GPS(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }
}
