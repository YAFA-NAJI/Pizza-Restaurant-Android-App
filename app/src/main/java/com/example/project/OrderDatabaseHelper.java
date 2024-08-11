package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.project.AllOrders.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "order_database.db";
    private static final int DATABASE_VERSION = 20;

    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_PIZZA_NAME = "pizza_name";
    private static final String COLUMN_SIZE = "size";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_UNIT_PRICE = "unit_price";
    private static final String COLUMN_TOTAL_PRICE = "total_price";
    private static final String COLUMN_DATE_TIME = "date_time";
    public static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_CUSTOMER_NAME = "customer_name";

    public OrderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_PIZZA_NAME + " TEXT,"
                + COLUMN_SIZE + " TEXT,"
                + COLUMN_QUANTITY + " INTEGER,"
                + COLUMN_UNIT_PRICE + " REAL,"
                + COLUMN_TOTAL_PRICE + " REAL,"
                + COLUMN_DATE_TIME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_CUSTOMER_NAME + " TEXT"
                + ")";
        db.execSQL(CREATE_ORDERS_TABLE);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public boolean addOrder(String pizzaName, String size, int quantity, double unitPrice, double totalPrice, String dateTime, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PIZZA_NAME, pizzaName);
        values.put(COLUMN_SIZE, size);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT_PRICE, unitPrice);
        values.put(COLUMN_TOTAL_PRICE, totalPrice);
        values.put(COLUMN_DATE_TIME, dateTime);
        values.put(COLUMN_USER_EMAIL, userEmail);

        long result = db.insert(TABLE_ORDERS, null, values);
        db.close();

        return result != -1;
    }
    public List<String> getOrderDetailsByPizzaName(String pizzaName) {
        List<String> orderDetails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {COLUMN_SIZE, COLUMN_QUANTITY, COLUMN_UNIT_PRICE, COLUMN_TOTAL_PRICE, COLUMN_DATE_TIME};
        String selection = COLUMN_PIZZA_NAME + "=?";
        String[] selectionArgs = {pizzaName};

        Cursor cursor = db.query(TABLE_ORDERS, projection, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String size = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SIZE));
            String quantity = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
            String unitPrice = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UNIT_PRICE));
            String totalPrice = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_PRICE));
            String dateTime = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE_TIME));

            // Add all details to the list
            orderDetails.add(size);
            orderDetails.add(quantity);
            orderDetails.add(unitPrice);
            orderDetails.add(totalPrice);
            orderDetails.add(dateTime);

            cursor.close();
        }

        db.close();
        return orderDetails;
    }

    public List<String> getAllPizzaNamesForUser(String userEmail) {
        List<String> pizzaNames = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String[] projection = {COLUMN_PIZZA_NAME};
        String selection = COLUMN_USER_EMAIL + "=?";
        String[] selectionArgs = {userEmail};
        Cursor cursor = db.query(TABLE_ORDERS, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String pizzaName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PIZZA_NAME));
                pizzaNames.add(pizzaName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return pizzaNames;
    }

    // Method to retrieve all orders for a specific user
    public List<Order> getAllOrdersForUser() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS,null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String pizzaName = cursor.getString(cursor.getColumnIndex(COLUMN_PIZZA_NAME));
                String size = cursor.getString(cursor.getColumnIndex(COLUMN_SIZE));
                String quantity = cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY));
                String unitPrice = cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_PRICE));
                String totalPrice = cursor.getString(cursor.getColumnIndex(COLUMN_TOTAL_PRICE));
                String dateTime = cursor.getString(cursor.getColumnIndex(COLUMN_DATE_TIME));
                String customerName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL));

                orders.add(new Order(pizzaName, size, quantity, unitPrice, totalPrice, dateTime, customerName));
            }
            cursor.close();
        } else {
            Log.e("DatabaseHelper", "Cursor is null in getAllOrdersForUser()");
        }
        db.close();
        return orders;
    }


    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS, null, null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String pizzaName = cursor.getString(cursor.getColumnIndex(COLUMN_PIZZA_NAME));
                String size = cursor.getString(cursor.getColumnIndex(COLUMN_SIZE));
                String quantity = cursor.getString(cursor.getColumnIndex(COLUMN_QUANTITY));
                String unitPrice = cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_PRICE));
                String totalPrice = cursor.getString(cursor.getColumnIndex(COLUMN_TOTAL_PRICE));
                String dateTime = cursor.getString(cursor.getColumnIndex(COLUMN_DATE_TIME));
                String customerName = cursor.getString(cursor.getColumnIndex(COLUMN_CUSTOMER_NAME));

                orders.add(new Order(pizzaName, size, quantity, unitPrice, totalPrice, dateTime, customerName));
            }
            cursor.close();
        } else {
            Log.d("getAllOrders", "Cursor is null");
        }

        db.close();
        Log.d("getAllOrders", "Retrieved " + orders.size() + " orders");
        return orders;
    }


}
