package anulom.executioner.com.anulom;


import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import anulom.executioner.com.anulom.database.DBManager;
import anulom.executioner.com.anulom.database.DBOperation;
import anulom.executioner.com.anulom.services.postreassignappointment;


import static anulom.executioner.com.anulom.database.DBManager.TableInfo.TABLE_REASSIGN_APPOINTMENTS;


public class reassignappointment extends AppCompatActivity {
    Toolbar toolbar;
     TextView txt1,txt2,txt3;
     Spinner sp1;
    String ID2;
    String TAG="";
     Button update;
     EditText e1;
     DBOperation db;
     String documentid,appointmentid,oldwitnessemail,newwitnessemail,REASSIGNREASON="";
    private SharedPreferences usermail;
    private String username2 = "", password2 = "";
    List<String> userlist = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reassignappointment);
        db = new DBOperation(this);
        txt1=findViewById(R.id.textView11);
        txt2=findViewById(R.id.textView);
        txt3=findViewById(R.id.textView10);
        sp1=findViewById(R.id.spinner6);
        update=findViewById(R.id.button6);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Reassign Appointment");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        e1=findViewById(R.id.editText);
        usermail = PreferenceManager.getDefaultSharedPreferences(this);
        username2 = usermail.getString("username", "");
        password2 = usermail.getString("password", "");
        documentid=getIntent().getStringExtra("document_id");
        appointmentid=getIntent().getStringExtra("appointment_id");


        userlist=db.getAllLabels();
        txt1.setText("Document Id:"+documentid);
        txt2.setText("Appointment Id:"+appointmentid);
        txt3.setText("Executioner:"+username2);
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this, R.layout.witnesslay, userlist);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(dataAdapter2);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newwitnessemail = parent.getItemAtPosition(position).toString();
                System.out.println("uservalues:" + newwitnessemail);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


     update.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             REASSIGNREASON=e1.getText().toString();
             SQLiteDatabase db3 = db.getWritableDatabase();
             ContentValues values3 = new ContentValues();
             values3.put(DBManager.TableInfo.KEYID, ID2);
             values3.put(DBManager.TableInfo.comment1, username2);
             values3.put(DBManager.TableInfo.rea_docid, documentid);
             values3.put(DBManager.TableInfo.rea_appid,appointmentid);
             values3.put(DBManager.TableInfo.rea_reason,REASSIGNREASON );
             values3.put(DBManager.TableInfo.rea_new_executioner, newwitnessemail);
             values3.put(DBManager.TableInfo.rea_current_executioner, username2);
             String condition3 = DBManager.TableInfo.rea_docid + " =?";
             Cursor cursor3 = db3.query(TABLE_REASSIGN_APPOINTMENTS, null, condition3, new String[]{documentid}, null, null, null);
             long status3 = db3.insert(TABLE_REASSIGN_APPOINTMENTS, null, values3);
             Log.d(TAG, "DB insert : " + status3+REASSIGNREASON);
             cursor3.close();
             db3.close();
             if (GenericMethods.isConnected(getApplicationContext())) {
                 if(GenericMethods.isOnline()) {
                     Intent i3 = new Intent(reassignappointment.this, postreassignappointment.class);
                     startService(i3);
                     finish();
                 }else{
                     Toast.makeText(reassignappointment.this, " Reassigned Appointments Saved Offline,App is waiting for active network connection", Toast.LENGTH_LONG).show();
                     finish();
                 }
             } else {
                 Toast.makeText(reassignappointment.this, " Reassigned Appointments Saved Offline!!", Toast.LENGTH_LONG).show();
                 finish();
             }
         }
     });





    }
}

