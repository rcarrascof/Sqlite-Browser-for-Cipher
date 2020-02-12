package com.rcdev.sqlitebrowser.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rcdev.sqlitebrowser.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Solicitando permiso de almacenamiento
        requestPermission();





        findViewById(R.id.btnSalir).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });


        findViewById(R.id.btnOpenDb).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                   startActivity(new Intent(MainActivity.this, FileList.class));
                } else {
                    alertNoPermission();
                }
            }
        });





        findViewById(R.id.btnCreateDb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                    startActivity(new Intent(MainActivity.this, NewDataBase.class));
                } else {
                    alertNoPermission();
                }

            }
        });

    }


    private void requestPermission() {
        if (!permissionsHasBeenGranted()) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 221);
        }
    }

    private boolean permissionsHasBeenGranted() {
        return ActivityCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0 && ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }


    private void alertNoPermission() {
        new AlertDialog.Builder(this).setTitle(getString(R.string.s_permission)).setMessage(getString(R.string.s_no_permission_msg)).setPositiveButton("Grant permissions", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.requestPermission();
            }
        }).setNegativeButton("Cancel", null).show();
    }
}
