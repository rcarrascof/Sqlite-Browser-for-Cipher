package com.rcdev.sqlitebrowser.utils;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rcdev.sqlitebrowser.R;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import static android.content.DialogInterface.*;

public class Functions {
    public static boolean fileIsADatabase(String path) {
        String ext = path.substring(path.lastIndexOf(".") + 1, path.length());
        return ext.trim().equalsIgnoreCase("db") || ext.trim().equalsIgnoreCase("db3") || ext.trim().equalsIgnoreCase("sqlite");
    }

    public static void genericAlert(Context context, String msg) {
        if (msg == null) {
            msg = context.getResources().getString(R.string.s_error_procesing);
        }
        new AlertDialog.Builder(context).setTitle((CharSequence) context.getResources().getString(R.string.s_notice)).setMessage(msg).setPositiveButton(R.string.s_ok, null).show();
    }

    public static boolean isFileEncrypted(File file) throws IOException {
        byte[] b = new byte[16];
        new DataInputStream(new FileInputStream(file)).readFully(b);
        return !new String(b).equalsIgnoreCase("SQLite format 3\u0000");
    }

    public static int dpToPx(int dp) {
        return (int) (((float) dp) * Resources.getSystem().getDisplayMetrics().density);
    }

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShifting(false); //setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e2) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e2);
        }
    }
}
