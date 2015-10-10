package dat255.app.buzzter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import dat255.app.buzzter.Events.GPSEvent;
import dat255.app.buzzter.Events.PostsEvent;
import dat255.app.buzzter.Events.SendDataEvent;
import dat255.app.buzzter.Events.StatusEvent;
import dat255.app.buzzter.Objects.GPS;
import dat255.app.buzzter.R;
import dat255.app.buzzter.Resources.Constants;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MapActivity extends Fragment implements OnMapReadyCallback {

    private final String TAG = "dat255.app.buzzter.Map";
    private Intent socketServiceIntent;
    private GoogleMap mMap;
    private LatLng busPos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.activity_map, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map);*/
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        if(busPos == null) {
            busPos = new LatLng(-34, 151);
        }
        mMap.addMarker(new MarkerOptions().position(busPos).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(busPos));
    }

    @Override
    public void onStart() {

        Log.i(TAG, "onStart");
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(Constants.SocketEvents.GET_BUSES_GPS);
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
    public void getBusPos(GPSEvent event){
            Log.i(TAG, "getBusPos");

            List<GPS> busList = event.gpsList;
            GPS bus = busList.get(0);
            LatLng busPos = new LatLng(bus.getLatitude(),bus.getLongitude());
            mMap.addMarker(new MarkerOptions().position(busPos).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(busPos));
            event.gpsList.get(0);

    }


}
