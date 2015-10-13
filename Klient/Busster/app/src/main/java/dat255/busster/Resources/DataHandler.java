package dat255.busster.Resources;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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
}