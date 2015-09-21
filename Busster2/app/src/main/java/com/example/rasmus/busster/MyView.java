package com.example.rasmus.busster;

import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.widget.TextView;

/**
 * Created by Rasmus on 2015-09-20.
 */

    public class MyView extends TextView {

        private static final int MAX_INDENT = 300;
        private static final String TAG = MyView.class.getSimpleName();

        public MyView(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas){
            canvas.save();
            float indent = getIndent(getY());
            canvas.translate(indent, 0);
            super.onDraw(canvas);
            canvas.restore();
        }

        public float getIndent(float distance){
            float x_vertex = MAX_INDENT;
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            float y_vertex = displayMetrics.heightPixels / 2 / displayMetrics.density;
            double a = ( 0 - x_vertex ) / ( Math.pow(( 0 - y_vertex), 2) ) ;
            float indent = (float) (a * Math.pow((distance - y_vertex), 2) + x_vertex);
            return indent;
        }
    }

