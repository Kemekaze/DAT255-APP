package com.example.rasmus.busster;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Item item1 = new Item("Matte", Item.CATEGORY.COOL);
        Item item2 = new Item("Pogrammering", Item.CATEGORY.COOL);
        Item item3 = new Item("Tenta", Item.CATEGORY.IMPORTANT);
        Item item4 = new Item("Matte", Item.CATEGORY.COOL);
        Item item5 = new Item("Pogrammering", Item.CATEGORY.COOL);
        Item item6 = new Item("Tenta", Item.CATEGORY.IMPORTANT);
        Item item7 = new Item("Matte", Item.CATEGORY.COOL);
        Item item8 = new Item("Pogrammering", Item.CATEGORY.COOL);
        Item item9 = new Item("Tenta", Item.CATEGORY.IMPORTANT);
        Item item10 = new Item("Matte", Item.CATEGORY.COOL);
        Item item11 = new Item("Pogrammering", Item.CATEGORY.COOL);
        Item item12 = new Item("Tenta", Item.CATEGORY.IMPORTANT);
        Item[] vals = {item1,item2,item3, item4,item5,item6,item7,item8,item9,item10,item11,item12};


        ListView lv = (ListView)findViewById(R.id.listView);

        CustomAdapter cad = new CustomAdapter(vals,this);
        lv.setAdapter(cad);
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
}
