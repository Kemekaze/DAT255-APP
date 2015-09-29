package com.example.rasmus.busster;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FeedActivity extends FragmentActivity{ //extends AppCompatActivity  {

    private ListView lv;
    private CustomAdapter cad;
    private ArrayList<Post> posts = new ArrayList<Post>();
    static float x1,x2;
    static float y1, y2;
    static float diffx ,diffy ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        System.out.println("Ststststts");

        lv = (ListView)findViewById(R.id.listView);

        cad = new CustomAdapter(posts,this);

        lv.setAdapter(cad);

        addNewPost("Hej", "Rasmus", "1min", 55, 600, 10000);
        addNewPost("Halloj!", "Oliver", "1min", 55, 50000, 1);

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
    public void addNewPost(View view){
        cad.addNewPost("Hej jag heter Sven :)", "Sven","2min", 55,2,1);
    }
    private void addNewPost(String message,String userName ,String timeStamp, int bussNumber, int downVote, int upVote ){

        cad.addNewPost(message, userName, timeStamp, bussNumber,downVote,upVote);

    }

    private void deletePost(int position){


        cad.deletePost(position);

    }

    private  View.OnTouchListener oTL = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent touchevent) {

            switch (touchevent.getAction())
            {
                // when user first touches the screen we get x and y coordinate
                case MotionEvent.ACTION_DOWN:
                {
                    x1 = touchevent.getX();
                    y1 = touchevent.getY();
                    break;
                }
                case MotionEvent.ACTION_UP:
                {

                    x2 = touchevent.getX();
                    y2 = touchevent.getY();
                    diffx = x2-x1;
                    diffy = y2-y1;

                    //if left to right sweep event on screen
                    if (x1 < x2 && Math.abs(diffy) < Math.abs(diffx))
                    {
                        Toast.makeText(FeedActivity.this, "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
                    }

                    // if right to left sweep event on screen
                    if (x1 > x2 && Math.abs(diffy) < Math.abs(diffx))
                    {
                        Toast.makeText(FeedActivity.this, "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
                    }

                    // if UP to Down sweep event on screen
                    if (y1 < y2 && Math.abs(diffx) < Math.abs(diffy))
                    {
                        Toast.makeText(FeedActivity.this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
                    }

                    //if Down to UP sweep event on screen
                    if (y1 > y2 && Math.abs(diffy) < Math.abs(diffx))
                    {
                        Toast.makeText(FeedActivity.this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
            }
            return false;
        }
    };

    // onTouchEvent () method gets called when User performs any touch event on screen
    // Method to handle touch event like left to right swap and right to left swap
  /*  public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {

                x2 = touchevent.getX();
                y2 = touchevent.getY();
                diffx = x2-x1;
                diffy = y2-y1;

                //if left to right sweep event on screen
                if (x1 < x2 && Math.abs(diffy) < Math.abs(diffx))
                {
                    Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
                }

                // if right to left sweep event on screen
                if (x1 > x2 && Math.abs(diffy) < Math.abs(diffx))
                {
                    Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
                }

                // if UP to Down sweep event on screen
                if (y1 < y2 && Math.abs(diffx) < Math.abs(diffy))
                {
                    Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
                }

                //if Down to UP sweep event on screen
                if (y1 > y2 && Math.abs(diffy) < Math.abs(diffx))
                {
                    Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
        return false;
    }*/


}
