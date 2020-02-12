package com.rcdev.sqlitebrowser.utils;


import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import com.rcdev.sqlitebrowser.R;

public class GridView extends LinearLayout {
    private int adapterColumnCount;
    /* access modifiers changed from: private */
    private ScrollView body;
    private int[] columnsWidth;
    /* access modifiers changed from: private */
    private GridViewAdapter currentAdapter;
    /* access modifiers changed from: private */
    private int currentShowing;
    private TableRow header;
    /* access modifiers changed from: private */
    private int maxLines;
    /* access modifiers changed from: private */
    private LayoutParams params;
    /* access modifiers changed from: private */
    private int startPosition;

    public GridView(Context context) {
        this(context, null);
    }

    public GridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.startPosition = -1;
        this.maxLines = 20;
        initComponents();
    }

    private void initComponents() {
        addView(inflate(getContext(), R.layout.gridview_base, null));
        this.header = findViewById(R.id.header);
        this.body = findViewById(R.id.layoutBody);
        this.params = new LayoutParams(-2, -2);
    }

    public void setAdapter(final GridViewAdapter adapter, final Activity act) {
        this.currentAdapter = adapter;
        this.adapterColumnCount = adapter.getColumnCount();
        this.body.removeAllViews();
        this.header.removeAllViews();
        this.columnsWidth = new int[adapter.getColumnCount()];
        for (int i = 0; i < adapter.getColumnCount(); i++) {
            TextView label = new TextView(getContext());
            label.setBackgroundResource(R.color.colorPrimaryDark);
            label.setTextColor(-1);
            label.setTextSize(13.0f);
            label.setText(adapter.getColumnsNames()[i]);
            label.setPadding(5, 2, 5, 2);
            label.measure(0, 0);
            columnsWidth[i] = label.getMeasuredWidth();
            header.addView(label);
        }
        new AsyncLoader(getContext()) {
            public void doInBackground() throws Exception {
                int step = 0;
                final LinearLayout columns = new LinearLayout(GridView.this.getContext());
                columns.setOrientation(LinearLayout.HORIZONTAL);
                columns.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                for (int i = 0; i < adapter.getColumnCount(); i++) {
                    LinearLayout layout = new LinearLayout(GridView.this.getContext());
                    layout.setLayoutParams(GridView.this.params);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    columns.addView(layout);
                }
                int colorrow = android.R.color.background_light;
                int colorNull = android.R.color.holo_red_dark;
                if (adapter.limiter && adapter.getDataCount() >= maxLines) {
                    adapter.moveToPosition(startPosition);
                }
                while (adapter.moveToNext()) {
                    for (int i = 0; i < adapter.getColumnCount(); i++) {
                        TextView label = new TextView(getContext());
                        label.setLayoutParams(params);
                        label.setPadding(5, 2, 5, 2);
                        label.setMaxLines(1);
                        label.setTextSize(13.0f);
                        if (adapter.getDataAt(i) == null) {
                            label.setBackgroundResource(colorNull);
                        } else {
                            label.setBackgroundResource(colorrow);
                        }
                        label.setText(adapter.getDataAt(i));
                        ((LinearLayout) columns.getChildAt(i)).addView(label);
                    }
                    if (colorNull == android.R.color.holo_red_dark) {
                        colorNull = android.R.color.holo_red_light;
                    } else {
                        colorNull = android.R.color.holo_red_dark;
                    }
                    if (colorrow == android.R.color.background_light) {
                        colorrow = android.R.color.darker_gray;
                    } else {
                        colorrow = android.R.color.background_light;
                    }
                    step++;
                    if (step == GridView.this.maxLines && adapter.limiter) {
                        break;
                    }
                }
                GridView.this.currentShowing = step;
                act.runOnUiThread(new Runnable() {
                    public void run() {
                        GridView.this.body.addView(columns);
                        GridView.this.refreshLayouts();
                    }
                });
            }
        }.execute();
    }


    private void refreshLayouts() {
        int i = 0;
        while (i < this.adapterColumnCount) {
            try {
                View v = ((LinearLayout) this.body.getChildAt(0)).getChildAt(i);
                v.measure(0, 0);
                int measureWidth = v.getMeasuredWidth();
                if (measureWidth > this.columnsWidth[i]) {
                    ((TextView) this.header.getChildAt(i)).setWidth(measureWidth);
                    this.header.getChildAt(i).requestLayout();
                    this.header.getChildAt(i).invalidate();
                } else {
                    v.setLayoutParams(new LinearLayout.LayoutParams(this.columnsWidth[i], -2));
                    v.requestLayout();
                    v.invalidate();
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        this.header.requestLayout();
        this.body.requestLayout();
        this.header.invalidate();
        this.body.invalidate();
    }

    public void setNavButtons(View btnNext, View btnPrevious, final Activity act) {
        btnNext.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (GridView.this.currentAdapter != null && !GridView.this.currentAdapter.isEmpty() && GridView.this.currentAdapter.limiter && GridView.this.currentAdapter.getDataCount() >= GridView.this.maxLines && GridView.this.startPosition != GridView.this.currentAdapter.getDataCount() && GridView.this.currentAdapter.getDataCount() - (GridView.this.startPosition + GridView.this.currentShowing) > 1) {
                    if (GridView.this.currentAdapter.getDataCount() >= GridView.this.startPosition + GridView.this.maxLines) {
                        GridView.this.startPosition = GridView.this.startPosition + GridView.this.maxLines;
                    } else {
                        GridView.this.startPosition = GridView.this.currentAdapter.getDataCount() - GridView.this.startPosition;
                    }
                    GridView.this.setAdapter(GridView.this.currentAdapter, act);
                }
            }
        });
        btnPrevious.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (GridView.this.currentAdapter != null && !GridView.this.currentAdapter.isEmpty() && GridView.this.currentAdapter.limiter && GridView.this.currentAdapter.getDataCount() >= GridView.this.maxLines && GridView.this.startPosition >= 0) {
                    if (GridView.this.currentShowing < GridView.this.maxLines) {
                        GridView.this.startPosition = GridView.this.startPosition - GridView.this.maxLines;
                    } else {
                        GridView.this.startPosition = GridView.this.startPosition - GridView.this.currentShowing;
                    }
                    GridView.this.setAdapter(GridView.this.currentAdapter, act);
                }
            }
        });
    }
}

