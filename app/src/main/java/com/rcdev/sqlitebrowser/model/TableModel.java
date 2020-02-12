package com.rcdev.sqlitebrowser.model;

import java.util.ArrayList;

public class TableModel {
    private ArrayList<TableColumn> mColumns;
    private String mTableName;

    public TableModel(String tableName, ArrayList<TableColumn> columns) {
        this.mTableName = tableName;
        this.mColumns = columns;
    }

    public String getTableName() {
        return this.mTableName;
    }

    public void setTableName(String value) {
        this.mTableName = value;
    }

    public ArrayList<TableColumn> getColumns() {
        return this.mColumns;
    }

    public void setColumns(ArrayList<TableColumn> value) {
        this.mColumns = value;
    }
}
