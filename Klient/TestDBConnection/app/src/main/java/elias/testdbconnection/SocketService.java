package elias.testdbconnection;


import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import de.greenrobot.event.EventBus;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketService extends IntentService{

    private static final String TAG = "App.SocketService";

    private Socket socket = null;
    public SocketService(){
        super("SocketService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.i(TAG, "Service started!");
        try {
            socket = IO.socket("http://192.168.0.202:3000");
            socket.connect();
            Log.i(TAG, String.valueOf(socket.connected()));
            Log.i(TAG, "Connected to Server!");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray arr = new JSONArray();
                arr.put(new JSONObject());
                arr.put(0);
                arr.put(5);
                try {
                    arr.put(new JSONObject().put("date", -1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "Sending get post request!");
                socket.emit("get posts", arr);
            }
        });

        socket.on("posts", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray arr = (JSONArray) args[0];
                EventBus.getDefault().post(new MessageEvent(arr));
                Log.i(TAG, arr.toString());
            }
        });

    }
}
