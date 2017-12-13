package wsd17z.togetter.DbManagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kosmos on 10/12/2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TogetterDB";
    // USERS
    private static final String TABLE_USERS = "users";
    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_PASS = "pass";
    private static final String[] USERS_COLUMNS = { KEY_NAME, KEY_SURNAME, KEY_LOGIN, KEY_PASS };
    // OFFERS
    private static final String TABLE_OFFERS = "offers";
    private static final String KEY_ID = "id";
    private static final String KEY_CLIENT = "client";
    private static final String KEY_DRIVER = "driver";
    private static final String KEY_PRICE = "client";
    private static final String KEY_STARTED = "started";
    private static final String KEY_ENDED = "ended";
    private static final String KEY_PAID = "paid";
    private static final String[] OFFERS_COLUMNS = { KEY_ID, KEY_CLIENT, KEY_DRIVER, KEY_PRICE, KEY_STARTED, KEY_ENDED, KEY_PAID };

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStr = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " ( " +
                KEY_NAME + " TEXT, "+
                KEY_SURNAME + " TEXT, "+
                KEY_LOGIN + " TEXT PRIMARY KEY NOT NULL, "+
                KEY_PASS + " INTEGER NOT NULL)";
        db.execSQL(createTableStr);

        createTableStr = "CREATE TABLE IF NOT EXISTS " + TABLE_OFFERS + " ( " +
                KEY_ID + " INT PRIMARY KEY AUTOINCREMENT, "+
                KEY_CLIENT + " TEXT NOT NULL, "+
                " FOREIGN KEY ("+KEY_CLIENT+") REFERENCES "+TABLE_USERS+"("+KEY_LOGIN+"),"+
                KEY_DRIVER + " TEXT NOT NULL, "+
                " FOREIGN KEY ("+KEY_DRIVER+") REFERENCES "+TABLE_USERS+"("+KEY_LOGIN+"),"+
                KEY_PRICE + " REAL NOT NULL"+
                KEY_STARTED + " INTEGER NOT NULL"+
                KEY_ENDED + " INTEGER NOT NULL"+
                KEY_PAID + " INTEGER NOT NULL)";
        db.execSQL(createTableStr);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFERS);

        this.onCreate(db);
    }

    public void addUser(DbUserObject obj){

        Log.d("addUser", obj.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, obj.getName());
        values.put(KEY_SURNAME, obj.getSurname());
        values.put(KEY_LOGIN, obj.getEmail());
        values.put(KEY_PASS, obj.getPassHash());

        db.insert(TABLE_USERS,
                null,
                values);

        db.close();
    }

    public DbUserObject getUser(String login){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(TABLE_USERS,
                        USERS_COLUMNS,
                        KEY_LOGIN + " = ?",
                        new String[] { login },
                        null,
                        null,
                        null,
                        null);

        if (cursor != null)
            cursor.moveToFirst();

        DbUserObject user = new DbUserObject(
            cursor.getString(0),
            cursor.getString(1),
            cursor.getString(2),
            Integer.parseInt(cursor.getString(3))
        );

        Log.d("getUser("+login+")", user.toString());

        return user;
    }

    public List<DbUserObject> getAllUsers() {
        List<DbUserObject> users = new LinkedList<DbUserObject>();

        String query = "SELECT * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                users.add(
                    new DbUserObject(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3))
                    )
                );
            } while (cursor.moveToNext());
        }

        Log.d("getAllUsers()", users.toString());

        return users;
    }

    public int updateUser(DbUserObject obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, obj.getName());
        values.put(KEY_SURNAME, obj.getSurname());
        values.put(KEY_LOGIN, obj.getEmail());
        values.put(KEY_PASS, obj.getPassHash());

        int i = db.update(TABLE_USERS,
                values,
                KEY_LOGIN + " = ?",
                new String[] { obj.getEmail() });

        db.close();

        return i;

    }

    public void deleteUser(DbUserObject obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_USERS,
                KEY_LOGIN+" = ?",
                new String[] { obj.getEmail() });

        db.close();

        Log.d("deleteUser", obj.toString());
    }

    public long addOffer(DbOfferObject obj) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CLIENT, obj.getClientEmail());
        values.put(KEY_DRIVER, obj.getDriverEmail());
        values.put(KEY_PRICE, obj.getPrice());
        values.put(KEY_STARTED, obj.getStarted() ? 1 : 0);
        values.put(KEY_ENDED, obj.getEnded() ? 1 : 0);
        values.put(KEY_PAID, obj.getPaid() ? 1 : 0);

        long id = db.insert(TABLE_OFFERS,
                null,
                values);

        db.close();

        return id;
    }

    public DbOfferObject getOffer(long offerId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(TABLE_OFFERS,
                        OFFERS_COLUMNS,
                        KEY_ID + " = ?",
                        new String[] { String.valueOf(offerId) },
                        null,
                        null,
                        null,
                        null);

        if (cursor != null)
            cursor.moveToFirst();

        DbOfferObject offer = new DbOfferObject(
                cursor.getDouble(3),
                cursor.getInt(4) != 0,
                cursor.getInt(5) != 0,
                cursor.getInt(6) != 0,
                cursor.getString(1),
                cursor.getString(2)
        );

        return offer;
    }

    public int updateOffer(long offerId, DbOfferObject obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CLIENT, obj.getClientEmail());
        values.put(KEY_DRIVER, obj.getDriverEmail());
        values.put(KEY_PRICE, obj.getPrice());
        values.put(KEY_STARTED, obj.getStarted() ? 1 : 0);
        values.put(KEY_ENDED, obj.getEnded() ? 1 : 0);
        values.put(KEY_PAID, obj.getPaid() ? 1 : 0);

        int i = db.update(TABLE_OFFERS,
                values,
                KEY_ID + " = ?",
                new String[] { String.valueOf(offerId) });

        db.close();

        return i;

    }
}
