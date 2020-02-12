package com.rcdev.sqlitebrowser.utils;


import android.database.Cursor;

import com.rcdev.sqlitebrowser.model.GridModel;

import java.util.ArrayList;

public class GridViewAdapter {
    private String[] columnsNames;
    private int currentPosInListData = -1;
    private Cursor data;
    boolean limiter = false;
    private ArrayList<String[]> listData;

    public GridViewAdapter(SqliteManager sql, String query, boolean limiter2) throws Exception {
        this.data = sql.rawQuery(query);
        this.columnsNames = this.data.getColumnNames();
        this.limiter = limiter2;
    }

    public GridViewAdapter(GridModel model, boolean limiter2) {
        this.listData = model.getData();
        this.columnsNames = model.getColumnsNames();
        this.data = null;
        this.limiter = limiter2;
    }

    /* access modifiers changed from: 0000 */
    public String getDataAt(int index) {
        if (this.data != null) {
            return this.data.getString(index);
        }
        if (this.listData != null) {
            return ((String[]) this.listData.get(this.currentPosInListData))[index];
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public boolean moveToNext() {
        if (this.data != null) {
            return this.data.moveToNext();
        }
        if (this.listData == null) {
            return false;
        }
        this.currentPosInListData++;
        if (this.currentPosInListData < this.listData.size()) {
            return true;
        }
        return false;
    }

    public boolean isLast() {
        if (this.data != null) {
            return this.data.isLast();
        }
        return this.listData.size() > 0 && this.listData.size() + -1 == this.currentPosInListData;
    }

    public void moveToPosition(int pos) {
        if (this.data != null) {
            this.data.moveToPosition(pos);
        } else {
            this.currentPosInListData = pos + 1;
        }
    }

    /* access modifiers changed from: 0000 */
    public String[] getColumnsNames() {
        return this.columnsNames;
    }

    /* access modifiers changed from: 0000 */
    public int getColumnCount() {
        if (this.data != null) {
            return this.data.getColumnCount();
        }
        if (this.listData.size() > 0) {
            return ((String[]) this.listData.get(0)).length;
        }
        return 0;
    }

    public int getDataCount() {
        if (this.data != null) {
            return this.data.getCount();
        }
        return this.listData.size();
    }

    /* access modifiers changed from: 0000 */
    public void close() {
        if (this.data != null) {
            this.data.close();
        } else {
            this.listData.clear();
        }
    }

    public boolean isEmpty() {
        return this.data == null && (this.listData == null || this.listData.isEmpty());
    }
}

