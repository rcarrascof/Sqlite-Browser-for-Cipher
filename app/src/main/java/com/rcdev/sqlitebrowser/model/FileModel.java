package com.rcdev.sqlitebrowser.model;

public class FileModel {
    private String ext;
    private boolean isBackRequest;
    private boolean isDirectory;
    private String mFileName;
    private String mFilePath;
    private String mParentPath;

    public String getFileName() {
        return this.mFileName;
    }

    public void setName(String value) {
        this.mFileName = value;
    }

    public String getFilePath() {
        return this.mFilePath;
    }

    public void setFilePath(String value) {
        this.mFilePath = value;
    }

    public String getPreviousPath() {
        return this.mParentPath;
    }

    public void setPreviousPath(String value) {
        this.mParentPath = value;
    }

    public boolean isDirectory() {
        return this.isDirectory;
    }

    public void setIsDirectory(boolean value) {
        this.isDirectory = value;
    }

    public String getExt() {
        return this.ext;
    }

    public void setExt(String value) {
        this.ext = value;
    }

    public boolean isBackRequest() {
        return this.isBackRequest;
    }

    public void setBackRequest(boolean value) {
        this.isBackRequest = value;
    }
}
