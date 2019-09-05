package anulom.executioner.com3.anulom;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import anulom.executioner.com3.anulom.database.DBManager;
import anulom.executioner.com3.anulom.database.DBOperation;
import anulom.executioner.com3.anulom.services.SendPaymentService;

import static anulom.executioner.com3.anulom.R.id.tvpay3;
import static anulom.executioner.com3.anulom.database.DBManager.TableInfo.PAYMENT;
import static anulom.executioner.com3.anulom.database.DBManager.TableInfo.UPDATEPAYMENT1;

public class Pay extends AppCompatActivity {

    DBOperation db;
    String option = "";
    String token = "DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7";

    String rkey, amount, document, ID1, pay,payamount11, commentvalue,filename, val, date,appid,content,startdate,poststatus;

    java.util.Date calander;
    SimpleDateFormat simpledateformat;
    String Date;
    int value1, payment, amt;

    TextView tv, tv1, tv2;
    EditText value, et1;
    Button update1;
    Context context;
    RadioButton rb1;
    View layout;
    private String username2 = "";

    public void radio(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        int checked1 = view.getId();
        if (checked1 == R.id.rp) {

        }


    }

    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.payinfo);
        db = new DBOperation(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Payment Info");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SharedPreferences usermail = PreferenceManager.getDefaultSharedPreferences(this);
        username2 = usermail.getString("username", "");
        String password2 = usermail.getString("password", "");
        filename=getIntent().getStringExtra("filename");
        layout = findViewById(R.id.l1);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        rkey = getIntent().getStringExtra("ReportKey");
        document = getIntent().getStringExtra("DocumentId");
        appid=getIntent().getStringExtra("appointmentid");
        content=getIntent().getStringExtra("content");
        startdate=getIntent().getStringExtra("StartDate");
        poststatus=getIntent().getStringExtra("post_status");
        payamount11=getIntent().getStringExtra("amount");
        tv1 = findViewById(R.id.textView1);

        SQLiteDatabase base1 = db.getReadableDatabase();
        String query = "select * from  " + PAYMENT + " where " + DBManager.TableInfo.KEY_LOGIN_USER + " = '" + GenericMethods.email + "'";
        Cursor cursor = base1.rawQuery(query, null);
        while (cursor.moveToNext()) {
            if (rkey.equals(cursor.getString(cursor.getColumnIndex(DBManager.TableInfo.rep1)))) {
                amount = cursor.getString(cursor.getColumnIndex(DBManager.TableInfo.payamount));

                System.out.println("amount:"+amount+"="+"payamount:"+payamount11);
            }

        }
        cursor.close();
        base1.close();

        if (amount == "null" || amount == null || Integer.valueOf(amount) == 0) {
            layout.setVisibility(View.INVISIBLE);
            tv2 = findViewById(R.id.nopay1);
            tv2.setText(" NO OUTSTANDING PAYMENTS!!! / " +
                    " उर्वरित रक्कम नाही!!!");
            tv2.setTextSize(18);
            tv2.setTypeface(null, Typeface.BOLD);
            tv2.setGravity(Gravity.CENTER_HORIZONTAL);
            tv2.setTextColor(Color.parseColor("#006B3C"));

        }else if(Integer.valueOf(amount)==50 ||Integer.valueOf(amount)<50){

            tv2 = findViewById(R.id.nopay1);
            tv2.setVisibility(View.GONE);
            tv = findViewById(R.id.tvreqno);
            tv.setText("Take ₹" + amount + "/- from client at the time of Execution");
            tv.setTextIsSelectable(true);


        }

        else {

            tv2 = findViewById(R.id.nopay1);
            tv2.setVisibility(View.GONE);
            tv = findViewById(R.id.tvreqno);
            tv.setText("Take ₹" + amount + "/- from client at the time of Execution");
            tv.setTextIsSelectable(true);
        }


        ArrayList<HashMap<String, String>> paymentlist = db.getPaymentReport(db);
        update1 = findViewById(R.id.btnupdate2);
        update1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                option = "rm";
                calander = Calendar.getInstance().getTime();
                simpledateformat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                Date = simpledateformat.format(calander.getTime());

                value = findViewById(tvpay3);
                et1 = findViewById(R.id.editText1);
                rb1 = findViewById(R.id.rp);

                try {
                    value1 = Integer.valueOf(value.getText().toString());


                } catch (Exception e) {

                }
                try {
                    amt = Integer.valueOf(amount);
                } catch (Exception e) {

                }
                payment = amt - value1;

                pay = String.valueOf(payment);
                try {
                    commentvalue = et1.getText().toString();
                } catch (Exception e) {

                }


                SQLiteDatabase sqldb = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(DBManager.TableInfo.payamount, pay);
                if (((value1 < amt) && (payment != amt)) || ((value1 == amt) && (payment != amt))) {
                    sqldb.update(PAYMENT, values, DBManager.TableInfo.rep1 + "=?", new String[]{rkey});
                    sqldb.close();

                } else {
                    Toast.makeText(Pay.this, " ENTER AMOUNT!!", Toast.LENGTH_LONG).show();
                }
                val = String.valueOf(value1);

                SQLiteDatabase sqldb1 = db.getWritableDatabase();
                ContentValues values1 = new ContentValues();
                values1.put(DBManager.TableInfo.KEYID, ID1);
                values1.put(DBManager.TableInfo.DOCID, document);
                values1.put(DBManager.TableInfo.email1, username2);
                values1.put(DBManager.TableInfo.amt, val);
                values1.put(DBManager.TableInfo.date, Date);
                values1.put(DBManager.TableInfo.radiotype, option);
                values1.put(DBManager.TableInfo.comment1, commentvalue);
                if (((value1 < amt) && (payment != amt)) || ((value1 == amt) && (payment != amt))) {
                    sqldb1.insert(UPDATEPAYMENT1, null, values1);
                    sqldb1.close();

                } else {
                    Toast.makeText(Pay.this, " ENTER AMOUNT!!", Toast.LENGTH_LONG).show();
                }
                if (GenericMethods.isConnected(getApplicationContext())) {
                    if(GenericMethods.isOnline()) {
                        if(!commentvalue.equals("")) {
                            Intent intent = new Intent(Pay.this, SendPaymentService.class);
                            startService(intent);
                            finish();

                        }else{
                            Toast.makeText(Pay.this, "FIll The Required Fields!!!", Toast.LENGTH_LONG).show();

                        }
                    }else{
                        Toast.makeText(Pay.this, " Payment Saved Offline,App is waiting for active network Connection", Toast.LENGTH_LONG).show();

                    }
                } else {

                    Toast.makeText(Pay.this, " Payment Saved Offline!!", Toast.LENGTH_LONG).show();
                    finish();
                }

if(!filename.equals("fragment")) {
    Pay.this.runOnUiThread(new Runnable() {
        @Override
        public void run() {
//                        Payment.this.singleTask.schedule(new TimerTask () {
//                            @Override
//                            public void run() {
            AlertDialog alertbox = new AlertDialog.Builder(Pay.this).setMessage("Payment inserted Successfully!!")
                    .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            arg0.dismiss();

                        }

                    }).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                            Intent i = new Intent(Pay.this, newstatusinfo.class);
                            i.putExtra("document_id", document);
                            i.putExtra("rkey", rkey);
                            i.putExtra("appointment_id", appid);
                            i.putExtra("content", content);
                            i.putExtra("StartDate", startdate);
                            i.putExtra("post_status", poststatus);
                            i.putExtra("amount", payamount11);
                            i.putExtra("paymentflag", "cashmode");
                            System.out.println("amount from pay:" + payamount11);
                            startActivity(i);

                        }
                    }).show();

//                            }
//                        }, 2000);
        }
    });
}
            }
        });


    }

    @Override
    public void onBackPressed() {


        finish();
    }
    public void onClick(android.view.View view) {}}