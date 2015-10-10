package dat255.app.buzzter.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;

import dat255.app.buzzter.Events.PostsEvent;
import dat255.app.buzzter.Events.SavePostEvent;
import dat255.app.buzzter.Events.SendDataEvent;
import dat255.app.buzzter.Objects.GPS;
import dat255.app.buzzter.Objects.Post;
import dat255.app.buzzter.Resources.Constants;
import dat255.app.buzzter.Resources.DataHandler;
import dat255.app.buzzter.Resources.ServerQueries;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketService extends Service {
    private static final String TAG = "dat255.app.buzzter.SS";

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
        socket.on(Socket.EVENT_RECONNECT, eventReconnected);
        socket.on(Socket.EVENT_DISCONNECT, eventDisconnected);
        //Authorization
        socket.on(Constants.SocketEvents.AUTHORIZED, eventAuthorized);
        socket.on(Constants.SocketEvents.UNAUTHORIZED, eventUnauthorized);
        //posts
        socket.on(Constants.SocketEvents.GET_POSTS, eventGetPosts);
        socket.on(Constants.SocketEvents.SAVE_POST, eventSavePost);
        socket.on(Constants.SocketEvents.INC_VOTES_UP, eventIncVotesUp);
        socket.on(Constants.SocketEvents.INC_VOTES_DOWN, eventIncVotesDown);
        socket.on(Constants.SocketEvents.DEC_VOTES_UP, eventDecVotesUp);
        socket.on(Constants.SocketEvents.DEC_VOTES_DOWN, eventDecVotesDown);
        //Comments
        socket.on(Constants.SocketEvents.GET_COMMENTS, eventGetComments);
        socket.on(Constants.SocketEvents.SAVE_COMMENT, eventSaveComment);
        //Bus
        socket.on(Constants.SocketEvents.GET_BUSES, eventGetBuses);
        socket.on(Constants.SocketEvents.GET_BUS_NEXT_STOP, eventNextStop);
        socket.on(Constants.SocketEvents.GET_BUSES_GPS, eventGetBusesGPS);
        socket.on(Constants.SocketEvents.GET_BUS_GPS, eventGetBusGPS);

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

            long busId = 2501069758l;
            JSONObject query = ServerQueries.add(ServerQueries.query("token", "this should be a token"),"bus_id", busId);
            socket.emit(Constants.SocketEvents.AUTHENTICATE, query);
        }
    };
    private Emitter.Listener eventReconnected = new Emitter.Listener() {

        @Override
        public void call(Object... args) {

            Log.i(TAG, "eventReconnected");

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
            socket.emit(Constants.SocketEvents.GET_BUSES_GPS);
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
            Log.i(TAG, "eventGetPosts");
            JSONObject data = (JSONObject)args[0];
            JSONArray posts = (JSONArray)data.opt("posts");

            int type = data.optInt("type");
            EventBus.getDefault().post(new PostsEvent(DataHandler.<Post>jsonToPostArr(posts), type));
        }
    };
    private Emitter.Listener eventSavePost = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventSavePost");
            JSONObject obj = (JSONObject)args[0];

            EventBus.getDefault().post(new SavePostEvent(obj.opt("status").toString()));

            EventBus.getDefault().postSticky(new PostsEvent(DataHandler.jsonToPostArr((JSONObject) obj.opt("post")), 2));
        }
    };
    private Emitter.Listener eventIncVotesUp = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventIncVotesUp");
            //vad den skall göra
        }
    };
    private Emitter.Listener eventDecVotesUp = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventIncVotesUp");
            //vad den skall göra
        }
    };private Emitter.Listener eventIncVotesDown = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventIncVotesUp");
            //vad den skall göra
        }
    };private Emitter.Listener eventDecVotesDown = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventIncVotesUp");
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
    private Emitter.Listener eventNextStop = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventNextStop");
            JSONObject post = (JSONObject)args[0];
            int type = 2;
            EventBus.getDefault().post(new PostsEvent(DataHandler.jsonToPostArr(post), type));
        }
    };
    private Emitter.Listener eventGetBusesGPS = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventGetBusesGPS");
            //vad den skall göra
            JSONObject data = (JSONObject)args[0];
            JSONArray gps = (JSONArray)data.opt("gps");

            EventBus.getDefault().post(new PostsEvent(DataHandler.<GPS>jsonToPostArr(gps)));


        }
    };
    private Emitter.Listener eventGetBusGPS = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i(TAG, "eventGetBusGPS");
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