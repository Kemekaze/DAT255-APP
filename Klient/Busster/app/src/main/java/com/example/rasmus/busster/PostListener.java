package com.example.rasmus.busster;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Rasmus on 2015-09-27.
 */
public class PostListener implements View.OnTouchListener {
    private float x1,x2;
    private float y1, y2;
    private float diffx ,diffy ;
    private CustomAdapter customAdapter;
    private int position;

    public PostListener(CustomAdapter customAdapter, int position) {
        this.customAdapter = customAdapter;
        this.position = position;
        System.out.println("Create");

    }

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
                    //customAdapter.deletePost(position);
                    System.out.println("Hej");
                    Toast.makeText(v.getContext(), "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
                }

                // if right to left sweep event on screen
                if (x1 > x2 && Math.abs(diffy) < Math.abs(diffx))
                {
                    Toast.makeText(v.getContext(), "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
                }

                // if UP to Down sweep event on screen
                if (y1 < y2 && Math.abs(diffx) < Math.abs(diffy))
                {
                    Toast.makeText(v.getContext(), "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
                }

                //if Down to UP sweep event on screen
                if (y1 > y2 && Math.abs(diffy) < Math.abs(diffx))
                {
                    Toast.makeText(v.getContext(), "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
        return false;
    }
}
