package dat255.app.buzzter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

import dat255.app.buzzter.Adapters.CommentsAdapter;
import dat255.app.buzzter.Objects.Post;


public class ViewComments extends AppCompatActivity {
    private final String TAG = "dat255.app.buzzter.VC";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_comments);

        mRecyclerView = (RecyclerView) findViewById(R.id.comments_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CommentsAdapter(new ArrayList<Post.Comment>());
        mRecyclerView.setAdapter(mAdapter);

<<<<<<< HEAD
=======
      //  mAdapter = new CommentsAdapter() {

      //  }
>>>>>>> dev
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
<<<<<<< HEAD
        getMenuInflater().inflate(R.menu.menu_view_comment, menu);
=======
        //getMenuInflater().inflate(R.menu.menu_view_comment, menu); //??
>>>>>>> dev
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

    public void displayComments(){


    }

}

















































