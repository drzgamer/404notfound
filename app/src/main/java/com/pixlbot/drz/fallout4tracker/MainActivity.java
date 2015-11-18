package com.pixlbot.drz.fallout4tracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {

    TableLayout table_layout;
    EditText firstname_et;
    RatingBar score_et;
    Button addmem_btn;

    SQLController sqlcon;

    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqlcon = new SQLController(this);

        firstname_et = (EditText) findViewById(R.id.fistname_et_id);
        score_et = (RatingBar) findViewById(R.id.score_et_id);
        addmem_btn = (Button) findViewById(R.id.addmem_btn_id);
        table_layout = (TableLayout) findViewById(R.id.tableLayout1);

        BuildTable();

        addmem_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                new MyAsync().execute();

            }
        });

    }

    private void BuildTable() {

        sqlcon.open();
        Cursor c = sqlcon.readEntry();

        int rows = c.getCount();
        int cols = c.getColumnCount();

        c.moveToFirst();

        // outer for loop
        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(this);
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            // inner for loop
            for (int j = 0; j < cols; j++) {

                if(j==2){
                    RatingBar score = new RatingBar(this);
                    score.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
                    score.setNumStars(5);
                    score.setIsIndicator(true);
                    score.setRating(c.getFloat(j));
                    row.addView(score);
                }else{
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
                    tv.setBackgroundResource(R.drawable.cell_shape);
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(18);
                    tv.setPadding(0, 5, 0, 5);

                    tv.setText(c.getString(j));

                    row.addView(tv);
                }



            }

            c.moveToNext();

            table_layout.addView(row);

        }
        sqlcon.close();
    }

    private class MyAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            table_layout.removeAllViews();

            PD = new ProgressDialog(MainActivity.this);
            PD.setTitle("Please Wait..");
            PD.setMessage("Loading...");
            PD.setCancelable(false);
            PD.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String firstname = firstname_et.getText().toString();
            float score = score_et.getRating();

            // inserting data
            sqlcon.open();
            sqlcon.insertData(firstname, score);
            // BuildTable();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            BuildTable();
            PD.dismiss();
        }
    }

    OnClickListener removeLocationListener= new OnClickListener() {
        @Override
        public void onClick(View v) {

            TableRow row = new TableRow((Context) v.getParent());
            TableLayout tl = new TableLayout((Context) v.getParent());
            tl.removeView(row);
        }
    };

}