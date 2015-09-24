package com.example.rasmus.busster;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    private ListView lv;
    private CustomAdapter cad;
    private ArrayList<Post> posts = new ArrayList<Post>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lv = (ListView)findViewById(R.id.listView);

        cad = new CustomAdapter(posts,this);

        lv.setAdapter(cad);

        addNewPost("Hej", "Rasmus", "1min", 55, 600, 10000);
        addNewPost("Halloj!", "Oliver","1min",55,50000,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addNewPost(String message,String userName ,String timeStamp, int bussNumber, int downVote, int upVote ){

        Post post = new Post(message, userName, timeStamp, bussNumber,downVote,upVote);
        posts.add(post);
        cad.notifyDataSetChanged();

    }


}
