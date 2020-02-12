package com.rcdev.sqlitebrowser.model;


import java.util.ArrayList;

public class GridModel {
        private String[] ColumnsNames;
        private ArrayList<String[]> data;

        public GridModel(String[] columnsNames, ArrayList<String[]> data2) {
            this.ColumnsNames = columnsNames;
            this.data = data2;
        }

        public String[] getColumnsNames() {
            return this.ColumnsNames;
        }

        public ArrayList<String[]> getData() {
            return this.data;
        }
    }
