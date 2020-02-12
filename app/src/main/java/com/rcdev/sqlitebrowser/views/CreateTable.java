package com.rcdev.sqlitebrowser.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rcdev.sqlitebrowser.R;
import com.rcdev.sqlitebrowser.model.TableColumn;
import com.rcdev.sqlitebrowser.utils.Functions;
import com.rcdev.sqlitebrowser.views.dialogs.DialogNewField;

import java.util.ArrayList;

public class CreateTable extends AppCompatActivity {
    /* access modifiers changed from: private */
    public DialogNewField dialogField;
    /* access modifiers changed from: private */
    public FieldsListAdapter fieldsListAdapter;
    /* access modifiers changed from: private */
    public ArrayList<String> fieldsNames;
    /* access modifiers changed from: private */
    public ArrayList<TableColumn> listDataSource;

    private class FieldsListAdapter extends RecyclerView.Adapter<C0399VH> {
        private FieldsListAdapter() {
        }

        public C0399VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new C0399VH(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new_table_fields, parent, false));
        }

        public void onBindViewHolder(C0399VH holder, int position) {
            holder.populateView((TableColumn) CreateTable.this.listDataSource.get(position));
        }

        public int getItemCount() {
            return CreateTable.this.listDataSource.size();
        }
    }

    /* renamed from: sqlitemanager.devm.org.sqlite_sqlciphermanager.views.CreateTable$VH */
    private class C0399VH extends RecyclerView.ViewHolder {
        private TextView allowNull;
        private ImageButton btnDelete;
        private ImageButton btnEdit;
        private TextView columnName;
        private TextView dataType;
        private TextView defaultValue;
        private TextView primaryKey;

        C0399VH(View itemView) {
            super(itemView);
            this.columnName = itemView.findViewById(R.id.columnName);
            this.dataType = itemView.findViewById(R.id.dataType);
            this.defaultValue = itemView.findViewById(R.id.default_value);
            this.allowNull = itemView.findViewById(R.id.allowNull);
            this.primaryKey = itemView.findViewById(R.id.labelPk);
            this.btnEdit = itemView.findViewById(R.id.btnEdit);
            this.btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        /* access modifiers changed from: 0000 */
        public void populateView(final TableColumn model) {
            this.columnName.setText(model.getColumnName());
            this.dataType.setText(model.getDataType());
            this.defaultValue.setText(model.getDefaultValue());
            this.allowNull.setText(model.allowNull() ? "Yes" : "No");
            if (model.getPK() > 0) {
                this.primaryKey.setText("Primary key");
                this.primaryKey.setVisibility(View.VISIBLE);
            } else {
                this.primaryKey.setVisibility(View.GONE);
            }
            this.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        CreateTable.this.dialogField.loadField(model);
                        CreateTable.this.dialogField.show();
                    } catch (Exception e) {
                        Functions.genericAlert(CreateTable.this, e.getMessage());
                    }
                }
            });
            this.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(CreateTable.this).setTitle("Aviso!").setMessage("Esta seguro que desea eliminar este campo?").setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            CreateTable.this.listDataSource.remove(model);
                            CreateTable.this.fieldsListAdapter.notifyDataSetChanged();
                        }
                    }).setNegativeButton("Cancelar", null).show();
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_table);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.listDataSource = new ArrayList<>();
        RecyclerView fieldsList = findViewById(R.id.fieldsList);
        this.fieldsListAdapter = new FieldsListAdapter();
        this.fieldsNames = new ArrayList<>();
        this.dialogField = new DialogNewField(this);
        this.dialogField.setOnSubmitFieldListener(new DialogNewField.OnSubmitFieldListener() {
            public void OnSubmitField(TableColumn field) throws Exception {
                if (CreateTable.this.fieldsNames.contains(field.getColumnName().trim())) {
                    throw new Exception("Una columna con este nombre ya fue agregada!");
                }
                CreateTable.this.listDataSource.add(field);
                CreateTable.this.fieldsNames.add(field.getColumnName().trim());
                CreateTable.this.fieldsListAdapter.notifyDataSetChanged();
            }

            public void OnFieldEdited(TableColumn oldField) {
                CreateTable.this.listDataSource.remove(oldField);
                CreateTable.this.fieldsNames.remove(oldField.getColumnName().trim());
                CreateTable.this.fieldsListAdapter.notifyDataSetChanged();
            }
        });
        if (fieldsList != null) {
            fieldsList.setHasFixedSize(true);
            fieldsList.setLayoutManager(new LinearLayoutManager(this));
        }
        fieldsList.setAdapter(this.fieldsListAdapter);
        findViewById(R.id.btnAddField).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTable.this.dialogField.reset();
                CreateTable.this.dialogField.show();
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