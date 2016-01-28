package br.com.appdev.sqlitecidades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CityDatabase extends SQLiteOpenHelper {

    private static final String TAG = "sql";
    public static final String DB_NAME = "cities.sqlite";
    private static final int DB_VERSION = 1;
    Context context;

    public CityDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists cities (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "city TEXT, " +
                "uf TEXT, " +
                "people INTEGER);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertCity(City city) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("city", city.getCity());
        values.put("uf", city.getUf());
        values.put("people", city.getPeople());
        long id = db.insert("cities", "", values);
        city.setId(id);
        db.close();
    }

    public void updateCity(City city) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("city", city.getCity());
        values.put("uf", city.getUf());
        values.put("people", city.getPeople());
        String _id = String.valueOf(city.getId());
        int count = db.update("cities", values, "_id=?", new String[]{_id});
        db.close();
    }

    public void removeCity(City city) {
        SQLiteDatabase db = getWritableDatabase();
        String _id = String.valueOf(city.getId());
        int count = db.delete("cities", "_id=?", new String[]{_id});
        db.close();
    }

    public List<City> getAll() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query("cities", null, null, null, null, null, null, null);
        List<City> cities = new ArrayList<City>();
        if (cursor.moveToFirst()) {
            do {
                City city = new City(
                        cursor.getString(cursor.getColumnIndex("city")),
                        cursor.getInt(cursor.getColumnIndex("people")),
                        cursor.getString(cursor.getColumnIndex("uf")));
                city.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                cities.add(city);
            } while (cursor.moveToNext());
        }

        return cities;
    }

    //O que deve ser inserido no database para atender ao provider
    public long insertByProvider(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert("cities", "", values);
        db.close();
        return id;
    }

    public int removeByProvider(String where, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        int count = db.delete("cities", where, whereArgs);
        db.close();

        return count;
    }

    public int updateByProvider(ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        int count = db.update("cities", values, where, whereArgs);
        db.close();

        return count;
    }
}
