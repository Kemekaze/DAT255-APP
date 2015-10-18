package dat255.busster.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import dat255.busster.Objects.Preference;
import dat255.busster.Resources.Constants;


public class PreferencesDBHandler extends DBHandler {
    public static final String TAG ="dat255.PrefDBHandler";

    public PreferencesDBHandler(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, factory);
    }

    public void addPreference(Preference preference){
        ContentValues values =  new ContentValues();
        values.put(Constants.DB.PREFERENCES.COLUMN_KEY, preference.get_key());
        values.put(Constants.DB.PREFERENCES.COLUMN_VALUE, preference.get_value());
        SQLiteDatabase db = getWritableDatabase();
        db.insertWithOnConflict(Constants.DB.PREFERENCES.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        Log.i(TAG, "Completed addPreference");
    }
    public Preference getPreference(String skey){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + Constants.DB.PREFERENCES.TABLE + " WHERE "+Constants.DB.PREFERENCES.COLUMN_KEY+"= \""+skey+"\";";
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();
        int id=0;
        String key="";
        String value="";
        try {
            id = c.getInt(c.getColumnIndex(Constants.DB.PREFERENCES.COLUMN_ID));
            key = c.getString(c.getColumnIndex(Constants.DB.PREFERENCES.COLUMN_KEY));
            value = c.getString(c.getColumnIndex(Constants.DB.PREFERENCES.COLUMN_VALUE));
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
        db.execSQL("DELETE FROM " + Constants.DB.PREFERENCES.TABLE + " WHERE "+ Constants.DB.PREFERENCES.COLUMN_KEY + "= \"" + skey + "\";");
        db.close();
        Log.i(TAG, "Completed removePreference");
    }

}
