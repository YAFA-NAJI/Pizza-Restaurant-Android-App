package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static com.example.project.userinfo.UserEntry.TABLE_NAME;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    // Database name and version
    private static final String DATABASE_NAME = "pizzaApp.db";
    private static final int DATABASE_VERSION = 32;

    // Table name and column names for user table
    public static final String TABLE_NAME_USERS = "users";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PROFILE_PICTURE = "profile_picture";
    public static final String COLUMN_FAVORITES = "favorites";
    public static final String TABLE_FAVORITES = "favorites";

    // Table name and column names for admin table
    public static final String TABLE_NAME_ADMINS = "admin";
    public static final String COLUMN_EMAIL_ADMINS = "email";
    public static final String COLUMN_PHONE_ADMINS = "phone";
    public static final String COLUMN_FIRST_NAME_ADMINS = "first_name";
    public static final String COLUMN_LAST_NAME_ADMINS = "last_name";
    public static final String COLUMN_GENDER_ADMINS = "gender";
    public static final String COLUMN_PASSWORD_ADMINS = "password";

    // Tables and columns for orders
    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_PIZZA_NAME = "pizza_name";

    // Table name and column names for special offers
    public static final String TABLE_SPECIAL_OFFERS = "special_offers";
    public static final String COLUMN_SPECIAL_OFFER_NAME = "name";
    public static final String COLUMN_SPECIAL_OFFER_DESCRIPTION = "description";
    public static final String COLUMN_SPECIAL_OFFER_DURATION = "duration";
    public static final String COLUMN_SPECIAL_OFFER_SIZE = "size";
    public static final String COLUMN_SPECIAL_OFFER_PRICE = "total_price";
    public static final String COLUMN_ID = "id";

    // Table name and column names for special offers Admin
    private static final String TABLE_SPECIAL_OFFERS_ADMIN = "special_offers_admin";
    private static final String COLUMN_ID_Admin = "id";
    private static final String COLUMN_SPECIAL_OFFER_NAME_Admin = "name";
    private static final String COLUMN_SPECIAL_OFFER_DESCRIPTION_Admin = "description";
    private static final String COLUMN_SPECIAL_OFFER_OFFER_PERIOD_Admin = "offer_period";
    private static final String COLUMN_SPECIAL_OFFER_TOTAL_PRICE_Admin = "total_price";

    // Table name and column names for pizza table
    public static final String TABLE_PIZZA = "pizza";
    public static final String COLUMN_AVAILABLE_SIZES = "available_sizes";
    public static final String COLUMN_PIZZA_NAME_offer = "pizza_offer";



    // SQL query to create the user table
    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE " + TABLE_NAME_USERS + " (" +
                    COLUMN_EMAIL + " TEXT PRIMARY KEY," +
                    COLUMN_PHONE + " TEXT," +
                    COLUMN_FIRST_NAME + " TEXT," +
                    COLUMN_LAST_NAME + " TEXT," +
                    COLUMN_GENDER + " TEXT," +
                    COLUMN_PASSWORD + " TEXT," +
                    COLUMN_PROFILE_PICTURE + " BLOB," +
                    COLUMN_FAVORITES + " TEXT)";

    // SQL query to create the special offers Admin table
    private static final String CREATE_SPECIAL_OFFERS_ADMIN_TABLE =
            "CREATE TABLE " + TABLE_SPECIAL_OFFERS_ADMIN + "(" +
                    COLUMN_ID_Admin + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_SPECIAL_OFFER_NAME_Admin + " TEXT," +
                    COLUMN_SPECIAL_OFFER_DESCRIPTION_Admin + " TEXT," +
                    COLUMN_SPECIAL_OFFER_OFFER_PERIOD_Admin + " TEXT," +
                    COLUMN_SPECIAL_OFFER_TOTAL_PRICE_Admin + " TEXT" +
                    ")";

    private static final String SQL_CREATE_PIZZA_TABLE =
            "CREATE TABLE " + TABLE_PIZZA + " (" +
                    COLUMN_PIZZA_NAME_offer + " TEXT PRIMARY KEY," +
                    COLUMN_AVAILABLE_SIZES + " TEXT)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the users table
        db.execSQL(SQL_CREATE_USERS_TABLE);

        // Create the favorites table
        String createFavoritesTableQuery = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COLUMN_USER_EMAIL + " TEXT," +
                COLUMN_PIZZA_NAME + " TEXT," +
                "PRIMARY KEY (" + COLUMN_USER_EMAIL + ", " + COLUMN_PIZZA_NAME + "))";
        db.execSQL(createFavoritesTableQuery);

        // Create the special offers table if it doesn't exist
        String createSpecialOffersTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_SPECIAL_OFFERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SPECIAL_OFFER_NAME + " TEXT, " +
                COLUMN_SPECIAL_OFFER_DESCRIPTION + " TEXT, " +
                COLUMN_SPECIAL_OFFER_SIZE + " TEXT, " +
                COLUMN_SPECIAL_OFFER_DURATION + " TEXT, " +
                COLUMN_SPECIAL_OFFER_PRICE + " REAL" +
                ")";
        db.execSQL(createSpecialOffersTableQuery);

        // Create the special offers admin table
        db.execSQL(CREATE_SPECIAL_OFFERS_ADMIN_TABLE);

        // Create the pizza table
        db.execSQL(SQL_CREATE_PIZZA_TABLE);

        // Execute the query to insert sample data into the pizza table
        String INSERT_SAMPLE_DATA =
                "INSERT INTO " + TABLE_PIZZA + " (" + COLUMN_PIZZA_NAME_offer + ", " + COLUMN_AVAILABLE_SIZES + ") VALUES " +
                        "('Margarita,Pepperoni', 'Small,Medium,Large')," +
                        "('Hawaiian,New York Style', 'Small,Medium,Large')," +
                        "('Calzone,Tandoori Chicken', 'Small,Medium,Large')," +
                        "('BBQ Chicken,Seafood Pizza', 'Small,Medium,Large')," +
                        "('Vegetarian,Buffalo Chicken', 'Small,Medium,Large')";

        db.execSQL(INSERT_SAMPLE_DATA);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECIAL_OFFERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECIAL_OFFERS_ADMIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIZZA);


        // Create tables again
        onCreate(db);
    }


    // Method to add admin information to the database
    public long addAdminInfo(SQLiteDatabase db) {
        String adminEmail = Admininfo.DEFAULT_ADMIN_EMAIL;
        String adminPhone = ""; // يجب عادةً معالجة حالة رقم الهاتف أيضًا
        String adminFirstName = ""; // يمكنك تعيين الاسم الأول حسب الحاجة
        String adminLastName = ""; // يمكنك تعيين الاسم الأخير حسب الحاجة
        String adminGender = ""; // يمكنك تعيين الجنس حسب الحاجة
        String adminPassword = ""; // استخدام كلمة المرور الافتراضية

        // التأكد من أن كلمة المرور ليست فارغة
        if(adminPassword.isEmpty()) {
            // يمكنك التعامل مع هذه الحالة بطرق مختلفة، هنا يتم رفض إدخال كلمة مرور فارغة
            return -1;
        }

        // تشفير كلمة المرور
        String encryptedPassword = encryptPassword(adminPassword);

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL_ADMINS, adminEmail);
        values.put(COLUMN_PHONE_ADMINS, adminPhone);
        values.put(COLUMN_FIRST_NAME_ADMINS, adminFirstName);
        values.put(COLUMN_LAST_NAME_ADMINS, adminLastName);
        values.put(COLUMN_GENDER_ADMINS, adminGender);
        values.put(COLUMN_PASSWORD_ADMINS, encryptedPassword);

        long result = db.insert(TABLE_NAME_ADMINS, null, values);
        return result;
    }


    // Helper method to encrypt password
    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] byteData = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : byteData) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Add a method to update the customer's profile picture in the database
    public boolean updateUserInfo(String email, String phone, String firstName, String lastName, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_FIRST_NAME, firstName);
        contentValues.put(COLUMN_LAST_NAME, lastName);
        contentValues.put(COLUMN_PASSWORD, password);
        int rowsAffected = db.update(TABLE_NAME_USERS, contentValues, COLUMN_EMAIL + "=?", new String[]{email});
        db.close(); // Close the database connection
        return rowsAffected > 0;
    }


    public Cursor getUserInfoByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_USERS, null, COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public Cursor getAdminInfoByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_ADMINS, null, COLUMN_EMAIL_ADMINS + "=?", new String[]{email}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public boolean updateAdminInfo(String email, String firstName, String lastName, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME_ADMINS, firstName);
        values.put(COLUMN_LAST_NAME_ADMINS, lastName);
        values.put(COLUMN_PHONE_ADMINS, phone);
        values.put(COLUMN_PASSWORD_ADMINS, password); // إضافة كلمة المرور إلى القيم المحدثة
        String selection = COLUMN_EMAIL_ADMINS + " = ?";
        String[] selectionArgs = { email };
        int rowsAffected = db.update(TABLE_NAME_ADMINS, values, selection, selectionArgs);
        return rowsAffected > 0;
    }



    public boolean updateCustomerProfilePicture(String userEmail, byte[] profilePicture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILE_PICTURE, profilePicture);

        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { userEmail };

        int rowsAffected = db.update(TABLE_NAME_USERS, values, selection, selectionArgs);
        return rowsAffected > 0;
    }

    public boolean addToFavorites(String pizzaName, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the table exists, if not, create it
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITES + " (" +
                COLUMN_USER_EMAIL + " TEXT," +
                COLUMN_PIZZA_NAME + " TEXT," +
                "PRIMARY KEY (" + COLUMN_USER_EMAIL + ", " + COLUMN_PIZZA_NAME + "))");

        // Check if the item is already in the favorites
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_FAVORITES + " WHERE " + COLUMN_USER_EMAIL + "=? AND " + COLUMN_PIZZA_NAME + "=?", new String[]{userEmail, pizzaName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();

        if (exists) {
            // Item already exists in the favorites
            return false;
        } else {
            // Item does not exist in the favorites, so add it
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_EMAIL, userEmail);
            values.put(COLUMN_PIZZA_NAME, pizzaName);
            long result = db.insert(TABLE_FAVORITES, null, values);
            return result != -1;
        }
    }


    // Method to remove a pizza from favorites
    public boolean removeFromFavorites(String pizzaName, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_PIZZA_NAME + "=? AND " + COLUMN_USER_EMAIL + "=?";
        String[] whereArgs = {pizzaName, userEmail};
        int result = db.delete(TABLE_FAVORITES, whereClause, whereArgs);
        return result > 0;
    }

    // Method to get all special offers
    public Cursor getAllSpecialOffers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_ID,
                COLUMN_SPECIAL_OFFER_NAME,
                COLUMN_SPECIAL_OFFER_DESCRIPTION,
                COLUMN_SPECIAL_OFFER_SIZE,
                COLUMN_SPECIAL_OFFER_DURATION,
                COLUMN_SPECIAL_OFFER_PRICE
        };
        return db.query(TABLE_SPECIAL_OFFERS, projection, null, null, null, null, null);
    }




    public boolean addSpecialOffer(String name, String description, String size, double price, String duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SPECIAL_OFFER_NAME, name);
        contentValues.put(COLUMN_SPECIAL_OFFER_DESCRIPTION, description);
        contentValues.put(COLUMN_SPECIAL_OFFER_SIZE, size);
        contentValues.put(COLUMN_SPECIAL_OFFER_DURATION, price);
        contentValues.put(COLUMN_SPECIAL_OFFER_PRICE, duration);

        long result = db.insert(TABLE_SPECIAL_OFFERS, null, contentValues);
        return result != -1;
    }






    // Method to insert admin info into the database
    public boolean insertAdmin(String email, String phone, String firstName, String lastName, String gender, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL_ADMINS, email);
        contentValues.put(COLUMN_PHONE_ADMINS, phone);
        contentValues.put(COLUMN_FIRST_NAME_ADMINS, firstName);
        contentValues.put(COLUMN_LAST_NAME_ADMINS, lastName);
        contentValues.put(COLUMN_GENDER_ADMINS, gender);
        contentValues.put(COLUMN_PASSWORD_ADMINS, password);

        long result = -1;
        try {
            result = db.insertOrThrow(TABLE_NAME_ADMINS, null, contentValues);
            // Log a success message if the insertion was successful
            Log.d("DatabaseHelper", "Admin inserted successfully");
        } catch (SQLiteConstraintException e) {
            // Log the error if there was a constraint violation
            Log.e("DatabaseHelper", "Error inserting admin: " + e.getMessage());
        } finally {
            db.close(); // Close the database connection
        }

        return result != -1;
    }


    public byte[] getUserProfilePicture(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        byte[] profilePicture = null;

        String[] projection = { COLUMN_PROFILE_PICTURE };
        String selection = COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };

        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            profilePicture = cursor.getBlob(cursor.getColumnIndex(COLUMN_PROFILE_PICTURE));
            cursor.close();
        }

        db.close();

        return profilePicture;
    }



    public String getPizzaPrice(String pizzaName, String pizzaSize) {
        SQLiteDatabase db = this.getReadableDatabase();
        String price = null;

        Cursor cursor = db.query(
                TABLE_SPECIAL_OFFERS, // Table to query
                new String[]{COLUMN_SPECIAL_OFFER_PRICE}, // Columns to return
                COLUMN_SPECIAL_OFFER_NAME + " = ? AND " + COLUMN_SPECIAL_OFFER_SIZE + " = ?", // Selection (WHERE clause)
                new String[]{pizzaName, pizzaSize}, // Selection arguments
                null, // Group by
                null, // Having
                null // Order by
        );

        if (cursor != null && cursor.moveToFirst()) {
            // If the cursor is not null and it moves to the first row, extract the price
            price = cursor.getString(cursor.getColumnIndex(COLUMN_SPECIAL_OFFER_PRICE));
            cursor.close();
        }

        // Close the cursor and the database
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        // Return the price
        return price;
    }


    public String[] getSizesOnOfferForPizza(String pizzaName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] sizes = null;

        Cursor cursor = db.query(TABLE_PIZZA, new String[]{COLUMN_AVAILABLE_SIZES}, COLUMN_PIZZA_NAME_offer + "=?", new String[]{pizzaName}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            // If the pizza exists, extract the available sizes
            String availableSizesString = cursor.getString(cursor.getColumnIndex(COLUMN_AVAILABLE_SIZES));
            sizes = availableSizesString.split(",");
            cursor.close();
        }

        // Close the cursor and the database
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        // Return the array of available sizes
        return sizes;
    }


}

