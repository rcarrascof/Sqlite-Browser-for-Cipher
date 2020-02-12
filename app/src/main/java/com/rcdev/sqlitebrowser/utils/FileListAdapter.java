package com.rcdev.sqlitebrowser.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.rcdev.sqlitebrowser.Interfaces.OnItemClickListener;
import com.rcdev.sqlitebrowser.R;
import com.rcdev.sqlitebrowser.model.FileModel;

import java.util.ArrayList;

public class FileListAdapter extends BaseAdapter {

    public ArrayList<FileModel> dataSource;
    private Drawable iconDbFile;
    private Drawable iconFile;
    private Drawable iconFolder;
    private LayoutInflater inflater;

    public OnItemClickListener<FileModel> onItemClickListener;

    private class ViewHolder {

        private TextView lblName;

        private ViewHolder(TextView label) {
            lblName = label;
        }
        private void populate(String name) {
            lblName.setText(name);
        }
    }

    public FileListAdapter(Context context, ArrayList<FileModel> data) {
        dataSource = data;
        inflater = LayoutInflater.from(context);
        iconFolder = ContextCompat.getDrawable(context, R.drawable.ic_folder_open_black_24dp);
        iconFile = ContextCompat.getDrawable(context, R.drawable.ic_insert_drive_file_black_24dp);
        iconDbFile = ContextCompat.getDrawable(context, R.drawable.ic_database);
    }

    public ArrayList<FileModel> getDataSource() {
        return this.dataSource;
    }

    public int getCount() {
        return dataSource.size();
    }

    public Object getItem(int i) {
        return dataSource.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public void reloadAdapter(ArrayList<FileModel> newData) {
        dataSource.clear();
        dataSource.addAll(newData);
        notifyDataSetChanged();
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        View root = view;
        if (view == null) {
            root = inflater.inflate(R.layout.row_file, null);
            vh = new ViewHolder((TextView) root.findViewById(R.id.lblName));
            root.setTag(vh);
        } else {
            vh = (ViewHolder) root.getTag();
        }
        if (onItemClickListener != null) {
            root.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) { onItemClickListener.OnItemClick(dataSource.get(i));
                }
            });
        }
        if (dataSource.get(i).isBackRequest()) {
            vh.lblName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        } else if (dataSource.get(i).isDirectory()) {
            vh.lblName.setCompoundDrawablesWithIntrinsicBounds(iconFolder, null, null, null);
        } else if (Functions.fileIsADatabase(dataSource.get(i).getFilePath())) {
            vh.lblName.setCompoundDrawablesWithIntrinsicBounds(iconDbFile, null, null, null);
        } else {
            vh.lblName.setCompoundDrawablesWithIntrinsicBounds(iconFile, null, null, null);
        }
        vh.populate(dataSource.get(i).getFileName());
        return root;
    }

    public void setOnItemClickListener(OnItemClickListener<FileModel> listener) {
        this.onItemClickListener = listener;
    }
}
