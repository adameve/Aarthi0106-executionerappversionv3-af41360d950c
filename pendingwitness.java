package anulom.executioner.com3.anulom;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import anulom.executioner.com3.anulom.database.DBManager;
import anulom.executioner.com3.anulom.database.DBOperation;
import anulom.executioner.com3.anulom.services.Posttask;
import anulom.executioner.com3.anulom.services.SendmultiPartyReport;
import anulom.executioner.com3.anulom.services.postpendingwitness;
import anulom.executioner.com3.anulom.services.postreassignwitness;
import static anulom.executioner.com3.anulom.database.DBManager.TableInfo.TABLE_REASSIGN_WITNESS;
import static anulom.executioner.com3.anulom.database.DBManager.TableInfo.TABLE_UPDATE_PENDING_WITNESS;


public class pendingwitness extends AppCompatActivity {


        Toolbar toolbar;
        TextView txt1,txt2,txt3;
        CheckBox txt4;
        Spinner sp1,sp2;
        Button update;
        String ID2;
        String TAG="";
        DBOperation db;
        int flag=0;
    private String username2 = "";
    String witnessname,env,mowner="",muser="",ID1,docid,appid,attid,tokenno,witnessemail,partytype;
    List<String> biometricvalues = new ArrayList<>();
    List<String> userlist = new ArrayList<>();
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            db = new DBOperation(this);
            setContentView(R.layout.pendingwitnesslayout);
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            txt1 = findViewById(R.id.textView);
            txt2 = findViewById(R.id.textView10);
            txt3 = findViewById(R.id.textView11);
            txt4 = findViewById(R.id.textView12);
            sp1 = findViewById(R.id.spinner6);
            sp2 = findViewById(R.id.spinner7);
            update = findViewById(R.id.button6);
            env = getIntent().getStringExtra("env");
            witnessname = getIntent().getStringExtra("witnessname");
            docid = getIntent().getStringExtra("docid");
            attid = getIntent().getStringExtra("attid");
            appid = getIntent().getStringExtra("env");
            witnessemail = getIntent().getStringExtra("witnesssemil");
            tokenno = getIntent().getStringExtra("tokenno");
            partytype = getIntent().getStringExtra("partytype");
            SharedPreferences usermail = PreferenceManager.getDefaultSharedPreferences(this);
            username2 = usermail.getString("username", "");
            String password2 = usermail.getString("password", "");
            txt1.setText("Report Key:" + env);
            txt2.setText("Anulom Witness:" + witnessname);
            txt3.setText("Token number:" + tokenno);
            biometricvalues.add("Select");
            biometricvalues.add("Yes");
            biometricvalues.add("Thumb Not Match");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, biometricvalues);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp1.setAdapter(dataAdapter2);
            sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mowner = parent.getItemAtPosition(position).toString();
                    System.out.println("biometricvalues:" + mowner);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            userlist=db.getAllLabels();
            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, userlist);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp2.setAdapter(dataAdapter3);
            sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    muser = parent.getItemAtPosition(position).toString();
                    System.out.println("uservalues:" + muser);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                }
            });

            txt4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(txt4.isChecked()){
                        flag=1;
                        sp2.setVisibility(View.VISIBLE);
                        sp1.setVisibility(View.GONE);
                    }else{

                        sp2.setVisibility(View.GONE);
                        sp1.setVisibility(View.VISIBLE);
                    }
                }
            });
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(flag==1 && txt4.isChecked()){
                        SQLiteDatabase db3 = db.getWritableDatabase();
                        ContentValues values3 = new ContentValues();
                        values3.put(DBManager.TableInfo.KEYID, ID2);
                        values3.put(DBManager.TableInfo.reassign_docid, docid);
                        values3.put(DBManager.TableInfo.reassign_newwitness, muser);
                        values3.put(DBManager.TableInfo.reassign_oldwitness,witnessemail);
                        values3.put(DBManager.TableInfo.KEY_LOGIN_USER,username2);
                        String condition3 = DBManager.TableInfo.reassign_docid + " =?";
                        Cursor cursor3 = db3.query(TABLE_REASSIGN_WITNESS, null, condition3, new String[]{docid}, null, null, null);
                        long status3 = db3.insert(TABLE_REASSIGN_WITNESS, null, values3);
                        Log.d(TAG, "DB insert : " + status3);
                        cursor3.close();
                        db3.close();
                        if (GenericMethods.isConnected(getApplicationContext())) {
                            if(GenericMethods.isOnline()) {
                                Intent i3 = new Intent(pendingwitness.this, postreassignwitness.class);
                                startService(i3);
                                finish();
                            }else{
                                Toast.makeText(pendingwitness.this, " Reassign Witness Saved Offline,App is waiting for active network connection", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(pendingwitness.this, " Reassign Witness Saved Offline!!", Toast.LENGTH_LONG).show();
                            finish();
                        }


                    }else{
                        SQLiteDatabase db3 = db.getWritableDatabase();
                        ContentValues values3 = new ContentValues();
                        values3.put(DBManager.TableInfo.KEYID, ID2);
                        values3.put(DBManager.TableInfo.execemail, username2);
                        values3.put(DBManager.TableInfo.pw_docid, docid);
                        values3.put(DBManager.TableInfo.pw_attid,attid);
                        values3.put(DBManager.TableInfo.pw_email,witnessemail );
                        values3.put(DBManager.TableInfo.pw_partytype, partytype);
                        values3.put(DBManager.TableInfo.pw_biometric,mowner);
                        String condition3 = DBManager.TableInfo.pw_attid + " =?";
                        Cursor cursor3 = db3.query(TABLE_UPDATE_PENDING_WITNESS, null, condition3, new String[]{attid}, null, null, null);
                        long status3 = db3.insert(TABLE_UPDATE_PENDING_WITNESS, null, values3);
                        Log.d(TAG, "DB insert : " + status3);
                        cursor3.close();
                        db3.close();
                        if (GenericMethods.isConnected(getApplicationContext())) {
                            if(GenericMethods.isOnline()) {
                                Intent i3 = new Intent(pendingwitness.this, postpendingwitness.class);
                                startService(i3);
                                finish();
                            }else{
                                Toast.makeText(pendingwitness.this, " Pending Witness Saved Offline,App is waiting for active network connection", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(pendingwitness.this, " Pending Witness Saved Offline!!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
            });

        }
    }

