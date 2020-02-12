package com.rcdev.sqlitebrowser.utils;

import android.content.Context;
import android.database.sqlite.SQLiteException;


import com.rcdev.sqlitebrowser.model.TableColumn;
import com.rcdev.sqlitebrowser.model.TableModel;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.Serializable;
import java.util.ArrayList;

public class SqliteManager implements Serializable {
    private static SqliteManager instance;
    private Context context;
    private boolean isDatabaseOpen;
    private boolean isSqlCipher;
    private SqlCipher sqlCipherInstance;
    private Sqlite sqliteInstance;

    public enum DatabaseInfoType {
        VIEW,
        INDEX,
        TABLE
    }

    private class SqlCipher extends SQLiteOpenHelper {
        private String password;

        public SqlCipher(Context context, String name, String password2) {
            super(context, name, null, 1);
            this.password = password2;
        }

        public void onCreate(SQLiteDatabase sqLiteDatabase) {
        }

        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        }

        public synchronized SQLiteDatabase getReadableDatabase() {
            return super.getReadableDatabase(this.password);
        }

        public synchronized SQLiteDatabase getWritableDatabase() {
            return super.getWritableDatabase(this.password);
        }
    }

    private class Sqlite extends android.database.sqlite.SQLiteOpenHelper {
        public Sqlite(Context context, String name) {
            super(context, name, null, 1);
        }

        public void onCreate(android.database.sqlite.SQLiteDatabase sqLiteDatabase) {
        }

        public void onUpgrade(android.database.sqlite.SQLiteDatabase sqLiteDatabase, int i, int i1) {
        }
    }

    private SqliteManager(Context context2) {
        this.context = context2;
    }

    public static SqliteManager getInstance(Context context2) {
        if (instance == null) {
            instance = new SqliteManager(context2);
        }
        return instance;
    }

    public void openDatabase(String dbPath, String password) {
        close();
        if (password != null) {
            SQLiteDatabase.loadLibs(this.context);
            this.sqlCipherInstance = new SqlCipher(this.context, dbPath, password);
            this.sqlCipherInstance.getReadableDatabase();
            this.isDatabaseOpen = true;
            this.isSqlCipher = true;
            return;
        }
        this.sqliteInstance = new Sqlite(this.context, dbPath);
        this.sqliteInstance.getReadableDatabase();
        this.isDatabaseOpen = true;
        this.isSqlCipher = false;
    }

    public void close() {
        if (this.sqliteInstance != null && this.isDatabaseOpen) {
            this.sqliteInstance.close();
            this.sqliteInstance = null;
        }
        if (this.sqlCipherInstance != null) {
            this.sqlCipherInstance.close();
            this.sqlCipherInstance = null;
        }
    }

    /* access modifiers changed from: 0000 */
    public Cursor rawQuery(String query) throws Exception {
        if (isDatabaseOpen) {
            return isSqlCipher ? sqlCipherInstance.getReadableDatabase().rawQuery(query, null) : sqliteInstance.getReadableDatabase().rawQuery(query, null);
        }
        throw new Exception("la base de datos no esta abierta");
    }

    public void execSQL(String query) {
        if (!this.isDatabaseOpen) {
            throw new SQLiteException("The database is not open!");
        } else if (!this.isSqlCipher || this.sqlCipherInstance == null) {
            this.sqliteInstance.getReadableDatabase().execSQL(query);
        } else {
            this.sqlCipherInstance.getReadableDatabase().execSQL(query);
        }
    }

    public ArrayList<String> getTableList() throws Exception {
        return getDatabaseInfo("table");
    }

    public ArrayList<String> getIndexList() throws Exception {
        return getDatabaseInfo("index");
    }

    public ArrayList<String> getViewList() throws Exception {
        return getDatabaseInfo("view");
    }

    private ArrayList<String> getDatabaseInfo(String type) throws Exception {
        ArrayList<String> list = new ArrayList<>();
        Cursor c = rawQuery("select name from sqlite_master where type = '" + type + "'");
        while (c.moveToNext()) {
            list.add(c.getString(0));
        }
        c.close();
        return list;
    }

    public TableModel getTableInfo(String tableName) throws Exception {
        Cursor c = rawQuery("pragma table_info(" + tableName + ")");
        ArrayList<TableColumn> columns = new ArrayList<>();
        while (c.moveToNext()) {
            TableColumn column = new TableColumn();
            column.setColumnName(c.getString(c.getColumnIndex("name")));
            column.setDataType(c.getString(c.getColumnIndex("type")));
            column.setPK(c.getInt(c.getColumnIndex("pk")));
            columns.add(column);
            column.setAllowNull(c.getInt(c.getColumnIndex("notnull")) == 0);
        }
        c.close();
        return new TableModel(tableName, columns);
    }

    public boolean tableHasAutoincrementPK(String tableName) {
        try {
            Cursor c = rawQuery("select 1 from sqlite_sequence where name = '" + tableName + "'");
            boolean result = c.moveToFirst();
            c.close();
            return result;
        } catch (Exception e) {
            return false;
        }
    }
}