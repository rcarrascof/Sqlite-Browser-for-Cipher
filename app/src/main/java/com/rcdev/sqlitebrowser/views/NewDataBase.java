package com.rcdev.sqlitebrowser.views;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rcdev.sqlitebrowser.R;

public class NewDataBase extends AppCompatActivity {

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView (R.layout.activity_new_database);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        findViewById(R.id.btn_create_database).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                EditText dbName = findViewById(R.id.database_name_input);
                Intent i = new Intent(NewDataBase.this, FileList.class);
                String DbnameS = dbName.getText().toString();
                i.putExtra("dbName",DbnameS);
                startActivity(i);



            }
        });

    }


}
