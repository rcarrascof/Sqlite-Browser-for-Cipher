package com.rcdev.sqlitebrowser.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.rcdev.sqlitebrowser.R;

public abstract class AsyncLoader extends AsyncTask<Void, Void, Exception> {
    private Context context;
    private ProgressDialog progressDialog;

    public abstract void doInBackground() throws Exception;

     AsyncLoader(Context context2) {
        this.context = context2;
    }

    public AsyncLoader() {
        this(null);
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        if (this.context != null) {
            this.progressDialog = ProgressDialog.show(this.context, "", this.context.getString(R.string.s_wait_msg), true, false);
        }
    }

    /* access modifiers changed from: protected */
    public Exception doInBackground(Void... params) {
        try {
            doInBackground();
            return null;
        } catch (Exception e) {
            return e;
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Exception e) {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
        if (e != null && this.context != null) {
            Functions.genericAlert(this.context, e.getMessage());
        }
    }
}
