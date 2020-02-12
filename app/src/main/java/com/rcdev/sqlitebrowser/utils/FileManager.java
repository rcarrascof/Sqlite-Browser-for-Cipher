package com.rcdev.sqlitebrowser.utils;

import com.rcdev.sqlitebrowser.model.FileModel;

import java.io.File;
import java.util.ArrayList;

public class FileManager {
    public ArrayList<FileModel>




      getListDirectory(String path) {
        ArrayList<FileModel> list = new ArrayList<>();
        File main = new File(path);
        if (!(main.getParentFile() == null || main.getParentFile().listFiles() == null)) {
            FileModel file = new FileModel();
            file.setBackRequest(true);
            file.setName("   ...");
            file.setIsDirectory(false);
            file.setPreviousPath(main.getParentFile().getAbsolutePath());
            list.add(file);
        }
        for (File file2 : main.listFiles()) {
            FileModel model = new FileModel();
            model.setName(file2.getName());
            model.setFilePath(file2.getAbsolutePath());
            model.setIsDirectory(file2.isDirectory());
            list.add(model);
        }
        return list;
    }
}
