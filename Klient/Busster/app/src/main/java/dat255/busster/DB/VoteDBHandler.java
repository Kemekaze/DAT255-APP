package dat255.busster.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dat255.busster.Objects.Vote;
import dat255.busster.Resources.Constants;

/**
 * Created by elias on 2015-10-10.
 */
public class VoteDBHandler extends DBHandler{
    public static final String TAG ="dat255.VoteDBHandler";

    public VoteDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, factory);
    }
    public void addVote(Vote vote){
        ContentValues values =  new ContentValues();
        values.put(Constants.DB.VOTES.COLUMN_LIKE, vote.get_like());
        values.put(Constants.DB.VOTES.COLUMN_POST_ID, vote.get_postId());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(Constants.DB.VOTES.TABLE,null,values);
        db.close();
        Log.i(TAG, "Completed addVote");
    }
    public List<Vote> getVotes(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + Constants.DB.VOTES.TABLE;
        Cursor c = db.rawQuery(query,null);
        List<Vote> votes = new ArrayList<Vote>();
        c.moveToFirst();
        while(!c.isAfterLast()){
            int id = c.getInt(c.getColumnIndex(Constants.DB.VOTES.COLUMN_ID));
            String post_id = c.getString(c.getColumnIndex(Constants.DB.VOTES.COLUMN_POST_ID));
            Boolean like = (c.getInt(c.getColumnIndex(Constants.DB.VOTES.COLUMN_LIKE)) != 0);
            votes.add(new Vote(id,post_id,like));

            c.moveToNext();
        }
        c.close();
        db.close();
        Log.i(TAG, "Completed getVotes");
        return votes;

    }
    public List<Boolean> checkIfExists(String postId){
        SQLiteDatabase db = getReadableDatabase();
        List<Vote> votes = getVotes();
        List<Boolean> exist = new ArrayList<Boolean>();
        for (Vote v: votes) {
            if(v.get_postId().equals(postId)){
                exist.add(true);
                exist.add(v.get_like());
                return exist;
            }
        }
        exist.add(false);
        return exist;
    }
    public void removeVote(String postId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + Constants.DB.VOTES.TABLE + " WHERE "+ Constants.DB.VOTES.COLUMN_POST_ID + "= \"" + postId + "\";");
        db.close();
        Log.i(TAG, "Completed removeProduct");
    }
}
