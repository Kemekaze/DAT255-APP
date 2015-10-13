package dat255.busster.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import dat255.busster.Objects.Preference;
import dat255.busster.Resources.Constants;


public class PreferencesDBHandler extends SQLiteOpenHelper {
    public static final String TAG ="dat255.PrefDBHandler";

    public static final String TABLE_PREFERENCES = "preferences";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_VALUE = "value";


    public PreferencesDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, Constants.DB.DB_NAME, factory, Constants.DB.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PREFERENCES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_KEY + " TEXT UNIQUE, " +
                COLUMN_VALUE + " TEXT "+
                ");";
        db.execSQL(query);
        Log.i(TAG, "Completed onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PREFERENCES);
        onCreate(db);
    }
    public void addPreference(Preference preference){
        ContentValues values =  new ContentValues();
        values.put(COLUMN_KEY, preference.get_key());
        values.put(COLUMN_VALUE, preference.get_value());
        SQLiteDatabase db = getWritableDatabase();
        db.insertWithOnConflict(TABLE_PREFERENCES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        Log.i(TAG, "Completed addPreference");
    }
    public Preference getPreference(String skey){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PREFERENCES + " WHERE "+COLUMN_KEY+"= \""+skey+"\";";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        int id=0;
        String key="";
        String value="";
        try {
            id = c.getInt(c.getColumnIndex(COLUMN_ID));
            key = c.getString(c.getColumnIndex(COLUMN_KEY));
            value = c.getString(c.getColumnIndex(COLUMN_VALUE));
        }catch (Exception e){
            e.printStackTrace();
        }
        c.close();
        db.close();
        Log.i(TAG, "Completed getPreference");
        return new Preference(id,key,value);
    }
    public void removePreference(String skey){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PREFERENCES + " WHERE "+ COLUMN_KEY + "= \"" + skey + "\";");
        db.close();
        Log.i(TAG, "Completed removePreference");
    }

}
