package santos.hmarco.loginsignup;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecordDatabase extends SQLiteOpenHelper {

    static final String DBNAME = "records.db";
    static final String DBTABLE = "records";
    static final String DBFN = "FirstName";
    static final String DBMN = "MiddleName";
    static final String DBLN = "LastName";
    static final String DBUN = "Username";
    static final String DBPW = "Password";
    static final String DBAD = "Address";
    static final String DBGE = "Gender";
    static final String DBEM = "Email";
    static final String DBCO = "Contact";
    static final String DBAM = "isAdmin";
    static final String DBCF = "isConfirmed";

    SQLiteDatabase db;
    Context cont;

    // SQL queries
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBTABLE + " (" +
                    "id INTEGER PRIMARY KEY," +
                    DBFN + " TEXT," +
                    DBMN + " TEXT," +
                    DBLN + " TEXT," +
                    DBUN + " TEXT," +
                    DBPW + " TEXT," +
                    DBAD + " TEXT," +
                    DBCO + " TEXT," +
                    DBGE + " TEXT," +
                    DBEM + " TEXT," +
                    DBAM + " TEXT," +
                    DBCF + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBTABLE;

    public RecordDatabase(@Nullable Context context) {
        super(context, DBNAME, null, 1);
        cont = context;
        hasAdmin();
    }

    private boolean hasAdmin() {
        //Toast.makeText(cont, "hasAdmin", Toast.LENGTH_SHORT).show();
        ArrayList<String> info = this.getRecord(1);
        //Toast.makeText(cont, "hasAdmin " + String.valueOf(info.size()), Toast.LENGTH_SHORT).show();
        if (info.size() > 0) {
            return true;
        }

        addRecord("Admin", "Admin", "Admin", "admin", "admin", "Admin", "Admin", "2", "Admin", "true", "true");
        return false;
    }

    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            Log.d("Password", hexString.toString());
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

        //Toast.makeText(cont, "Tosst", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_CREATE_ENTRIES);
        onCreate(db);
    }

    public ArrayList<Integer> getRecordIds() {
        ArrayList<Integer> records = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "id",
                DBFN,
                DBMN,
                DBLN,
                DBUN,
                DBPW,
                DBAD,
                DBCO,
                DBGE,
                DBEM,
                DBAM,
                DBCF
        };

        Cursor cursor = db.query(DBTABLE, projection, null, null, null, null, null);

        while(cursor.moveToNext()) {
            int itemId = cursor.getInt(0);
            records.add(itemId);
        }
        cursor.close();

        return records;
    }

    public ArrayList<String> getGuestIds() {
        ArrayList<String> records = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "id",
                DBFN,
                DBMN,
                DBLN,
                DBUN,
                DBPW,
                DBAD,
                DBCO,
                DBGE,
                DBEM,
                DBAM,
                DBCF
        };

        String selection = DBCF + " = 'false'";

        Cursor cursor = db.query(DBTABLE, projection, selection, null, null, null, null);

        while(cursor.moveToNext()) {
            int itemId = cursor.getInt(0);
            String username = cursor.getString(4);
            records.add(String.valueOf(itemId) + " " + username);
        }
        cursor.close();

        return records;
    }

    public ArrayList<String> getStrings() {
        ArrayList<String> records = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "id",
                DBFN,
                DBMN,
                DBLN,
                DBUN,
                DBPW,
                DBAD,
                DBCO,
                DBGE,
                DBEM,
                DBAM,
                DBCF
        };

        Cursor cursor = db.query(DBTABLE, projection, null, null, null, null, null);

        while(cursor.moveToNext()) {
            int itemId = cursor.getInt(0);
            String username = cursor.getString(4);
            records.add(String.valueOf(itemId) + " " + username);
        }
        cursor.close();

        return records;
    }

    public int login(String un, String pw) {
        List records = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();

        //pw = hashPassword(pw);

        String[] projection = {
                "id"
        };

        String selection = DBUN + " = ? AND " + DBPW + " = ? AND " + DBCF + " = 'true'";
        String[] selectionArgs = { un, pw };

        Cursor cursor = db.query(DBTABLE, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            // If the cursor has a result, retrieve the ID
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            cursor.close();
            return id;
        } else {
            // No matching record found
            cursor.close();
            return -1;
        }
    }

    public boolean isAdmin(int id) {
        List records = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "id",
                DBAM
        };

        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(DBTABLE, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            // If the cursor has a result, retrieve the isAdmin value
            @SuppressLint("Range") String isAdminValue = cursor.getString(cursor.getColumnIndex(DBAM));
            cursor.close();
            return "true".equals(isAdminValue);
        } else {
            // No matching record found
            cursor.close();
            return false;
        }
    }

    public boolean isConfirm(int id) {
        List records = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "id",
                DBCF
        };

        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(DBTABLE, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            // If the cursor has a result, retrieve the isAdmin value
            @SuppressLint("Range") String isAdminValue = cursor.getString(cursor.getColumnIndex(DBCF));
            cursor.close();
            return "true".equals(isAdminValue);
        } else {
            // No matching record found
            cursor.close();
            return false;
        }
    }

    public boolean isUnique(String fn, String mn, String ln, String un) {
        List records = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "id",
                DBFN,
                DBMN,
                DBLN,
                DBUN
        };

        String selection = DBFN + " = ? AND " + DBMN + " = ? AND " + DBLN + " = ? AND " + DBUN + " = ?";
        String[] selectionArgs = { fn, mn, ln, un };

        Cursor cursor = db.query(DBTABLE, projection, selection, selectionArgs, null, null, null);
        boolean isunique = cursor.getCount() == 0;
        cursor.close();
        return isunique;
    }

    public ArrayList<String> getRecord(int id) {
        ArrayList<String> trio = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "id",
                DBFN,
                DBMN,
                DBLN,
                DBUN,
                DBPW,
                DBAD,
                DBCO,
                DBGE,
                DBEM,
                DBAM,
                DBCF
        };

        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(DBTABLE, projection, selection, selectionArgs, null, null, null);

        while(cursor.moveToNext()) {
            String fn = cursor.getString(1);
            String mn = cursor.getString(2);
            String ln = cursor.getString(3);
            String un = cursor.getString(4);
            String pw = cursor.getString(5);
            String ad = cursor.getString(6);
            String co = cursor.getString(7);
            String ge = cursor.getString(8);
            String em = cursor.getString(9);
            String am = cursor.getString(10);
            String cf = cursor.getString(11);
            trio.add(fn);
            trio.add(mn);
            trio.add(ln);
            trio.add(un);
            trio.add(pw);
            trio.add(ad);
            trio.add(co);
            trio.add(ge);
            trio.add(em);
            trio.add(am);
            trio.add(cf);
        }
        cursor.close();

        return trio;
    }

    public long addRecord(String fn, String mn, String ln, String un, String pw, String ad, String co, String ge, String em, String am, String cf) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBFN, fn);
        values.put(DBMN, mn);
        values.put(DBLN, ln);
        values.put(DBUN, un);
        values.put(DBPW, pw);
        values.put(DBAD, ad);
        values.put(DBCO, co);
        values.put(DBGE, ge);
        values.put(DBEM, em);
        values.put(DBAM, am);
        values.put(DBCF, cf);

        long id = db.insert(DBTABLE, null, values);
        return id;
    }

    public int editRecord(int id, String fn, String mn, String ln, String un, String pw, String ad, String co, String ge, String em, String am, String cf) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBFN, fn);
        values.put(DBMN, mn);
        values.put(DBLN, ln);
        values.put(DBUN, un);
        values.put(DBPW, pw);
        values.put(DBAD, ad);
        values.put(DBCO, co);
        values.put(DBGE, ge);
        values.put(DBEM, em);
        values.put(DBAM, am);
        values.put(DBCF, cf);

        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                DBTABLE,
                values,
                selection,
                selectionArgs);

        return count;
    }

    public int approve(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBCF, "true");

        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                DBTABLE,
                values,
                selection,
                selectionArgs);

        return count;
    }

    public int deleteRecord(int id) {
        String selection = "id = ?";
        String[] selectionArgs = { String.valueOf(id) };
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(DBTABLE, selection, selectionArgs);
        return deletedRows;
    }
}
