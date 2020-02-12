package com.rcdev.sqlitebrowser.views;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.rcdev.sqlitebrowser.Interfaces.OnItemClickListener;
import com.rcdev.sqlitebrowser.R;
import com.rcdev.sqlitebrowser.model.FileModel;
import com.rcdev.sqlitebrowser.utils.FileListAdapter;
import com.rcdev.sqlitebrowser.utils.FileManager;
import com.rcdev.sqlitebrowser.utils.Functions;

import java.io.File;

public class FileList extends AppCompatActivity {
    private FileListAdapter adapter;
    private TextView lblPath;
    private FileManager manager;
    private String dbName;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.lblPath = findViewById(R.id.lblPath);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        dbName= getIntent().getStringExtra("dbName");

        final ListView fileList = findViewById(R.id.fileList);
        manager = new FileManager();
        adapter = new FileListAdapter(this, manager.getListDirectory(Environment.getExternalStorageDirectory().getAbsolutePath()));
        adapter.setOnItemClickListener(new OnItemClickListener<FileModel>() {
            public void OnItemClick(final FileModel data) {
                if (data.isDirectory() && !data.isBackRequest()) {
                    adapter.reloadAdapter(manager.getListDirectory(data.getFilePath()));
                    lblPath.setText(data.getFilePath());
                    fileList.setSelection(0);
                } else if (data.isBackRequest()) {
                    adapter.reloadAdapter(FileList.this.manager.getListDirectory(data.getPreviousPath()));
                    lblPath.setText(data.getPreviousPath());
                } else {
                    try {


                      if(dbName!=null && !dbName.equalsIgnoreCase(""))
                      {



                      }




                        if (data.isDirectory()) {
                            return;
                        }

                        if (!Functions.fileIsADatabase(data.getFilePath())) {
                            Functions.genericAlert(FileList.this, FileList.this.getResources().getString(R.string.s_msg_file_is_not_db));
                        } else if (Functions.isFileEncrypted(new File(data.getFilePath()))) {
                            FileList.this.AlertTypePassword(data.getFilePath());
                        } else {
                            Intent i = new Intent(FileList.this, DatabaseList.class);
                            i.putExtra("DatabasePath", data.getFilePath());
                            startActivity(i);
                        }
                    } catch (Exception e) {
                        Toast.makeText(FileList.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        fileList.setAdapter(this.adapter);
        lblPath.setText(Environment.getExternalStorageDirectory().getAbsolutePath());
    }


    private void AlertTypePassword(final String dataBasePath) {
        final EditText editPass = new EditText(this);
        editPass.setHint(getString(R.string.s_enter_db_pass));
        editPass.setMaxLines(1);
        editPass.setInputType(128);
        new AlertDialog.Builder(this).setTitle(getString(R.string.s_database_password)).setMessage(getString(R.string.s_enter_db_pass)).setView(editPass).setPositiveButton( getString(R.string.abrir_db), new DialogInterface.OnClickListener() {
           @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editPass.getText().toString().trim().isEmpty()) {
                    Toast.makeText(FileList.this, FileList.this.getString(R.string.s_pass_not_empty), Toast.LENGTH_LONG).show();
                    return;
                }
                Intent i = new Intent(FileList.this, DatabaseList.class);
                i.putExtra("DatabasePath", dataBasePath);
                i.putExtra("Password", editPass.getText().toString().trim());
                startActivity(i);
            }
        }).setNegativeButton(getString(R.string.s_cancel), null).show();
    }

    public void onBackPressed() {
        if (adapter.getDataSource().isEmpty() || !this.adapter.getDataSource().get(0).isBackRequest()) {
            super.onBackPressed();
        } else {
            adapter.reloadAdapter(this.manager.getListDirectory(this.adapter.getDataSource().get(0).getPreviousPath()));
        }
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

