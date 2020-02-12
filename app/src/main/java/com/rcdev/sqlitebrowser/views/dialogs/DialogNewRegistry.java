package com.rcdev.sqlitebrowser.views.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rcdev.sqlitebrowser.R;
import com.rcdev.sqlitebrowser.model.TableColumn;
import com.rcdev.sqlitebrowser.model.TableModel;
import com.rcdev.sqlitebrowser.utils.Functions;
import com.rcdev.sqlitebrowser.utils.SqliteManager;

import java.util.Iterator;

public class DialogNewRegistry extends Dialog {
    /* access modifiers changed from: private */
    private LinearLayout columnsContainer;
    /* access modifiers changed from: private */
    private SqliteManager dbImpl;
    /* access modifiers changed from: private */
    private OnInsertSuccessListener onInsertSuccessListener;
    /* access modifiers changed from: private */
    private StringBuilder queryInsertFinal;
    /* access modifiers changed from: private */
    private StringBuilder queryInsertRaw;
    /* access modifiers changed from: private */
    private boolean tableHasAutoincrement;

    public interface OnInsertSuccessListener {
        void OnInsertSuccess() throws Exception;
    }

    public DialogNewRegistry(@NonNull Activity context, SqliteManager sql) {
        super(context);
        setOwnerActivity(context);
        getWindow().requestFeature(1);
        setContentView(R.layout.dialog_table_row);
        getWindow().setLayout(-1, -2);
        this.dbImpl = sql;
        initComponents();
    }

    private void initComponents() {
        this.columnsContainer = (LinearLayout) findViewById(R.id.columnsContainer);
        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNewRegistry.this.dismiss();
            }
        });
        findViewById(R.id.btnInsert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value;
                boolean first = true;
                try {
                    DialogNewRegistry.this.queryInsertFinal = new StringBuilder(DialogNewRegistry.this.queryInsertRaw);
                    for (int i = 0; i < DialogNewRegistry.this.columnsContainer.getChildCount(); i++) {
                        View view = DialogNewRegistry.this.columnsContainer.getChildAt(i);
                        TableColumn model = (TableColumn) view.getTag();
                        if ((view instanceof EditText) && (model.getPK() != 1 || !DialogNewRegistry.this.tableHasAutoincrement)) {
                            if (model.allowNull() || !((EditText) view).getText().toString().trim().isEmpty()) {
                                String value2 = ((EditText) view).getText().toString().trim();
                                if (value2.isEmpty()) {
                                    value = "NULL";
                                } else {
                                    value = "'" + value2 + "'";
                                }
                                if (first) {
                                    first = false;
                                    DialogNewRegistry.this.queryInsertFinal.append(value);
                                } else {
                                    DialogNewRegistry.this.queryInsertFinal.append(", ").append(value);
                                }
                            } else {
                                throw new Exception(model.getColumnName() + " not must be empty");
                            }
                        }
                    }
                    DialogNewRegistry.this.queryInsertFinal.append(")");
                    DialogNewRegistry.this.dbImpl.execSQL(DialogNewRegistry.this.queryInsertFinal.toString());
                    DialogNewRegistry.this.dismiss();
                    if (DialogNewRegistry.this.onInsertSuccessListener != null) {
                        DialogNewRegistry.this.onInsertSuccessListener.OnInsertSuccess();
                    }
                    DialogNewRegistry.this.cleanAllEdits();
                } catch (Exception e) {
                    Functions.genericAlert(DialogNewRegistry.this.getOwnerActivity(), e.getMessage());
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void cleanAllEdits() {
        for (int i = 0; i < this.columnsContainer.getChildCount(); i++) {
            View v = this.columnsContainer.getChildAt(i);
            if (v instanceof EditText) {
                ((EditText) v).getText().clear();
            }
        }
    }

    public void loadDataForInsert(TableModel tableModel) {
        this.columnsContainer.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(Functions.dpToPx(0), Functions.dpToPx(2), Functions.dpToPx(0), Functions.dpToPx(7));
        this.queryInsertRaw = new StringBuilder("INSERT INTO " + tableModel.getTableName() + " (");
        boolean first = true;
        Iterator it = tableModel.getColumns().iterator();
        while (it.hasNext()) {
            TableColumn column = (TableColumn) it.next();
            TextView lblName = new TextView(getContext());
            lblName.setText(column.getColumnName());
            EditText edit = new EditText(getContext());
            edit.setMaxLines(1);
            edit.setLayoutParams(params);
            if (first) {
                first = false;
                this.queryInsertRaw.append(column.getColumnName());
            } else {
                this.queryInsertRaw.append(", ").append(column.getColumnName());
            }
            edit.setTag(column);
            this.columnsContainer.addView(lblName);
            this.columnsContainer.addView(edit);
        }
        this.queryInsertRaw.append(") VALUES (");
        this.columnsContainer.requestLayout();
        this.columnsContainer.invalidate();
    }

    public void setOnInsertSuccessListener(OnInsertSuccessListener listener) {
        this.onInsertSuccessListener = listener;
    }
}
