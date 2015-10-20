package dat255.busster.Resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import dat255.busster.Objects.Event;
import dat255.busster.Objects.Post;
import dat255.busster.Objects.Survey;
import dat255.busster.Objects.UserPost;

/**
 * Created by Costas Pappas on 2015-09-30.
 */
public class DataHandler {

    public static  <T> List<T> jsonToObjArr(Class<T> tClass,JSONObject jsonObject){
        List<T> posts = new ArrayList<T>();
        try {
            T cls = tClass.getConstructor(JSONObject.class).newInstance(jsonObject);
            posts.add(cls);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return posts;
    }
    public static  <T> T jsonToObj(Class<T> tClass,JSONObject jsonObject){
        try {
            T cls = tClass.getConstructor(JSONObject.class).newInstance(jsonObject);
            return cls;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static <T> List<T> jsonArrToObjArr(Class<T> tClass, JSONArray jsonArray) throws IllegalAccessException, InstantiationException {
        List<T> posts = new ArrayList<T>();
        for(int i = 0; i< jsonArray.length();i++){
            try {

                T cls = tClass.getConstructor(JSONObject.class).newInstance(jsonArray.getJSONObject(i));
                posts.add(cls);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }
    public static List<Post> postToRposts(JSONArray jsonArray){
        List<Post> posts = new ArrayList<Post>();
        for(int i = 0; i< jsonArray.length();i++){
            try {
                JSONObject meta = jsonArray.getJSONObject(i).getJSONObject("meta");
                if(meta.getString("type").equals("post"))
                    posts.add(new UserPost(jsonArray.getJSONObject(i)));
                else if(meta.getString("type").equals("survey"))
                    posts.add(new Survey(jsonArray.getJSONObject(i)));
                else if(meta.getString("type").equals("event")) {
                    posts.add(new Event(jsonArray.getJSONObject(i)));

                    Notifyer.nextStopNotify(new Event(jsonArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return posts;
    }
}