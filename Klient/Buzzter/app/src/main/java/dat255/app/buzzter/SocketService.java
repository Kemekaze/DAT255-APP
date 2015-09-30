package dat255.app.buzzter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;

import dat255.app.buzzter.Events.PostsEvent;
import dat255.app.buzzter.Events.SavePostEvent;
import dat255.app.buzzter.Events.SendDataEvent;
import dat255.app.buzzter.Resources.Constants;
import dat255.app.buzzter.Resources.DataHandler;
import dat255.app.buzzter.Resources.ServerQueries;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketService extends Service {
    private static final String TAG = " dat255.app.buzzter.SS";

    private static Socket socket;

    public SocketService() {
        Log.i(TAG, "SocketService");
        try {
            socket = IO.socket("http://"+Constants.SERVER_IP+":"+Constants.SERVER_PORT);
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
        //Connection
        socket.on(Socket.EVENT_CONNECT, eventConnected);
        socket.on(Socket.EVENT_DISCONNECT, eventDisconnected);
        //Authorization
        socket.on(Constants.SocketEvents.AUTHORIZED, eventAuthorized);
        socket.on(Constants.SocketEvents.UNAUTHORIZED, eventUnauthorized);
        //posts
        socket.on(Constants.SocketEvents.GET_POSTS, eventGetPosts);
        socket.on(Constants.SocketEvents.SAVE_POST, eventSavePost);
        socket.on(Constants.SocketEvents.VOTE_UP, eventVotedUp);
        socket.on(Constants.SocketEvents.VOTE_DOWN, eventVotedDown);
        //Comments
        socket.on(Constants.SocketEvents.GET_COMMENTS, eventGetComments);
        socket.on(Constants.SocketEvents.SAVE_COMMENT, eventSaveComment);
        //Bus
        socket.on(Constants.SocketEvents.GET_BUSES, eventGetBuses);

        socket.connect();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service destroyed");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    /**Socket events**/

    //Connection
    private Emitter.Listener eventConnected = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.i(TAG, "eventConnected");
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            String mac = "null";
            if(nInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                Log.i(TAG, String.valueOf(manager.getWifiState()));
                WifiInfo info = manager.getConnectionInfo();
                mac = info.getBSSID();
            }
            JSONObject query = ServerQueries.add(ServerQueries.query("token", "this should be a token"),"mac", mac);
            socket.emit(Constants.SocketEvents.AUTHENTICATE, query);
        }
    };

    private Emitter.Listener eventDisconnected = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventDisconnected");
            socket.emit(Socket.EVENT_RECONNECT_ATTEMPT);
        }
    };

    //Authentication
    private Emitter.Listener eventAuthorized = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventAuthorized");
            socket.emit(Constants.SocketEvents.GET_POSTS);
        }
    };
    private Emitter.Listener eventUnauthorized = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventUnauthorized");
        }
    };

    //Posts
    private Emitter.Listener eventGetPosts = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG,"eventGetPosts");
            EventBus.getDefault().post(new PostsEvent(DataHandler.jsonToPostArr((JSONArray) args[0])));
        }
    };
    private Emitter.Listener eventSavePost = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventSavePost");
            JSONObject obj = (JSONObject)args[0];

            EventBus.getDefault().post(new SavePostEvent(obj.opt("status").toString()));
            EventBus.getDefault().postSticky(new PostsEvent(DataHandler.jsonToPostArr((JSONObject)obj.opt("post")),2));
        }
    };
    private Emitter.Listener eventVotedUp = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventVotedUp");
            //vad den skall göra
        }
    };
    private Emitter.Listener eventVotedDown = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventVotedDown");
            //vad den skall göra
        }
    };

    //Comments
    private Emitter.Listener eventGetComments = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventGetComments");
            //vad den skall göra
        }
    };
    private Emitter.Listener eventSaveComment = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventSaveComment");

        }
    };
    //Buses
    private Emitter.Listener eventGetBuses = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventGetBuses");
            //vad den skall göra
        }
    };


    //Eventbus events
    @Subscribe
    public void sendToServerEvent(SendDataEvent event){
        Log.i(TAG, "sendToServerEvent(SendDataEvent)");
        socket.emit(event.getEventName(),event.getData());
    }


}
