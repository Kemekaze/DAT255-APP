package dat255.busster.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dat255.busster.Objects.SurveyVote;
import dat255.busster.Resources.Constants;


public class SurveyDBHandler extends DBHandler {
    public static final String TAG ="dat255.SVoteDBHandler";


    public SurveyDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, factory);
    }
    public void addVote(SurveyVote vote){
        ContentValues values =  new ContentValues();
        values.put(Constants.DB.SURVEY.COLUMN_OPTION, vote.get_vote());
        values.put(Constants.DB.SURVEY.COLUMN_POST_ID, vote.get_postId());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(Constants.DB.SURVEY.TABLE,null,values);
        db.close();
        Log.i(TAG, "Completed addVote");
    }
    public List<SurveyVote> getVotes(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + Constants.DB.SURVEY.TABLE;
        Cursor c = db.rawQuery(query,null);
        List<SurveyVote> votes = new ArrayList<SurveyVote>();
        c.moveToFirst();
        while(!c.isAfterLast()){
            int id = c.getInt(c.getColumnIndex(Constants.DB.SURVEY.COLUMN_ID));
            String post_id = c.getString(c.getColumnIndex(Constants.DB.SURVEY.COLUMN_POST_ID));
            int like = c.getInt(c.getColumnIndex(Constants.DB.SURVEY.COLUMN_OPTION));
            votes.add(new SurveyVote(id,post_id,like));

            c.moveToNext();
        }
        c.close();
        db.close();
        Log.i(TAG, "Completed getVotes");
        return votes;

    }
    public List<Integer> checkIfExists(String postId){
        SQLiteDatabase db = getReadableDatabase();
        List<SurveyVote> votes = getVotes();
        List<Integer> exist = new ArrayList<Integer>();
        for (SurveyVote v: votes) {
            if(v.get_postId().equals(postId)){
                exist.add(1);
                exist.add(v.get_vote());
                return exist;
            }
        }
        exist.add(0);
        return exist;
    }

    public void removeVote(String postId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Constants.DB.SURVEY.TABLE + " WHERE "+ Constants.DB.SURVEY.COLUMN_POST_ID + "= \"" + postId + "\";");
        db.close();
        Log.i(TAG, "Completed removeProduct");
    }
}
