package com.rcdev.sqlitebrowser.model;

public class TableColumn {
    private boolean allowNull;
    private String defaultValue;
    private String mColumnName;
    private String mDataType;
    private int mPK;

    public String getColumnName() {
        return this.mColumnName;
    }

    public void setColumnName(String value) {
        this.mColumnName = value;
    }

    public String getDataType() {
        return this.mDataType;
    }

    public void setDataType(String value) {
        this.mDataType = value;
    }

    public int getPK() {
        return this.mPK;
    }

    public void setPK(int value) {
        this.mPK = value;
    }

    public boolean allowNull() {
        return this.allowNull;
    }

    public void setAllowNull(boolean value) {
        this.allowNull = value;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String value) {
        this.defaultValue = value;
    }
}
