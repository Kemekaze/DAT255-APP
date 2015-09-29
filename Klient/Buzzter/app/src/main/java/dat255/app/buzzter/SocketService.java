package dat255.app.buzzter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import dat255.app.buzzter.Events.PostsEvent;
import dat255.app.buzzter.Events.SendDataEvent;
import dat255.app.buzzter.Objects.Post;
import dat255.app.buzzter.Resources.Constants;
import dat255.app.buzzter.Resources.ServerQueries;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketService extends Service {
    private static final String TAG = " dat255.app.buzzter.SS";

    private Socket socket;

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
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        EventBus.getDefault().register(this);
        socket.on(Socket.EVENT_CONNECT, eventConnected);
        socket.on("posts", eventGetPosts);
        socket.on("busses", eventGetBuses);
        socket.connect();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service destroyed");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
    //Socket events
    private Emitter.Listener eventGetPosts = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG,"eventGetPosts");
            EventBus.getDefault().post(new PostsEvent(jsonToPost((JSONArray) args[0])));
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
        }
    };
    //Eventbus events
    @Subscribe
    public void sendToServerEvent(SendDataEvent event){
        Log.i(TAG, "sendToServerEvent(SendDataEvent)");

        socket.emit(event.getEventName(),event.getData());
    }


}
