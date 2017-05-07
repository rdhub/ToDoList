package com.example.richarddu.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Richard Du on 5/6/2017.
 */

public class ToDoItemDatabase extends SQLiteOpenHelper {
    private static ToDoItemDatabase sInstance;
    // Database Info
    private static final String DATABASE_NAME = "todoItemsDatabase";
    private static final int DATABASE_VERSION = 6;

    // Table Names
    private static final String TABLE_TODOITEMS = "todoitems";

    // Post Table Columns
    private static final String KEY_TODOITEMS_ID = "id";
    private static final String KEY_TODOITEMS_TEXT = "text";
    private static final String KEY_TODOITEMS_RANDID = "randid";
    private static final String KEY_TODOITEMS_PRIORITY = "priority";

    private ToDoItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ToDoItemDatabase getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new ToDoItemDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOITEMS_TABLE = "CREATE TABLE " + TABLE_TODOITEMS +
                "(" +
                KEY_TODOITEMS_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_TODOITEMS_TEXT + " TEXT, " +
                KEY_TODOITEMS_RANDID + " TEXT, " +
                KEY_TODOITEMS_PRIORITY + " TEXT" +
                ")";

        db.execSQL(CREATE_TODOITEMS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOITEMS);
            onCreate(db);
        }
    }

    public int deleteItem(ToDoItem item)
    {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_TODOITEMS, KEY_TODOITEMS_RANDID + " = ?",
                new String[] { String.valueOf(item.id) });
    }
    // Insert a post into the database
    public void addItem(ToDoItem item) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try {


            ContentValues values = new ContentValues();
            values.put(KEY_TODOITEMS_TEXT, item.text);
            values.put(KEY_TODOITEMS_RANDID, ""+item.id);
            values.put(KEY_TODOITEMS_PRIORITY, item.priority);
            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_TODOITEMS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add item to database");
        } finally {
            db.endTransaction();
        }
    }

    public List<ToDoItem> getAllItems() {
        List<ToDoItem> toDoItems = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_TODOITEMS,
                        TABLE_TODOITEMS);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    ToDoItem newItem = new ToDoItem();
                    newItem.text = cursor.getString(cursor.getColumnIndex(KEY_TODOITEMS_TEXT));
                    newItem.id = cursor.getString(cursor.getColumnIndex(KEY_TODOITEMS_RANDID));
                    newItem.priority = cursor.getInt(cursor.getColumnIndex(KEY_TODOITEMS_PRIORITY));
                    toDoItems.add(newItem);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get items from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return toDoItems;
    }

    // Update the user's profile picture url
    public int updateToDoItem(ToDoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TODOITEMS_TEXT, item.text);
        values.put(KEY_TODOITEMS_PRIORITY, item.priority);
        // Updating profile picture url for user with that userName
        return db.update(TABLE_TODOITEMS, values, KEY_TODOITEMS_RANDID + " = ?",
                new String[] { String.valueOf(item.id) });
    }

    public void deleteAllItems() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_TODOITEMS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all items");
        } finally {
            db.endTransaction();
        }
    }
}