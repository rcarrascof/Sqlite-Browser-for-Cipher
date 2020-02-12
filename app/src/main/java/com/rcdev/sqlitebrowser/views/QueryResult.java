package com.rcdev.sqlitebrowser.views;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rcdev.sqlitebrowser.R;
import com.rcdev.sqlitebrowser.utils.Functions;
import com.rcdev.sqlitebrowser.utils.GridView;
import com.rcdev.sqlitebrowser.utils.GridViewAdapter;
import com.rcdev.sqlitebrowser.utils.SqliteManager;

public class QueryResult extends AppCompatActivity {
    private FloatingActionButton btnExecute;
    /* access modifiers changed from: private */
    private EditText mEditQuery;
    /* access modifiers changed from: private */
    private GridView mGridResult;
    /* access modifiers changed from: private */
    private SqliteManager sqliteManager;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.sqliteManager = SqliteManager.getInstance(this);
        if (this.sqliteManager == null) {
            finish();
            return;
        }
        this.mGridResult = findViewById(R.id.gridView);
        this.mGridResult.setNavButtons(findViewById(R.id.btnNext), findViewById(R.id.btnPrev), this);
        this.mEditQuery = findViewById(R.id.editQuery);
        this.btnExecute = findViewById(R.id.btnExecute);
        this.btnExecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QueryResult.this.mEditQuery.getText().toString().trim().isEmpty()) {
                    Functions.genericAlert(QueryResult.this, QueryResult.this.getString(R.string.s_query_not_empty));
                    return;
                }
                try {
                    GridViewAdapter adapterData = new GridViewAdapter(QueryResult.this.sqliteManager, QueryResult.this.mEditQuery.getText().toString().trim(), true);
                    if (adapterData.getDataCount() == 0) {
                        adapterData = new GridViewAdapter(QueryResult.this.sqliteManager, "select 'Not an error' as ''", true);
                    }
                    QueryResult.this.mGridResult.setAdapter(adapterData, QueryResult.this);
                } catch (Exception e) {
                    Functions.genericAlert(QueryResult.this, e.getMessage());
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
