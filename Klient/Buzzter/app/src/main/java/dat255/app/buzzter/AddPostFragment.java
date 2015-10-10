package dat255.app.buzzter;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Button sendBtn;


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
        final View rootView = inflater.inflate(R.layout.add_post, container, false);
        edittext = (EditText) rootView.findViewById(R.id.editText);
        sendBtn = (Button) rootView.findViewById(R.id.SendBtn);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditText(rootView);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public void getEditText(View view) {
        EditText edittext = (EditText)view.findViewById(R.id.editText);
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
            EventBus.getDefault().postSticky(new StatusEvent("Post saved!"));
            //getActivity().finish();
            showOtherFragment();
        }
        //BEhandla error för fel.
        else Log.e(TAG, "PostSaveError");
    }

    public void showOtherFragment(){
        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        PostFragment fragment = new PostFragment();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }


}
