package elias.testdbconnection.Resources;

import org.json.JSONArray;

/**
 * Created by elias on 2015-09-25.
 */
public class ServerQueries {
    public static <T> JSONArray query(T arg){
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
    }

}
