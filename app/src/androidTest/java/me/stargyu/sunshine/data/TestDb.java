package me.stargyu.sunshine.data;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase() {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(WeatherContract.LocationEntry.TABLE_NAME);
        tableNameHashSet.add(WeatherContract.WeatherEntry.TABLE_NAME);

        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + WeatherContract.LocationEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(WeatherContract.LocationEntry._ID);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_CITY_NAME);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LAT);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LONG);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }

    public void testLocationTable() {
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();

        long locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, testValues);
        assertTrue(locationRowId != -1);
        // 삽입 검사

        Cursor cursor = db.query(
                WeatherContract.LocationEntry.TABLE_NAME, // table
                null, // colums
                null, // selection
                null, // selectionArgs
                null, // groupBy
                null, // having
                null //orderBy
        );
        assertTrue("Error : No Records retuned from location query", cursor.moveToFirst());
        // select 검사

        TestUtilities.validateCurrentRecord("Error : Location Query Validation Failed", cursor, testValues);
        // integrity

        assertFalse("Error: More than one record returned from location query", cursor.moveToNext());

        cursor.close();
        db.close();
        // 커서, 디비 닫음
    } // insert, select, integrity(무결성), 레코드 검사, 커서/디비 닫음

    public void testWeatherTable() {
        long locationRowId = insertLocation();
        assertTrue(locationRowId != -1L);

        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues weatherValues = TestUtilities.createWeatherValues(locationRowId);

        long weatherRowId =
                db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, weatherValues);
        assertTrue(weatherRowId != -1L);
        // insert

        Cursor weatherCursor = db.query(
                WeatherContract.WeatherEntry.TABLE_NAME, // table
                null, // columns
                null, // selection
                null, // selectionArgs
                null, // groupBy
                null, // having
                null //orderBy
        );
        assertTrue("No Records retuned from Weather query", weatherCursor.moveToFirst());
        // select

        TestUtilities.validateCurrentRecord("Weather Query Validation Failed", weatherCursor, weatherValues);
        // integrity

        assertFalse("More than one record returned from Weather query", weatherCursor.moveToNext());
        // record check

        weatherCursor.close();
        db.close();
    }


    public long insertLocation() {
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues northPoleLocationValues = TestUtilities.createNorthPoleLocationValues();

        long locationRowId =
                db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, northPoleLocationValues);
        assertTrue(locationRowId != -1);
        // insert

        Cursor locationCursor = db.query(
                WeatherContract.LocationEntry.TABLE_NAME, // table
                null, // columns
                null, // selection
                null, // selectionArgs
                null, // groupBy
                null, // having
                null //orderBy
        );
        assertTrue("No Records retuned from Location query", locationCursor.moveToFirst());
        // select

        TestUtilities.validateCurrentRecord(
                "Location Cursor Validation Failed", locationCursor, northPoleLocationValues);
        // integrity

        assertFalse("More than one record in Location query", locationCursor.moveToNext());
        // record check

        locationCursor.close();
        db.close();
        return locationRowId;
    }
}