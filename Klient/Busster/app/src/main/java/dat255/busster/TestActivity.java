package dat255.busster;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import dat255.busster.Objects.Event;

public class TestActivity extends AppCompatActivity {

    Notification.Builder not ;
    private static final int uID = 46756;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        not = new Notification.Builder(this);
        not.setAutoCancel(true);
        resultDialog();
    }

    public void nextStopNotify(Event event){
        not.setSmallIcon(R.drawable.ic_action_directions_bus);
        not.setTicker("Nästa hållplats");
        not.setWhen(System.currentTimeMillis());
        not.setContentTitle("Nästa hållplats");
        not.setContentText(event.getBody());

        Intent intent = new Intent(this, TestActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        not.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(uID, not.build());

    }


    public void resultDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);


        View v = getLayoutInflater().inflate(R.layout.dialog_result, null);
        dialogBuilder.setView(v);

        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       // dialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_result, null));

        ArrayList<String> strs = new ArrayList<String>();
        strs.add("hej1");
        strs.add("Hej2");

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs);
        ListView lw = (ListView) v.findViewById(R.id.listResultView);

        lw.setAdapter(listAdapter);


        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        dialogBuilder.create();
        dialogBuilder.show();

    }

}
