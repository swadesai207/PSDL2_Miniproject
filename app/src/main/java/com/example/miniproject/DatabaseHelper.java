package com.example.miniproject;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
//
//import java.util.ArrayList;
//
//public class DatabaseHelper extends SQLiteOpenHelper {
//
//    private static final String DATABASE_NAME = "Orders.db";
//    private static final int DATABASE_VERSION = 1;
//
//    private static final String TABLE_ORDERS = "previous_orders";
//    private static final String COLUMN_ID = "id";
//    private static final String COLUMN_ORDER_DETAILS = "orderDetails";
//    private static final String COLUMN_TOTAL_BILL = "totalBill";
//    private static final String COLUMN_TIMESTAMP = "timestamp";
//
//    // Constructor
//    public DatabaseHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    // Called when the database is created for the first time
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        try {
//            String createTable = "CREATE TABLE " + TABLE_ORDERS + " (" +
//                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    COLUMN_ORDER_DETAILS + " TEXT, " +
//                    COLUMN_TOTAL_BILL + " INTEGER, " +
//                    COLUMN_TIMESTAMP + " TEXT)";
//            db.execSQL(createTable);
//        } catch (Exception e) {
//            Log.e("DatabaseHelper", "Error creating table", e);
//        }
//    }
//
//    // Called when the database needs to be upgraded
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        try {
//            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
//            onCreate(db);
//        } catch (Exception e) {
//            Log.e("DatabaseHelper", "Error upgrading table", e);
//        }
//    }
//
//    // Insert a new order into the database
//    public void insertOrder(String orderDetails, int totalBill, String timestamp) {
//        SQLiteDatabase db = null;
//        try {
//            db = this.getWritableDatabase();
//            ContentValues values = new ContentValues();
//            values.put(COLUMN_ORDER_DETAILS, orderDetails);
//            values.put(COLUMN_TOTAL_BILL, totalBill);
//            values.put(COLUMN_TIMESTAMP, timestamp);
//
//            long result = db.insert(TABLE_ORDERS, null, values);
//            if (result == -1) {
//                Log.e("DatabaseHelper", "Error inserting order");
//            } else {
//                Log.i("DatabaseHelper", "Order inserted successfully");
//            }
//        } catch (Exception e) {
//            Log.e("DatabaseHelper", "Error inserting order", e);
//        } finally {
//            if (db != null) {
//                db.close();
//            }
//        }
//    }
//
//    // Retrieve all orders from the database
//    public ArrayList<String> getAllOrders() {
//        ArrayList<String> ordersList = new ArrayList<>();
//        SQLiteDatabase db = null;
//        Cursor cursor = null;
//
//        try {
//            db = this.getReadableDatabase();
//            cursor = db.query(TABLE_ORDERS, null, null, null, null, null, COLUMN_ID + " DESC");
//
//            if (cursor != null && cursor.moveToFirst()) {
//                do {
//                    String orderDetails = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DETAILS));
//                    int totalBill = cursor.getInt(cursor.getColumnIndex(COLUMN_TOTAL_BILL));
//                    String timestamp = cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP));
//
//                    ordersList.add(orderDetails + "\nTotal: ₹" + totalBill + "\nDate: " + timestamp);
//                } while (cursor.moveToNext());
//            }
//        } catch (Exception e) {
//            Log.e("DatabaseHelper", "Error retrieving orders", e);
//        } finally {
//            // Ensure cursor and database are closed
//            if (cursor != null) cursor.close();
//            if (db != null) db.close();
//        }
//
//        return ordersList;
//    }
//}


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Orders.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ORDERS = "previous_orders";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ORDER_DETAILS = "orderDetails";
    private static final String COLUMN_TOTAL_BILL = "totalBill";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String createTable = "CREATE TABLE " + TABLE_ORDERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDER_DETAILS + " TEXT, " +
                    COLUMN_TOTAL_BILL + " INTEGER, " +
                    COLUMN_TIMESTAMP + " TEXT)";
            db.execSQL(createTable);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error creating table", e);
        }
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            onCreate(db);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error upgrading table", e);
        }
    }

    // Insert a new order into the database
    public void insertOrder(String orderDetails, int totalBill, String timestamp) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_ORDER_DETAILS, orderDetails);
            values.put(COLUMN_TOTAL_BILL, totalBill);
            values.put(COLUMN_TIMESTAMP, timestamp);

            long result = db.insert(TABLE_ORDERS, null, values);
            if (result == -1) {
                Log.e("DatabaseHelper", "Error inserting order");
            } else {
                Log.i("DatabaseHelper", "Order inserted successfully");
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting order", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    // Retrieve all orders from the database and convert the timestamp
    public ArrayList<String> getAllOrders() {
        ArrayList<String> ordersList = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_ORDERS, null, null, null, null, null, COLUMN_ID + " DESC");

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String orderDetails = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DETAILS));
                    @SuppressLint("Range") int totalBill = cursor.getInt(cursor.getColumnIndex(COLUMN_TOTAL_BILL));
                    @SuppressLint("Range") long timestampMillis = cursor.getLong(cursor.getColumnIndex(COLUMN_TIMESTAMP));

                    // Convert the timestamp to a human-readable date and time format
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String formattedDate = sdf.format(new Date(timestampMillis));

                    ordersList.add(orderDetails + "\nTotal: ₹" + totalBill + "\nDate: " + formattedDate);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error retrieving orders", e);
        } finally {
            // Ensure cursor and database are closed
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return ordersList;
    }
}
