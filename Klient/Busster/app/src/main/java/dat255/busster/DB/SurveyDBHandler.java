package dat255.busster.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dat255.busster.Objects.Survey;
import dat255.busster.Objects.SurveyVote;
import dat255.busster.Objects.Vote;
import dat255.busster.Resources.Constants;

/**
 * Created by elias on 2015-10-10.
 */
public class SurveyDBHandler extends SQLiteOpenHelper {
    public static final String TAG ="dat255.SVoteDBHandler";

    public static final String TABLE_SURVEY_VOTES = "votes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_POST_ID = "postId";
    public static final String COLUMN_OPTION = "like";


    public SurveyDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, Constants.DB.DB_NAME, factory, Constants.DB.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_SURVEY_VOTES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_POST_ID + " TEXT UNIQUE, " +
                COLUMN_OPTION + " INTEGER "+
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY_VOTES);
        onCreate(db);
    }
    public void addVote(SurveyVote vote){
        ContentValues values =  new ContentValues();
        values.put(COLUMN_OPTION, vote.get_vote());
        values.put(COLUMN_POST_ID, vote.get_postId());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_SURVEY_VOTES,null,values);
        db.close();
        Log.i(TAG, "Completed addVote");
    }
    public List<SurveyVote> getVotes(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_SURVEY_VOTES;
        Cursor c = db.rawQuery(query,null);
        List<SurveyVote> votes = new ArrayList<SurveyVote>();
        c.moveToFirst();
        while(!c.isAfterLast()){
            int id = c.getInt(c.getColumnIndex(COLUMN_ID));
            String post_id = c.getString(c.getColumnIndex(COLUMN_POST_ID));
            int like = c.getInt(c.getColumnIndex(COLUMN_OPTION));
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
        db.execSQL("DELETE FROM " + TABLE_SURVEY_VOTES + " WHERE "+ COLUMN_POST_ID + "= \"" + postId + "\";");
        db.close();
        Log.i(TAG, "Completed removeProduct");
    }
}
