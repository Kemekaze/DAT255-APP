package com.example.rasmus.busster;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Rasmus on 2015-09-12.
 */
public class CustomAdapter extends BaseAdapter {

    Context ctxt;
    Item[] items;
    LayoutInflater myInflater;



    public CustomAdapter(Item[] items, Context context){
        this.items = items;
        this.ctxt = context;
        myInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = myInflater.inflate(R.layout.three_row_list_item,parent,false);
        }
        TextView id = (TextView) convertView.findViewById(R.id.TextViewId);
        TextView txt = (TextView) convertView.findViewById(R.id.TextViewTxt);
        TextView cat = (TextView) convertView.findViewById(R.id.TextViewCat);

        id.setText(position+1+"");
        txt.setText(items[position].getText());
        cat.setText(items[position].getCategory().toString());
        cat.setTextColor(whichColor(items[position].getCategory()));

        return convertView;
    }

    public int whichColor(Item.CATEGORY category){
        switch(category) {
            case COOL:
                return Color.CYAN;
            case IMPORTANT:
                return Color.RED;
            case VERYIMPORTANT:
                return Color.YELLOW;
            default:
                return Color.BLACK;
        }
    }
}
