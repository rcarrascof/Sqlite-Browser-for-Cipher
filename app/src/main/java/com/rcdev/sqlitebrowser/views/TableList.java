package com.rcdev.sqlitebrowser.views;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rcdev.sqlitebrowser.R;
import com.rcdev.sqlitebrowser.model.GridModel;
import com.rcdev.sqlitebrowser.model.TableColumn;
import com.rcdev.sqlitebrowser.model.TableModel;
import com.rcdev.sqlitebrowser.utils.Functions;
import com.rcdev.sqlitebrowser.utils.GridView;
import com.rcdev.sqlitebrowser.utils.GridViewAdapter;
import com.rcdev.sqlitebrowser.utils.SqliteManager;
import com.rcdev.sqlitebrowser.views.dialogs.DialogNewField;
import com.rcdev.sqlitebrowser.views.dialogs.DialogNewRegistry;

import java.util.ArrayList;
import java.util.Iterator;

public class TableList extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private Button btnNext;
    private Button btnPrev;
    private boolean dataLoaded;
    private DialogNewRegistry dialogInsert;
    private boolean fieldsLoaded;
    private GridView grid;
    private GridView gridFields;
    private SqliteManager sqliteManager;

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_table_list);
            BottomNavigationView bottomSheet = findViewById(R.id.bottomSheet);
            this.sqliteManager = SqliteManager.getInstance(this);
            bottomSheet.setOnNavigationItemSelectedListener(this);
            this.gridFields = findViewById(R.id.gridFields);
            this.grid = findViewById(R.id.grid);
            this.btnNext = findViewById(R.id.btnDown);
            this.btnPrev = findViewById(R.id.btnUp);
            this.grid.setNavButtons(this.btnNext, this.btnPrev, this);
            ((TextView) findViewById(R.id.labelTableName)).setText(getResources().getString(R.string.s_table_with_placeholder, new Object[]{getIntent().getStringExtra("tableName")}));
            setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            this.dialogInsert = new DialogNewRegistry(this, this.sqliteManager);
            this.dialogInsert.loadDataForInsert(this.sqliteManager.getTableInfo(getIntent().getStringExtra("tableName")));
            this.dialogInsert.setOnInsertSuccessListener(new DialogNewRegistry.OnInsertSuccessListener() {
                public void OnInsertSuccess() throws Exception {
                    TableList.this.loadGridData(true);
                }
            });
            showData();
            bottomSheet.setSelectedItemId(R.id.menu_data);
            Functions.disableShiftMode(bottomSheet);
        } catch (Exception e) {
            Functions.genericAlert(this, e.getMessage());
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_table_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create_field /*2131624148*/:
                new DialogNewField(this).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.menu_query /*2131624152*/:
                    startActivity(new Intent(this, QueryResult.class));
                    return false;
                case R.id.menu_fields /*2131624153*/:
                    if (!this.fieldsLoaded) {
                        this.gridFields.setAdapter(new GridViewAdapter(getTableSchemGridModel(), false), this);
                    }
                    this.gridFields.setVisibility(View.VISIBLE);
                    this.grid.setVisibility(View.GONE);
                    if (!this.fieldsLoaded) {
                        this.fieldsLoaded = true;
                    }
                    this.btnNext.setVisibility(View.GONE);
                    this.btnPrev.setVisibility(View.GONE);
                    break;
                case R.id.menu_new /*2131624154*/:
                    this.dialogInsert.show();
                    return false;
                case R.id.menu_data /*2131624155*/:
                    loadGridData(false);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /* access modifiers changed from: private */
    private void loadGridData(boolean reload) throws Exception {
        this.btnNext.setVisibility(View.VISIBLE);
        this.btnPrev.setVisibility(View.VISIBLE);
        showData(reload);
    }

    private GridModel getTableSchemGridModel() throws Exception {
        TableModel table = this.sqliteManager.getTableInfo(getIntent().getStringExtra("tableName"));
        String[] columnsNames = {"name", "type", "pk"};
        ArrayList<String[]> data = new ArrayList<>();
        Iterator it = table.getColumns().iterator();
        while (it.hasNext()) {
            TableColumn column = (TableColumn) it.next();
            data.add(new String[]{column.getColumnName(), column.getDataType(), String.valueOf(column.getPK())});
        }
        return new GridModel(columnsNames, data);
    }

    private void showData() throws Exception {
        showData(false);
    }

    private void showData(boolean reload) throws Exception {
        if (!this.dataLoaded || reload) {
            this.grid.setAdapter(new GridViewAdapter(this.sqliteManager, "select * from " + getIntent().getStringExtra("tableName"), true), this);
        }
        this.gridFields.setVisibility(View.GONE);
        this.grid.setVisibility(View.VISIBLE);
        if (!this.dataLoaded) {
            this.dataLoaded = true;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

