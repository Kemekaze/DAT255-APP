package dat255.busster.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import dat255.busster.Resources.Constants;

/**
 * Created by elias on 2015-10-18.
 */
public class DBHandler extends SQLiteOpenHelper {
    public static final String TAG ="dat255.PrefDBHandler";

    String queryVote = "CREATE TABLE " + Constants.DB.VOTES.TABLE + "(" +
            Constants.DB.VOTES.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Constants.DB.VOTES.COLUMN_POST_ID + " TEXT UNIQUE, " +
            Constants.DB.VOTES.COLUMN_LIKE + " INTEGER "+
            ");";
    String querySurvey = "CREATE TABLE " + Constants.DB.SURVEY.TABLE + "(" +
            Constants.DB.SURVEY.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Constants.DB.SURVEY.COLUMN_POST_ID + " TEXT UNIQUE, " +
            Constants.DB.SURVEY.COLUMN_OPTION + " INTEGER "+
            ");";
    String queryPreferences = "CREATE TABLE " + Constants.DB.PREFERENCES.TABLE + "(" +
            Constants.DB.PREFERENCES.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Constants.DB.PREFERENCES.COLUMN_KEY + " TEXT UNIQUE, " +
            Constants.DB.PREFERENCES.COLUMN_VALUE + " TEXT "+
            ");";
    public DBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, Constants.DB.DB_NAME, factory, Constants.DB.VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(queryVote);
        db.execSQL(querySurvey);
        db.execSQL(queryPreferences);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.DB.VOTES.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.DB.SURVEY.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.DB.PREFERENCES.TABLE);
        onCreate(db);
        Log.i(TAG, "Completed onCreate");
    }
}
