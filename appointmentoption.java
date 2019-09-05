package anulom.executioner.com3.anulom;

import android.content.Intent;
import android.os.Bundle;


import android.view.View;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class appointmentoption extends AppCompatActivity {

    Toolbar toolbar;
    TextView free, custom;
    RelativeLayout layout1, layout2;
    String option, documentId, apptype, content, editsize, reportkey, appointment_id;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointmentoption);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Select Your Appointment Type");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        documentId = getIntent().getStringExtra("DocumentId");
        apptype = getIntent().getStringExtra("apptype");
        content = getIntent().getStringExtra("content");
        reportkey = getIntent().getStringExtra("ReportKey");
        appointment_id = getIntent().getStringExtra("AppointmentId");
        free = findViewById(R.id.textView7);
        layout1 = findViewById(R.id.lay1);
        layout2 = findViewById(R.id.lay2);
        custom = findViewById(R.id.textView8);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                option = "1";
                Intent i = new Intent(appointmentoption.this, Appointmentbooking.class);
                i.putExtra("DocumentId", documentId);
                i.putExtra("apptype", apptype);
                i.putExtra("content", content);
                i.putExtra("ReportKey", reportkey);
                i.putExtra("option", option);
                i.putExtra("AppointmentId", appointment_id);
                startActivity(i);
            }
        });


        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option = "0";
                Intent i = new Intent(appointmentoption.this, Appointmentbooking.class);
                i.putExtra("DocumentId", documentId);
                i.putExtra("apptype", apptype);
                i.putExtra("ReportKey", reportkey);
                i.putExtra("content", content);
                i.putExtra("free_reason", editsize);
                i.putExtra("option", option);
                i.putExtra("AppointmentId", appointment_id);
                startActivity(i);

            }
        });


    }
}

