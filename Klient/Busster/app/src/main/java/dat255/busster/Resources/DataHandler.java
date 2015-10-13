package dat255.busster.Resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dat255.busster.Objects.GPS;
import dat255.busster.Objects.Post;
import dat255.busster.Objects.Survey;
import dat255.busster.Objects.UserPost;

/**
 * Created by Costas Pappas on 2015-09-30.
 */
public class DataHandler {

    public static List<Post> jsonToPostArr(JSONArray jsonArray){
        List<Post> posts = new ArrayList<>();
        for(int i = 0; i< jsonArray.length();i++){
            try {
                JSONObject meta = jsonArray.getJSONObject(i).getJSONObject("meta");
                String type =meta.getString("type");
                switch (type){
                    case "post":
                        posts.add(new UserPost(jsonArray.getJSONObject(i)));
                        break;
                    case "survey":
                        posts.add(new Survey(jsonArray.getJSONObject(i)));
                        break;

                }

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

        try {

        JSONObject meta = jsonObject.getJSONObject("meta");
        String type = meta.getString("type");

        switch (type){
            case "post":
                posts.add(new UserPost(jsonObject));
                break;
            case "survey":
                posts.add(new Survey(jsonObject));
                break;
        }

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
