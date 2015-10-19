package dat255.busster.Resources;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import dat255.busster.MainActivity;
import dat255.busster.Objects.Event;
import dat255.busster.R;

/**
 * Created by Rasmus on 2015-10-19.
 */
public  class Notifyer {

    private static Notification.Builder not ;
    private static final int uID = 46756;
    private static Context context;



    public static void nextStopNotify(Event event){

        not = new Notification.Builder(context);
        not.setAutoCancel(true);

        not.setSmallIcon(R.drawable.ic_action_directions_bus);
        not.setTicker("Nästa hållplats");
        not.setWhen(System.currentTimeMillis());
        not.setContentTitle("Nästa hållplats");
        not.setContentText(event.getBody());

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        not.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(uID, not.build());

    }

    public static void setContext(Context ctxt) {
        context = ctxt;
    }

    public static Context getContext() {
        return context;
    }
}
