package com.rcdev.sqlitebrowser.views;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.rcdev.sqlitebrowser.R;
import com.rcdev.sqlitebrowser.utils.DatabaseListAdapter;
import com.rcdev.sqlitebrowser.utils.Functions;
import com.rcdev.sqlitebrowser.utils.SqliteManager;


import java.util.ArrayList;

public class DatabaseList extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private DatabaseListAdapter adapter;
    /* access modifiers changed from: private */
    private SqliteManager.DatabaseInfoType currentType;
    private SqliteManager sqlManager;
    private String mDatabasePath="";

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_list);
        final ListView list = findViewById(R.id.listView);
        BottomNavigationView bottomSheet = findViewById(R.id.bottomSheet);

        bottomSheet.setOnNavigationItemSelectedListener(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        try {
            mDatabasePath = getIntent().getStringExtra("DatabasePath");

            final String DbName = getIntent().getStringExtra("dbName");
            if(DbName!=null)
            {
                Toast.makeText(DatabaseList.this,mDatabasePath + DbName,Toast.LENGTH_LONG);

            }


            this.sqlManager = SqliteManager.getInstance(this);
            this.sqlManager.openDatabase(mDatabasePath, getIntent().getStringExtra("Password"));
            this.adapter = new DatabaseListAdapter(this, new ArrayList());
            list.setAdapter(this.adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    if (currentType == SqliteManager.DatabaseInfoType.TABLE || DatabaseList.this.currentType == SqliteManager.DatabaseInfoType.VIEW) {
                        Intent i = new Intent(DatabaseList.this, TableList.class);
                        i.putExtra("tableName", (String) list.getItemAtPosition(position));
                        i.putExtra("databasePath", mDatabasePath);
                        DatabaseList.this.startActivity(i);
                    }
                }
            });
            ((TextView) findViewById(R.id.lblDatabaseName)).setText(getString(R.string.s_database_name, new Object[]{mDatabasePath}));
            this.currentType = SqliteManager.DatabaseInfoType.TABLE;
            Functions.disableShiftMode(bottomSheet);
            selectedTableNavItem();
        } catch (Exception e) {
            AlertFileNotValid();
        }
    }

    private void AlertFileNotValid() {
        new AlertDialog.Builder(this).setTitle((CharSequence) getResources().getString(R.string.s_notice)).setMessage((CharSequence) getResources().getString(R.string.s_msg_file_is_not_db)).setPositiveButton((CharSequence) getString(R.string.s_ok), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseList.this.finish();
            }
        }).show();
    }

    /* access modifiers changed from: protected */
    protected void onDestroy() {
        sqlManager.close();
        super.onDestroy();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_table_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            case R.id.menu_create_field:
                startActivity(new Intent(this, CreateTable.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectedTableNavItem() throws Exception {
        this.currentType = SqliteManager.DatabaseInfoType.TABLE;
        this.adapter.reloadAdapter(this.sqlManager.getTableList());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_views:
                try {
                    this.adapter.reloadAdapter(this.sqlManager.getViewList());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.menu_index:
                try {
                    this.adapter.reloadAdapter(this.sqlManager.getIndexList());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.menu_query:
                startActivity(new Intent(this, QueryResult.class));
                break;
            case R.id.menu_tables:
                try {
                    this.adapter.reloadAdapter(this.sqlManager.getTableList());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


        }
        return true;
    }
}
