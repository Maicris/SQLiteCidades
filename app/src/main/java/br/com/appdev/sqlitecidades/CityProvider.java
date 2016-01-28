package br.com.appdev.sqlitecidades;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

public class CityProvider extends ContentProvider {

    private CityDatabase database;

    private static HashMap<String, String> columns;
    private static final int CITIES = 1;
    private static final int CITIES_ID = 2;
    private UriMatcher uriCity;

    private static String getAuthority() {
        return "br.com.appdev.sqlitecidades";
    }

    private static final class Cities implements BaseColumns {

        private Cities() {}

        public static final Uri CONTENT_URI = Uri.parse("content://"+getAuthority()+"/cities");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.googlo.cities";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.cities";

        public static final String CITY = "city";
        public static final String UF = "uf";
        public static final String PEOPLE = "people";

        public static Uri getUriId(long id) {
            Uri uriCity = ContentUris.withAppendedId(Cities.CONTENT_URI, id);
            return uriCity;
        }
    }

    @Override
    public boolean onCreate() {

        uriCity = new UriMatcher(UriMatcher.NO_MATCH);
        uriCity.addURI(getAuthority(), "cities", CITIES);
        uriCity.addURI(getAuthority(), "cities/#", CITIES_ID);
        columns = new HashMap<String, String>();
        columns.put(Cities._ID, Cities._ID);
        columns.put(Cities.CITY, Cities.CITY);
        columns.put(Cities.UF, Cities.UF);
        columns.put(Cities.PEOPLE, Cities.PEOPLE);
        database = new CityDatabase(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        if (projection == null) {
            projection = new String[] {"_id", Cities.CITY, Cities.UF, Cities.PEOPLE};
        }

        switch (uriCity.match(uri)) {
            case CITIES:
                builder.setTables("cities");
                builder.setProjectionMap(columns);
                break;
            case CITIES_ID:
                builder.setTables("cities");
                builder.setProjectionMap(columns);
                builder.appendWhere(Cities._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Uri Desconhecida " + uri);
        }

        SQLiteDatabase db = this.database.getReadableDatabase();
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (uriCity.match(uri)) {
            case CITIES:
                return Cities.CONTENT_TYPE;
            case CITIES_ID:
                return Cities.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Uri Desconhecida: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {

        if (uriCity.match(uri) != CITIES) {
            throw new IllegalArgumentException("Uri Desconhecida: " + uri);
        }
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }
        long id = database.insertByProvider(values);
        if (id > 0) {
            Uri uriCity = Cities.getUriId(id);
            getContext().getContentResolver().notifyChange(uriCity, null);
            return uriCity;
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (uriCity.match(uri)) {
            case CITIES:
                count = database.removeByProvider(selection, selectionArgs);
                break;
            case CITIES_ID:
                String id = uri.getPathSegments().get(1);
                String whereFinal = Cities._ID + "=" + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                count = database.removeByProvider(whereFinal, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Uri Desconhecida: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int count;
        switch (uriCity.match(uri)) {
            case CITIES:
                count = database.updateByProvider(values, selection, selectionArgs);
                break;
            case CITIES_ID:
                String id = uri.getPathSegments().get(1);
                String whereFinal = Cities._ID + "=" + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                count = database.updateByProvider(values, whereFinal, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI Desconhecida: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
