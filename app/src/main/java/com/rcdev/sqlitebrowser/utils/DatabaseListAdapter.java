package com.rcdev.sqlitebrowser.utils;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rcdev.sqlitebrowser.Interfaces.OnItemClickListener;
import com.rcdev.sqlitebrowser.R;

import java.util.ArrayList;

public class DatabaseListAdapter extends BaseAdapter {
    /* access modifiers changed from: private */
    public ArrayList<String> dataSource;
    private LayoutInflater inflater;
    /* access modifiers changed from: private */
    public OnItemClickListener<String> onItemClickListener;

    public DatabaseListAdapter(Context context, ArrayList<String> source) {
        this.dataSource = source;
        this.inflater = LayoutInflater.from(context);
    }

    public void reloadAdapter(ArrayList<String> data) {
        this.dataSource.clear();
        this.dataSource.addAll(data);
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.dataSource.size();
    }

    public Object getItem(int i) {
        return this.dataSource.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        View root = view;
        if (root == null) {
            root = this.inflater.inflate(R.layout.row_database_tables, null);
        }
        ((TextView) root.findViewById(R.id.lblName)).setText((CharSequence) this.dataSource.get(i));
        if (this.onItemClickListener != null) {
            root.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    DatabaseListAdapter.this.onItemClickListener.OnItemClick(DatabaseListAdapter.this.dataSource.get(i));
                }
            });
        }
        return root;
    }

    public void setOnItemClickListener(OnItemClickListener<String> listener) {
        this.onItemClickListener = listener;
    }
}
