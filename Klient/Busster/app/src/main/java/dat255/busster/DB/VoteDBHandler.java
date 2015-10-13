package dat255.busster.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dat255.busster.Objects.Vote;
import dat255.busster.Resources.Constants;

/**
 * Created by elias on 2015-10-10.
 */
public class VoteDBHandler extends SQLiteOpenHelper {
    public static final String TAG ="dat255.VoteDBHandler";

    public static final String TABLE_VOTES = "votes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_POST_ID = "postId";
    public static final String COLUMN_LIKE = "like";


    public VoteDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, Constants.DB.DB_NAME, factory, Constants.DB.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_VOTES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_POST_ID + " TEXT UNIQUE, " +
                COLUMN_LIKE + " INTEGER "+
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOTES);
        onCreate(db);
    }
    public void addVote(Vote vote){
        ContentValues values =  new ContentValues();
        values.put(COLUMN_LIKE, vote.get_like());
        values.put(COLUMN_POST_ID, vote.get_postId());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_VOTES,null,values);
        db.close();
        Log.i(TAG, "Completed addVote");
    }
    public List<Vote> getVotes(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_VOTES;
        Cursor c = db.rawQuery(query,null);
        List<Vote> votes = new ArrayList<Vote>();
        c.moveToFirst();
        while(!c.isAfterLast()){
            int id = c.getInt(c.getColumnIndex(COLUMN_ID));
            String post_id = c.getString(c.getColumnIndex(COLUMN_POST_ID));
            Boolean like = (c.getInt(c.getColumnIndex(COLUMN_LIKE)) != 0);
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
        db.execSQL("DELETE FROM " + TABLE_VOTES + " WHERE "+ COLUMN_POST_ID + "= \"" + postId + "\";");
        db.close();
        Log.i(TAG, "Completed removeProduct");
    }
}
