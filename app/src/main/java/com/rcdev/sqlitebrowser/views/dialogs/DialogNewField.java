package com.rcdev.sqlitebrowser.views.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;

import com.rcdev.sqlitebrowser.R;
import com.rcdev.sqlitebrowser.model.TableColumn;
import com.rcdev.sqlitebrowser.utils.Functions;

public class DialogNewField extends Dialog implements View.OnClickListener {
    /* access modifiers changed from: private */
    public AppCompatSpinner comboDataType;
    private EditText editDefaultValue;
    private EditText editFieldName;
    private TableColumn editedModel;
    private OnSubmitFieldListener onSubmitFieldListener;
    private SwitchCompat switchAllowNull;
    private SwitchCompat switchPk;

    public interface OnSubmitFieldListener {
        void OnFieldEdited(TableColumn tableColumn);

        void OnSubmitField(TableColumn tableColumn) throws Exception;
    }

    public DialogNewField(@NonNull Activity context) {
        super(context);
        setOwnerActivity(context);
        getWindow().requestFeature(1);
        setContentView(R.layout.dialog_new_table);
        getWindow().setLayout(-1, -2);
        initComponent();
    }

    private void initComponent() {
        this.editFieldName = findViewById(R.id.editFieldName);
        this.editDefaultValue = findViewById(R.id.editDefaultValue);
        this.comboDataType = findViewById(R.id.comboDataType);
        this.comboDataType.setAdapter(new ArrayAdapter(getContext(), R.layout.simple_spinner_item_dropdown, getContext().getResources().getStringArray(R.array.data_types)));
        ((ArrayAdapter) this.comboDataType.getAdapter()).setDropDownViewResource(R.layout.simple_spinner_item_dropdown);
        this.comboDataType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 4) {
                    final EditText type = new EditText(DialogNewField.this.getOwnerActivity());
                    type.setHint("Data type");
                    type.setInputType(524288);
                    type.setMaxLines(1);
                    new AlertDialog.Builder(DialogNewField.this.getOwnerActivity()).setTitle((CharSequence) "Data type").setMessage((CharSequence) "Enter the data type").setView((View) type).setPositiveButton((CharSequence) "Aceptar", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                       @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((TextView) DialogNewField.this.comboDataType.getSelectedView()).setText(type.getText().toString().trim());
                        }
                    }).setNegativeButton((CharSequence) "Cancelar", (DialogInterface.OnClickListener) null).show();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.switchPk = findViewById(R.id.PK);
        this.switchAllowNull = findViewById(R.id.allowNull);
        findViewById(R.id.btnAdd).setOnClickListener(this);
        findViewById(R.id.btnCancel).setOnClickListener(this);
    }

    public void loadField(TableColumn model) throws Exception {
        this.editedModel = model;
        this.editFieldName.setText(model.getColumnName());
        this.editDefaultValue.setText(model.getDefaultValue());
        this.switchPk.setChecked(model.getPK() > 0);
        this.switchAllowNull.setChecked(model.allowNull());
        for (int i = 0; i < this.comboDataType.getCount(); i++) {
            if (model.getDataType().trim().equalsIgnoreCase(this.comboDataType.getItemAtPosition(i).toString().trim())) {
                this.comboDataType.setSelection(i);
                return;
            }
        }
        this.comboDataType.setSelection(4);
        ((TextView) this.comboDataType.getSelectedView()).setText(model.getDataType());
    }

    public void reset() {
        this.editFieldName.getText().clear();
        this.editDefaultValue.getText().clear();
        this.comboDataType.setAdapter((SpinnerAdapter) new ArrayAdapter(getContext(), R.layout.simple_spinner_item_dropdown, getContext().getResources().getStringArray(R.array.data_types)));
        this.switchPk.setChecked(false);
        this.switchAllowNull.setChecked(true);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd /*2131624098*/:
                if (this.editFieldName.getText().toString().trim().isEmpty()) {
                    Functions.genericAlert(getOwnerActivity(), "El nombre del campo no puede estar vacio");
                    this.editFieldName.setError("");
                    this.editFieldName.requestFocus();
                    return;
                }
                if (this.onSubmitFieldListener != null) {
                    if (this.editedModel != null) {
                        this.onSubmitFieldListener.OnFieldEdited(this.editedModel);
                        this.editedModel = null;
                    }
                    try {
                        this.onSubmitFieldListener.OnSubmitField(bindDataModel());
                    } catch (Exception e) {
                        Functions.genericAlert(getOwnerActivity(), e.getMessage());
                        return;
                    }
                }
                dismiss();
                return;
            case R.id.btnCancel /*2131624099*/:
                this.editedModel = null;
                dismiss();
                return;
            default:
                return;
        }
    }

    private TableColumn bindDataModel() {
        TableColumn model = new TableColumn();
        model.setColumnName(this.editFieldName.getText().toString().trim());
        model.setAllowNull(this.switchAllowNull.isChecked());
        model.setDefaultValue(this.editDefaultValue.getText().toString().trim());
        model.setPK(this.switchPk.isChecked() ? 1 : 0);
        model.setDataType(((TextView) this.comboDataType.getSelectedView()).getText().toString().trim());
        return model;
    }

    public void setOnSubmitFieldListener(OnSubmitFieldListener listener) {
        this.onSubmitFieldListener = listener;
    }
}
