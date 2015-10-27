package dat255.busster.Resources;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import dat255.busster.Objects.Survey;
import dat255.busster.R;

/**
 * Created by Rasmus on 2015-10-19.
 */
public class DialogTool {

    public static void resultDialog(Context context,Survey sur) {


        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View v = inflater.inflate(R.layout.dialog_result, null);
        dialogBuilder.setView(v);

        ArrayList<String> strs = new ArrayList<String>();
        strs.add(sur.getBody());
        for(int i = 0; i< sur.getOptions(); i++) {
            strs.add( sur.getCount(i) + " röstade på " + sur.getAlternatives().get(i));

        }

        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, strs);
        ListView lw = (ListView) v.findViewById(R.id.listResultView);

        lw.setAdapter(listAdapter);


        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });


        dialogBuilder.create();
        dialogBuilder.show();

    }




}
