package com.example.project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper2 extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "pizza_db";

    // Database Version
    private static final int DATABASE_VERSION = 7;
    public static final String TABLE_PIZZA_DETAILS = "pizza_details";
    public static final String KEY_TYPE_NAME = "type_name";
    public static final String KEY_PRICE_SMALL = "price_small";
    public static final String KEY_PRICE_MEDIUM = "price_medium";
    public static final String KEY_PRICE_LARGE = "price_large";
    // Table Names
    private static final String TABLE_TYPES = "types";
    private static final String TABLE_SIZES = "sizes";
    private static final String TABLE_PRICES = "prices";

    // Table offers
    // Define the key for the image resource ID column
    private static final String KEY_IMAGE_RESOURCE_ID = "image_resource_id";


    // Create table SQL statement for pizza details
    // Common column names
    private static final String KEY_ID = "id"; // Declare KEY_ID before CREATE_TABLE_PIZZA_DETAILS

    // New column names for pizza details table
    private static final String KEY_PIZZA_ID = "pizza_id";
    private static final String KEY_COMPONENTS = "components";

    // Common column names
    public static final String KEY_SIZE = "size";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_TYPE_ID = "type_id"; // Define KEY_TYPE_ID

    // Create table SQL statement for pizza details
    private static final String CREATE_TABLE_PIZZA_DETAILS = "CREATE TABLE " + TABLE_PIZZA_DETAILS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PIZZA_ID + " INTEGER,"
            + KEY_COMPONENTS + " TEXT,"
            + KEY_PRICE_SMALL + " REAL,"
            + KEY_PRICE_MEDIUM + " REAL,"
            + KEY_PRICE_LARGE + " REAL,"
            + KEY_SIZE + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_TYPE_ID + " INTEGER," // Include KEY_TYPE_ID in CREATE_TABLE_PIZZA_DETAILS
            + KEY_IMAGE_RESOURCE_ID + " INTEGER" + ")";

    // Create table SQL statements
    private static final String CREATE_TABLE_TYPES = "CREATE TABLE " + TABLE_TYPES +
            "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TYPE_NAME + " TEXT)";

    private static final String CREATE_TABLE_SIZES = "CREATE TABLE " + TABLE_SIZES +
            "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SIZE + " TEXT)";

    private static final String CREATE_TABLE_PRICES = "CREATE TABLE " + TABLE_PRICES +
            "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TYPE_ID + " INTEGER," +
            KEY_ID + " INTEGER," + KEY_PRICE_SMALL + " REAL," +
            KEY_PRICE_MEDIUM + " REAL," + KEY_PRICE_LARGE + " REAL," +
            " FOREIGN KEY(" + KEY_TYPE_ID + ") REFERENCES " + TABLE_TYPES + "(" + KEY_ID + ")," +
            " FOREIGN KEY(" + KEY_ID + ") REFERENCES " + TABLE_SIZES + "(" + KEY_ID + "))";

    public DatabaseHelper2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_TYPES);
        db.execSQL(CREATE_TABLE_SIZES);
        db.execSQL(CREATE_TABLE_PRICES);
        db.execSQL(CREATE_TABLE_PIZZA_DETAILS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIZES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRICES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIZZA_DETAILS);

        // create new tables
        onCreate(db);
    }

}
