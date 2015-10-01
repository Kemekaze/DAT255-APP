package dat255.app.buzzter;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import dat255.app.buzzter.Events.SavePostEvent;
import dat255.app.buzzter.Events.SendDataEvent;
import dat255.app.buzzter.Events.StatusEvent;
import dat255.app.buzzter.Resources.Constants;
import dat255.app.buzzter.Resources.ServerQueries;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostFragment extends Fragment {

    private final String TAG = "dat255.app.buzzter.AP";
    private EditText edittext;


    public AddPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_post, container, false);

        edittext = (EditText) rootView.findViewById(R.id.editText);
        // Inflate the layout for this fragment
        return rootView;
    }

    public void getEditText(View v) {
        Log.e(TAG, edittext.getText().toString());
        EventBus.getDefault().post(new SendDataEvent(Constants.SocketEvents.SAVE_POST, ServerQueries.query("body", edittext.getText().toString())));
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart");
        EventBus.getDefault().register(this);
        super.onStart();
    }


    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    //Eventbus events
    @Subscribe
    public void savePostStatus(SavePostEvent event){
        Log.i(TAG, "savePostStatus");
        if(event.getStatus().equals("ok")){
            Log.i(TAG, "savePostStatus");
            EventBus.getDefault().postSticky(new StatusEvent("Post saved!"));
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame,new PostFragment());
            ft.addToBackStack(null);
            ft.commit();
        }
        //BEhandla error för fel.
        else Log.e(TAG, "PostSaveError");
    }


}
