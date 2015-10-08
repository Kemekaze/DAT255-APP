package elias.testdbconnection;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import elias.testdbconnection.Objects.Post;
import elias.testdbconnection.Resources.Constants;
import elias.testdbconnection.Resources.ServerQueries;
import elias.testdbconnection.events.PostsEvent;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import de.greenrobot.event.EventBus;

public class SocketService extends Service {
    private static final String TAG = "App.SocketService";

    private final IBinder socketServiceBinder = new SocketServiceBinder();
    private static Socket socket;
    private EventBus eventBus = EventBus.getDefault();

    public SocketService() {
        Log.i(TAG, "SocketService");
        try {
            socket = IO.socket(Constants.SERVER_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        setupSocket();
        return socketServiceBinder;
    }
    private void setupSocket(){
        Log.i(TAG, "onBind");
        socket.on(Socket.EVENT_CONNECT, eventConnected);
        socket.on("posts", eventGetPosts);
        socket.on("busses", eventGetBuses);
        socket.connect();
    }
    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        setupSocket();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service destroyed");
        super.onDestroy();
    }
    public Socket getSocket(){
        Log.i(TAG, "getSocket");
        Log.i(TAG, Thread.currentThread().toString());
        return socket;
    }
    public static <T> void  send(String key, T value){
        Log.i(TAG, "send");
        Log.i(TAG, Thread.currentThread().toString());
        socket.emit(key,value);
    }

    private List<Post> jsonToPost(JSONArray jsonArray){
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

    private Emitter.Listener eventGetPosts = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG,"eventGetPosts");
            eventBus.post(new PostsEvent(jsonToPost((JSONArray) args[0])));
        }
    };
    private Emitter.Listener eventGetBuses = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventGetBuses");
        }
    };
    private Emitter.Listener eventConnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventConnected");

                JSONObject query = ServerQueries.query("token", "this should be a token");
                socket.emit("authenticate",query);

            /*try {
                JSONArray query = ServerQueries.query(new JSONObject(), 10, 0, new JSONObject().putOpt("date", -1));
                socket.emit("get posts",query);
            } catch (JSONException e) {
                socket.emit("getPosts",new JSONArray());
            }*/

        }
    };

    public class SocketServiceBinder extends Binder{
        SocketService getService(){
            return SocketService.this;
        }
    }
}