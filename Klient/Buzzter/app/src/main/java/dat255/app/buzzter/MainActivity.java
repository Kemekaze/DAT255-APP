package dat255.app.buzzter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

//import android.support.v7.app.ActionBarDrawerToggle;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, FragmentChangeListener{

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private String[] planets;
    private ActionBarDrawerToggle drawerToggle;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    private final String TAG = "dat255.app.buzzter.Main";
    private Intent socketServiceIntent;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       // DrawerLayout section start
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        planets = getResources().getStringArray(R.array.planets);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        drawerListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, planets));
        drawerListView.setOnItemClickListener(this);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,
                R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
            }


        };


        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        fragmentManager = getFragmentManager();

        loadSelection(0);
        // DrawerLayout section end
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConfigurationChanged(Configuration configuration){
     super.onConfigurationChanged(configuration);
        drawerToggle.onConfigurationChanged(configuration);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, planets[position] + "was selected", Toast.LENGTH_LONG).show();
        selectItem(position);
    }


    private void loadSelection(int pos){
        drawerListView.setItemChecked(pos,true);

        switch (pos){
            case 0:
                PostFragment postFragment = new PostFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,postFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                AddPostFragment addPostFragment = new AddPostFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,addPostFragment);
                fragmentTransaction.commit();
                break;

            case 3:
                AlarmFragment alarmFragment = new AlarmFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,alarmFragment);
                fragmentTransaction.commit();
                break;
            case 4:
                break;
        }

    }

    private void selectItem(int position) {
        drawerListView.setItemChecked(position, true);
        setTitle(planets[position]);

        loadSelection(position);

        drawerLayout.closeDrawer(drawerListView);

    }

    public void setTitle(String title){
       getActionBar().setTitle(title);
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, fragment.toString());
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }
}
