package anulom.executioner.com3.anulom;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;


import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import anulom.executioner.com3.anulom.database.DBManager;
import anulom.executioner.com3.anulom.database.DBOperation;

import anulom.executioner.com3.anulom.services.SendCommentService;
import anulom.executioner.com3.anulom.services.getappointmentslot;
import anulom.executioner.com3.anulom.services.postappointmentbooking;

import static anulom.executioner.com3.anulom.database.DBManager.TableInfo.APPOINTMENTBOOKING;




public class Appointmentbooking extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String LOG_TAG = "Anulom-Executioner";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";


    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyClWGASK76mkrDB3Z2aYQB195kxv7KS84A";
    Toolbar toolbar;
    int v, a, a1;
    String valuenext = "0";

    TextView reason, txt, txtdate;
    EditText landmark1, slottime, free_reason1, line1, line2, name1, email2, contact2, attendesadress2;
    int startdelay = 3000;
    Date date = null, date2 = null;
    int j, checkk = 0;
    String TAG = "", current_Date = "", party_address;
    int mposition;
    ArrayList<HashMap<String, String>> getAlldataList = null;
    private String username2 = "";
    String appointment_id, startdate, add, land, ID1, contactmail, contactaddress, contactaddress1, contactphone, value, value1, contactmail1, contactphone1;
    DBOperation db;

    final Timer singleTask = new Timer();
    String document, apptype, con1, option1, val, mregion, division_id, option, free_reason, slotid, reportkey, mperson, mperson1;

    String mspin = "";
    String datevalue = "";
    final String datevalue1 = "";
    String starttime;
    String endtime;
    String finaltime;
    String timevalue = "";
    String slot = "";
    String mowner = "";
    String mtenant = "";
    String name;

    final List<String> categories1 = new ArrayList<>();
    final List<String> city = new ArrayList<>();
    final List<String> regionlist = new ArrayList<>();
    final List<String> region = new ArrayList<>();
    final List<String> region1 = new ArrayList<>();
    final List<String> region2 = new ArrayList<>();
    final List<String> region3 = new ArrayList<>();
    final List<String> region4 = new ArrayList<>();
    final List<String> region5 = new ArrayList<>();
    final List<String> region6 = new ArrayList<>();
    final List<String> region7 = new ArrayList<>();
    final List<String> division1 = new ArrayList<>();
    final List<String> division2 = new ArrayList<>();
    final List<String> division3 = new ArrayList<>();
    final List<String> division4 = new ArrayList<>();
    final List<String> division5 = new ArrayList<>();
    final List<String> division6 = new ArrayList<>();
    final List<String> division7 = new ArrayList<>();
    final List<String> division9 = new ArrayList<>();
    final List<String> division11 = new ArrayList<>();
    final List<String> division12 = new ArrayList<>();
    final List<String> division13 = new ArrayList<>();
    final List<String> division14 = new ArrayList<>();
    final List<String> division15 = new ArrayList<>();

    final List<String> contactperson1 = new ArrayList<>();

    final List<String> doclist = new ArrayList<>();
    final List<String> contactmaillist = new ArrayList<>();
    final List<String> namelist = new ArrayList<>();
    final List<String> emaillist = new ArrayList<>();
    final List<String> contactphonelist = new ArrayList<>();
    final List<String> contactaddresslist = new ArrayList<>();
    final List<String> contactlist = new ArrayList<>();
    final List<String> addresslist = new ArrayList<>();
    DatePickerDialog StartTime;

    protected void onCreate(Bundle savedInstanceState) {
        db = new DBOperation(getApplicationContext());
        super.onCreate(savedInstanceState);
        final LinearLayout rootView = new LinearLayout(this);

        document = getIntent().getStringExtra("DocumentId");
        apptype = getIntent().getStringExtra("apptype");
        con1 = getIntent().getStringExtra("content");
        option = getIntent().getStringExtra("option");
        free_reason = getIntent().getStringExtra("free_reason");
        reportkey = getIntent().getStringExtra("ReportKey");
        appointment_id = getIntent().getStringExtra("AppointmentId");
        current_Date = GenericMethods.getCurrentDate();

        val = getIntent().getStringExtra("value");
        startdate = getIntent().getStringExtra("StartDate");

        SharedPreferences usermail = PreferenceManager.getDefaultSharedPreferences(this);
        username2 = usermail.getString("username", "");
        String password2 = usermail.getString("password", "");


        if (con1.equals("Today")) {

            getAlldataList = db.getTodayPartiesReport(db, startdate);

        } else if (con1.equals("Older")) {

            getAlldataList = db.getOlderPartiesReport(db, startdate);

        } else if (con1.equals("New")) {

            getAlldataList = db.getNewPartiesReport(db, startdate);

        } else {
            getAlldataList = db.getAllPartiesReport(db, startdate);

        }
        TextView citytext = new TextView(this);


        citytext.setText("  Select the City:");
        citytext.setTextColor(Color.parseColor("#000080"));
        citytext.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        final TextView regiontext = new TextView(this);
        regiontext.setText("  Select the Region:");
        regiontext.setTextColor(Color.parseColor("#000080"));
        regiontext.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));

        TextView regiontext1 = new TextView(this);
        regiontext1.setText("  Select Date:");
        regiontext1.setTextColor(Color.parseColor("#000080"));
        regiontext1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));


        final TextView name = new TextView(Appointmentbooking.this);
        name.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        name.setTextColor(Color.parseColor("#000080"));
        name.setText("  Name:");
        name.setVisibility(View.GONE);

        final TextView email = new TextView(Appointmentbooking.this);
        email.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        email.setTextColor(Color.parseColor("#000080"));
        email.setText("  Email:");
        email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        email.setVisibility(View.GONE);

        final TextView datetest = new TextView(Appointmentbooking.this);
        datetest.setTextColor(Color.parseColor("#000080"));
        datetest.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        datetest.setVisibility(View.INVISIBLE);

        final TextView contact = new TextView(Appointmentbooking.this);
        contact.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        contact.setTextColor(Color.parseColor("#000080"));
        contact.setText("  Contact No:");
        contact.setInputType(InputType.TYPE_CLASS_PHONE);
        contact.setVisibility(View.GONE);

        final TextView attendeesaddress = new TextView(Appointmentbooking.this);
        attendeesaddress.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        attendeesaddress.setTextColor(Color.parseColor("#000080"));
        attendeesaddress.setText("  Address:");
        attendeesaddress.setInputType(InputType.TYPE_CLASS_PHONE);
        attendeesaddress.setVisibility(View.GONE);

        txt = new TextView(Appointmentbooking.this);
        txt.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        txt.setTextColor(Color.parseColor("#000080"));
        txt.setText("  NO AVAILABLE SLOTS!!!");
        txt.setTypeface(null, Typeface.BOLD);
        txt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        txt.setVisibility(View.GONE);


        txtdate = new TextView(Appointmentbooking.this);
        txtdate.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        txtdate.setTextColor(Color.parseColor("#000080"));
        txtdate.setText("  PLEASE SELECT CORRECT DATE!!!");
        txtdate.setTypeface(null, Typeface.BOLD);
        txtdate.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        txtdate.setVisibility(View.GONE);

        name1 = new EditText(this);
        name1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams paramsname = (LinearLayout.LayoutParams) name1.getLayoutParams();
        paramsname.setMargins(40, 20, 50, 10);
        name1.setLayoutParams(paramsname);
        name1.setVisibility(View.GONE);
        name1.setBackgroundResource(R.drawable.edittextborder);

        email2 = new EditText(this);
        email2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams paramsemail = (LinearLayout.LayoutParams) email2.getLayoutParams();
        paramsemail.setMargins(40, 20, 50, 10);
        email2.setLayoutParams(paramsemail);
        email2.setVisibility(View.GONE);
        email2.setBackgroundResource(R.drawable.edittextborder);

        contact2 = new EditText(this);
        contact2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams paramscontact = (LinearLayout.LayoutParams) contact2.getLayoutParams();
        paramscontact.setMargins(40, 20, 50, 10);
        contact2.setLayoutParams(paramscontact);
        contact2.setVisibility(View.GONE);
        contact2.setBackgroundResource(R.drawable.edittextborder);

        attendesadress2 = new EditText(this);
        attendesadress2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams paramscontact1 = (LinearLayout.LayoutParams) contact2.getLayoutParams();
        paramscontact1.setMargins(40, 20, 50, 10);
        attendesadress2.setLayoutParams(paramscontact1);
        attendesadress2.setVisibility(View.GONE);
        attendesadress2.setBackgroundResource(R.drawable.edittextborder);

        final TextView regiontext2 = new TextView(this);
        regiontext2.setText("  Select Time Slot:");
        regiontext2.setTextColor(Color.parseColor("#000080"));
        regiontext2.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        regiontext2.setVisibility(View.GONE);

        TextView attendees = new TextView(this);
        attendees.setText("  Select Attendees:");
        attendees.setTextColor(Color.parseColor("#000080"));
        attendees.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        TextView tv = new TextView(this);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        tv.setTextColor(Color.parseColor("#545454"));
        tv.setText("  Owner:");
        TextView tv1 = new TextView(this);
        tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        tv1.setTextColor(Color.parseColor("#545454"));
        tv1.setText("  Tenant:");

        final CheckBox add1 = new CheckBox(this);
        add1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        add1.setTextColor(Color.parseColor("#000080"));
        add1.setText("Add Attendees");
        add1.setId(a1);


        final Button addattendees = new Button(this);
        addattendees.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        addattendees.setGravity(Gravity.CENTER);
        addattendees.setText("ADD ATTENDEES");

        addattendees.setBackgroundColor(Color.parseColor("#3b5998"));
        addattendees.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams paramsaddattendess = (LinearLayout.LayoutParams) addattendees.getLayoutParams();
        paramsaddattendess.setMargins(40, 20, 50, 10);
        addattendees.setLayoutParams(paramsaddattendess);
        addattendees.setVisibility(View.GONE);

        reason = new TextView(this);
        reason.setText("  Type the reason for free appointment:");
        reason.setTextColor(Color.parseColor("#545454"));
        reason.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        //reason.setVisibility(View.GONE);
        TextView tv3 = new TextView(this);
        tv3.setText("  Contact Person:");
        tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        tv3.setTextColor(Color.parseColor("#545454"));

        final TextView address = new TextView(this);
        address.setText("  Address:");
        address.setTextColor(Color.parseColor("#545454"));
        address.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));
        address.setVisibility(View.GONE);

        final TextView landmark = new TextView(this);
        landmark.setText("  Landmark:");
        landmark.setTextColor(Color.parseColor("#545454"));
        landmark.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));

        TextView buildingtype = new TextView(this);
        buildingtype.setText("  Select The Building Type:");
        buildingtype.setTextColor(Color.parseColor("#000080"));
        buildingtype.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));

        TextView addresstype = new TextView(this);
        addresstype.setText("  Select The Address:");
        addresstype.setTextColor(Color.parseColor("#000080"));
        addresstype.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.font_size_medium));

        add1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (add1.isChecked()) {

                    name.setVisibility(View.VISIBLE);
                    name1.setVisibility(View.VISIBLE);
                    email.setVisibility(View.VISIBLE);
                    email2.setVisibility(View.VISIBLE);
                    contact.setVisibility(View.VISIBLE);
                    contact2.setVisibility(View.VISIBLE);
                    addattendees.setVisibility(View.VISIBLE);
                    attendeesaddress.setVisibility(View.VISIBLE);
                    attendesadress2.setVisibility(View.VISIBLE);


                } else if (!add1.isChecked()) {


                    name1.setText("");
                    email2.setText("");
                    contact2.setText("");
                    attendesadress2.setText("");

                    name.setVisibility(View.GONE);
                    name1.setVisibility(View.GONE);
                    email.setVisibility(View.GONE);
                    email2.setVisibility(View.GONE);
                    contact.setVisibility(View.GONE);
                    contact2.setVisibility(View.GONE);
                    addattendees.setVisibility(View.GONE);
                    attendeesaddress.setVisibility(View.GONE);
                    attendesadress2.setVisibility(View.GONE);

                }
            }
        });

        Button update1 = new Button(this);
        update1.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        update1.setBackgroundResource(R.drawable.calendar);
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) update1.getLayoutParams();
        params2.setMargins(40, 10, 250, 0);
        update1.setLayoutParams(params2);


        Button confirm = new Button(this);
        confirm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        confirm.setGravity(Gravity.CENTER);
        confirm.setText("CONFIRM APPOINTMENT");
        confirm.setBackgroundColor(Color.parseColor("#3b5998"));
        confirm.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) confirm.getLayoutParams();
        params.setMargins(40, 20, 50, 10);
        confirm.setLayoutParams(params);

        line1 = new EditText(this);
        line1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams paramsline = (LinearLayout.LayoutParams) line1.getLayoutParams();
        paramsline.setMargins(40, 20, 50, 10);
        line1.setHint("Building Name/Door No");
        line1.setLayoutParams(paramsline);
        line1.setVisibility(View.GONE);

        line2 = new EditText(this);
        line2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams paramsline1 = (LinearLayout.LayoutParams) line1.getLayoutParams();
        paramsline1.setMargins(40, 20, 50, 10);
        line2.setHint("Building Name/Door No");
        line2.setLayoutParams(paramsline1);
        line2.setVisibility(View.GONE);

        slottime = new EditText(this);
        slottime.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) slottime.getLayoutParams();
        params1.setMargins(40, 20, 320, 10);
        slottime.setLayoutParams(params1);

        free_reason1 = new EditText(this);
        free_reason1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams para = (LinearLayout.LayoutParams) free_reason1.getLayoutParams();
        para.setMargins(40, 20, 50, 10);
        free_reason1.setLayoutParams(para);
        free_reason1.setVisibility(View.GONE);


        final AutoCompleteTextView address1 = new AutoCompleteTextView(this);
        address1.setId(v);
        address1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) address1.getLayoutParams();
        params3.setMargins(40, 20, 50, 10);
        address1.setHint("City/State/Pincode");
        address1.setLayoutParams(params3);
        address1.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.autocomplete_item));
        address1.setOnItemClickListener(this);
        address1.setVisibility(View.GONE);

        landmark1 = new EditText(this);
        landmark1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams) landmark1.getLayoutParams();
        params4.setMargins(40, 20, 50, 10);
        landmark1.setHint("Please Enter Your Landmark");
        landmark1.setLayoutParams(params4);


        final CheckBox check1 = new CheckBox(this);
        check1.setTextSize(18);
        check1.setTextColor(Color.parseColor("#545454"));
        check1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams params5 = (LinearLayout.LayoutParams) check1.getLayoutParams();
        params5.setMargins(40, 20, 50, 10);
        check1.setLayoutParams(params5);

        final CheckBox check2 = new CheckBox(this);
        check2.setTextSize(18);
        check2.setTextColor(Color.parseColor("#545454"));
        check2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams params6 = (LinearLayout.LayoutParams) check2.getLayoutParams();
        params6.setMargins(40, 20, 50, 10);
        check2.setLayoutParams(params6);

//        if (option.equals("0")) {
//
//
//            free_reason1.setVisibility(View.GONE);
//            reason.setVisibility(View.GONE);
//
//        }

        RadioGroup rg = new RadioGroup(getApplicationContext());
        rg.setOrientation(RadioGroup.HORIZONTAL);
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{getResources().getColor(R.color.colorPrimaryDark)}
        );

        final RadioButton apartment = new RadioButton(getApplicationContext());
        apartment.setText("Apartment");
        apartment.setTextColor(Color.BLACK);
        apartment.setButtonDrawable(R.drawable.radiobutton);
        apartment.setTextSize(18);
        rg.addView(apartment);

        // Create another Radio Button for RadioGroup
        final RadioButton Individual_house = new RadioButton(getApplicationContext());
        Individual_house.setText("Individual House");
        Individual_house.setButtonDrawable(R.drawable.radiobutton);
        Individual_house.setTextColor(Color.BLACK);
        Individual_house.setTextSize(18);
        rg.addView(Individual_house);

        RadioGroup rg1 = new RadioGroup(getApplicationContext());
        rg1.setOrientation(RadioGroup.VERTICAL);

        final RadioButton contactpersonaddress = new RadioButton(getApplicationContext());
        contactpersonaddress.setText("Same as Contact Person Address");
        contactpersonaddress.setButtonDrawable(R.drawable.radiobutton);
        contactpersonaddress.setTextColor(Color.BLACK);
        contactpersonaddress.setTextSize(18);
        rg1.addView(contactpersonaddress);

        final RadioButton differentaddress = new RadioButton(getApplicationContext());
        differentaddress.setText("Different Address");
        differentaddress.setButtonDrawable(R.drawable.radiobutton);
        differentaddress.setTextColor(Color.BLACK);
        differentaddress.setTextSize(18);
        rg1.addView(differentaddress);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (apartment.isChecked()) {
                    option1 = "Apartment";
                } else if (Individual_house.isChecked()) {
                    option1 = "Individual House";
                }

            }
        });


        Spinner sp1 = new Spinner(this);
        final Spinner sp2 = new Spinner(this);
        final Spinner sp3 = new Spinner(this);
        final Spinner sp4 = new Spinner(this);
        final Spinner sp5 = new Spinner(this);
        final Spinner sp6 = new Spinner(this);
        sp3.setGravity(Gravity.CENTER);
        sp3.setBackgroundResource(R.drawable.spinnbackground);
        sp3.setVisibility(View.GONE);
        categories1.add("-SELECT-");
        sp5.setGravity(Gravity.CENTER);
        sp5.setBackgroundResource(R.drawable.spinnbackground);

        sp6.setGravity(Gravity.CENTER);
        sp6.setBackgroundResource(R.drawable.spinnbackground);
        sp6.setVisibility(View.GONE);


        sp1.setGravity(Gravity.CENTER);
        sp1.setBackgroundResource(R.drawable.spinnbackground);
        doclist.add(document);
        city.add("Pune");
        city.add("Mumbai");
        city.add("Mumbai Suburban");
        city.add("Thane");
        city.add("Raigad");
        city.add("Navi Mumbai");
        city.add("Nashik");
        city.add("Nagpur");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(Appointmentbooking.this, R.layout.witnesslay, city);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(dataAdapter);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mspin = parent.getItemAtPosition(position).toString();


                if (mspin.equals("Pune")) {

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(Appointmentbooking.this, R.layout.witnesslay, region);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp2.setAdapter(dataAdapter2);
                } else if (mspin.equals("Mumbai")) {

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(Appointmentbooking.this, R.layout.witnesslay, region1);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp2.setAdapter(dataAdapter2);


                } else if (mspin.equals("Mumbai Suburban")) {

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(Appointmentbooking.this, R.layout.witnesslay, region2);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp2.setAdapter(dataAdapter2);

                } else if (mspin.equals("Thane")) {

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(Appointmentbooking.this, R.layout.witnesslay, region3);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp2.setAdapter(dataAdapter2);


                } else if (mspin.equals("Raigad")) {

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(Appointmentbooking.this, R.layout.witnesslay, region4);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp2.setAdapter(dataAdapter2);

                } else if (mspin.equals("Navi Mumbai")) {

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(Appointmentbooking.this, R.layout.witnesslay, region5);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp2.setAdapter(dataAdapter2);
                } else if (mspin.equals("Nashik")) {

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(Appointmentbooking.this, R.layout.witnesslay, region6);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp2.setAdapter(dataAdapter2);
                } else if (mspin.equals("Nagpur")) {

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(Appointmentbooking.this, R.layout.witnesslay, region7);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp2.setAdapter(dataAdapter2);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp2.setGravity(Gravity.CENTER);
        sp2.setBackgroundResource(R.drawable.spinnbackground);

        //region list

        regionlist.add("Akurdi");
        regionlist.add("Alandi Devachi");
        regionlist.add("Alandi Khed");
        regionlist.add("Alandi Road");
        regionlist.add("Ambegaon BK");
        regionlist.add("Anandnagar");
        regionlist.add("Aundh");
        regionlist.add("Aundh Road");
        regionlist.add("Balaji Nagar");
        regionlist.add("Balewadi");
        regionlist.add("Baner");
        regionlist.add("Baner Road");
        regionlist.add("Bavdhan");
        regionlist.add("Bhandarkar Road");
        regionlist.add("Bhavani Peth");
        regionlist.add("Bhosari");
        regionlist.add("Bibvewadi");
        regionlist.add("Bopodi");
        regionlist.add("Budhwar Peth");
        regionlist.add("Bund Garden Road");
        regionlist.add("Camp");
        regionlist.add("Chakan");
        regionlist.add("Chikhali");
        regionlist.add("Chinchwad");
        regionlist.add("Dapodi");
        regionlist.add("Dattawadi");
        regionlist.add("Deccan Gymkhana");
        regionlist.add("Dehu Road");
        regionlist.add("Dhankawadi");
        regionlist.add("Dhanori");
        regionlist.add("Dhayari");
        regionlist.add("Dhole Patil Road");
        regionlist.add("Dighi Camp");
        regionlist.add("Erandwana");
        regionlist.add("Fatima Nagar");
        regionlist.add("Ganesh Peth");
        regionlist.add("Ganeshkhind");
        regionlist.add("Ghorpade Peth");
        regionlist.add("Gokhale Nagar");
        regionlist.add("Gultekdi");
        regionlist.add("Guruwar Peth");
        regionlist.add("Hadapsar");
        regionlist.add("Handevadi");
        regionlist.add("Hingane Khurd");
        regionlist.add("Hinjewadi");
        regionlist.add("Kalewadi");
        regionlist.add("Kalyani Nagar");
        regionlist.add("Karve Nagar");
        regionlist.add("Karve Road");
        regionlist.add("Kasarwadi");
        regionlist.add("Kasba Peth");
        regionlist.add("Katraj");
        regionlist.add("Khadakwasla");
        regionlist.add("Khadki");
        regionlist.add("Kharadi");
        regionlist.add("Khed");
        regionlist.add("Kondhwa");
        regionlist.add("Kondhwa Budruk");
        regionlist.add("Kondhwa Khurd");
        regionlist.add("Koregaon Park");
        regionlist.add("Kothrud");
        regionlist.add("Law College Road");
        regionlist.add("Laxmi Road");
        regionlist.add("Lohegaon");
        regionlist.add("Loni Kalbhor");
        regionlist.add("Lulla Nagar");
        regionlist.add("Mangalwar Peth");
        regionlist.add("Manik Baug");
        regionlist.add("Market Yard");
        regionlist.add("Model Colony");
        regionlist.add("Mukund Nagar");
        regionlist.add("Mundhawa");
        regionlist.add("Nagar Road");
        regionlist.add("Nana Peth");
        regionlist.add("Narayan Peth");
        regionlist.add("Narayangaon");
        regionlist.add("Navi Peth");
        regionlist.add("Navsahyadri");
        regionlist.add("Nigdi");
        regionlist.add("Padmavati");
        regionlist.add("Parvati");
        regionlist.add("Pashan");
        regionlist.add("Paud Road");
        regionlist.add("Phursungi");
        regionlist.add("Pimple Gurav");
        regionlist.add("Pimple Nilakh");
        regionlist.add("Pimple Saudagar");
        regionlist.add("Pimpri");
        regionlist.add("Pirangut");
        regionlist.add("Prabhat Road");
        regionlist.add("Pune Railway Station");
        regionlist.add("Range Hill");
        regionlist.add("Rasta Peth");
        regionlist.add("Raviwar Peth");
        regionlist.add("S.P. College");
        regionlist.add("Sadashiv Peth");
        regionlist.add("Sahakar Nagar");
        regionlist.add("Salunke Vihar");
        regionlist.add("Sangavi");
        regionlist.add("Senapati Bapat Marg");
        regionlist.add("Shaniwar Peth");
        regionlist.add("Shivaji Nagar");
        regionlist.add("Shukrawar Peth");
        regionlist.add("Sinhagad Road");
        regionlist.add("Somwar Peth");
        regionlist.add("Sus");
        regionlist.add("Swargate");
        regionlist.add("Talegaon Dabhade");
        regionlist.add("Thergaon");
        regionlist.add("Tilak Road");
        regionlist.add("Undri");
        regionlist.add("Uruli");
        regionlist.add("Vadgaon Budruk");
        regionlist.add("Wadgaon Sheri");
        regionlist.add("Viman Nagar");
        regionlist.add("Vishrantwadi");
        regionlist.add("Wagholi");
        regionlist.add("Wakad");
        regionlist.add("Wakadewadi");
        regionlist.add("Wanawadi");
        regionlist.add("Warje");
        regionlist.add("Warje Malwadi");
        regionlist.add("Yerawada");
        regionlist.add("Fort");
        regionlist.add("Girgaon");
        regionlist.add("Lower-Parel");
        regionlist.add("Mahim");
        regionlist.add("Malabar");
        regionlist.add("Mandavi");
        regionlist.add("Matunga-Central");
        regionlist.add("Naigaon");
        regionlist.add("Parel");
        regionlist.add("Parela-shiwadi");
        regionlist.add("Sion");
        regionlist.add("Tardev");
        regionlist.add("Varali");
        regionlist.add("Bhuleshwar");
        regionlist.add("Byculla");
        regionlist.add("Culaba R");
        regionlist.add("Dadar");
        regionlist.add("Pavai");
        regionlist.add("Kondivita");
        regionlist.add("Poisar");
        regionlist.add("Kurla");
        regionlist.add("Ghatkopar");
        regionlist.add("Madh");
        regionlist.add("Gorai");
        regionlist.add("Majas");
        regionlist.add("Goregaon");
        regionlist.add("Shimpavali");
        regionlist.add("Kandivali");
        regionlist.add("Tungaona");
        regionlist.add("Oshivara");
        regionlist.add("Kanheri");
        regionlist.add("Turbhe");
        regionlist.add("Sahar");
        regionlist.add("Varsova");
        regionlist.add("Kurar");
        regionlist.add("Magathane");
        regionlist.add("Mahul");
        regionlist.add("Akse");
        regionlist.add("Malad");
        regionlist.add("Akurli");
        regionlist.add("Malavani");
        regionlist.add("Ambivali");
        regionlist.add("Andheri");
        regionlist.add("Asalpha");
        regionlist.add("Bandra");
        regionlist.add("Mankhurd");
        regionlist.add("Bhandup");
        regionlist.add("Borivali");
        regionlist.add("Chandivali");
        regionlist.add("Maroshi");
        regionlist.add("Chakala");
        regionlist.add("Charkop");
        regionlist.add("Marve");
        regionlist.add("Chembur");
        regionlist.add("Mulund");
        regionlist.add("Gundavali");
        regionlist.add("Dahisar");
        regionlist.add("Nahur");
        regionlist.add("Juhu");
        regionlist.add("Dindoshi");
        regionlist.add("CBD Belapur");
        regionlist.add("Dahisar");
        regionlist.add("Diva");
        regionlist.add("Bamandongri");
        regionlist.add("Kalyan");
        regionlist.add("Nerul");
        regionlist.add("Palghar");
        regionlist.add("Thane");
        regionlist.add("Turbhe");
        regionlist.add("Ulhasnagar");
        regionlist.add("Vashi");
        regionlist.add("Murabad");
        regionlist.add("Shahapur");
        regionlist.add("Talasari");
        regionlist.add("Vasai");
        regionlist.add("Ambarnath");
        regionlist.add("Mumbai C.S.T");
        regionlist.add("Masjid");
        regionlist.add("Sandhurst Road");
        regionlist.add("Chinchpokli");
        regionlist.add("Currey Road");
        regionlist.add("Cotton Green");
        regionlist.add("Sewari");
        regionlist.add("Vadala");
        regionlist.add("King Circle");
        regionlist.add("Chunabhatti");
        regionlist.add("Churchgate");
        regionlist.add("Marin Lines");
        regionlist.add("Chami Road");
        regionlist.add("Grant Road");
        regionlist.add(" Mumbai Central");
        regionlist.add("Mahalaxmi");
        regionlist.add("Elphinstone Road");
        regionlist.add("Worli");
        regionlist.add("Culaba");
        regionlist.add("Malabar Hill");
        regionlist.add("Kharghar");
        regionlist.add("Mansarovar");
        regionlist.add("Khandeshwar");
        regionlist.add("Panvel");
        regionlist.add("New Panvel");
        regionlist.add("Komthe");
        regionlist.add("Taloja");
        regionlist.add("Kalamboli");
        regionlist.add("Sanpada");
        regionlist.add("Seawoods");
        regionlist.add("Kopar-Khairane");
        regionlist.add("Ghansoli");
        regionlist.add("Rabale");
        regionlist.add("Airoli");
        regionlist.add("Khar");
        regionlist.add("Santacruz");
        regionlist.add("Vile Parle");
        regionlist.add("Jogeshwari");
        regionlist.add("Vidhyavihar");
        regionlist.add("Vikhroli");
        regionlist.add("Kanjurmarg");
        regionlist.add("Tilaknagar");
        regionlist.add("Govandi");
        regionlist.add("Sakinaka");
        regionlist.add("Mira Road");
        regionlist.add("Bhayander");
        regionlist.add("Virar");
        regionlist.add("Vaitarna");
        regionlist.add("Saphale");
        regionlist.add("Boisar");
        regionlist.add("Dahanu Road");
        regionlist.add("Kalwa");
        regionlist.add("Mumbra");
        regionlist.add("Dombivali");
        regionlist.add("Thakurli");
        regionlist.add("Shahad");
        regionlist.add("Titwala");
        regionlist.add("Nilaje");
        regionlist.add("Shilphata");
        regionlist.add("Nashik");
        regionlist.add("Nagpur");
        regionlist.add(" Koregaon Bhima");
        regionlist.add("Ranjangaon");
        regionlist.add("Daund");
        regionlist.add("Lonavala");
        regionlist.add(" Rajguru Nagar");
        regionlist.add("Kasara");
        regionlist.add("Karjat");
        regionlist.add("Khopoli");
        regionlist.add("Pen");
        regionlist.add("Alibaug");
        regionlist.add("Roha");
        regionlist.add("Karnala");
        regionlist.add("Khalapur");
        regionlist.add("GTBNagar");
        regionlist.add("Prabhadevi");
        regionlist.add("Rammandir");
        regionlist.add("Kopar");
        regionlist.add("Palava city");
        regionlist.add("Vithalwadi");
        regionlist.add("Balapur");
        regionlist.add("Vangani");
        regionlist.add("Shelu");
        regionlist.add("BhivpuriRoad");
        regionlist.add("Palasdhari");
        regionlist.add("Kelavli");
        regionlist.add("Dolavli");
        regionlist.add("Khadavali");
        regionlist.add("Vashind");
        regionlist.add("Asangaon");
        regionlist.add("Kelva Road");
        regionlist.add("Umroliroad");
        regionlist.add("Vangaon");
        regionlist.add("Kharkopar");
        regionlist.add("Matunga-Western");



        //city 1
        region.add("Akurdi");
        region.add("Alandi Devachi");
        region.add("Alandi Khed");
        region.add("Alandi Road");
        region.add("Ambegaon BK");
        region.add("Anandnagar");
        region.add("Aundh");
        region.add("Aundh Road");
        region.add("Balaji Nagar");
        region.add("Balewadi");
        region.add("Baner");
        region.add("Baner Road");
        region.add("Bavdhan");
        region.add("Bhandarkar Road");
        region.add("Bhavani Peth");
        region.add("Bhosari");
        region.add("Bibvewadi");
        region.add("Bopodi");
        region.add("Budhwar Peth");
        region.add("Bund Garden Road");
        region.add("Camp");
        region.add("Chakan");
        region.add("Chikhali");
        region.add("Chinchwad");
        region.add("Dapodi");
        region.add("Dattawadi");
        region.add("Deccan Gymkhana");
        region.add("Dehu Road");
        region.add("Dhankawadi");
        region.add("Dhanori");
        region.add("Dhayari");
        region.add("Dhole Patil Road");
        region.add("Dighi Camp");
        region.add("Erandwana");
        region.add("Fatima Nagar");
        region.add("Ganesh Peth");
        region.add("Ganeshkhind");
        region.add("Ghorpade Peth");
        region.add("Gokhale Nagar");
        region.add("Gultekdi");
        region.add("Guruwar Peth");
        region.add("Hadapsar");
        region.add("Handevadi");
        region.add("Hingane Khurd");
        region.add("Hingne Khurd");
        region.add("Hinjewadi");
        region.add("Kalewadi");
        region.add("Kalyani Nagar");
        region.add("Karve Nagar");
        region.add("Kasarwadi");
        region.add("Kasba Peth");
        region.add("Katraj");
        region.add("Khadakwasla");
        region.add("Khadki");
        region.add("Kharadi");
        region.add("Khed");
        region.add("Kondhwa");
        region.add("Kondhwa Budruk");
        region.add("Kondhwa Khurd");
        region.add("Koregaon Park");
        region.add("Kothrud");
        region.add("Law College Road");
        region.add("Laxmi Road");
        region.add("Lohegaon");
        region.add("Loni Kalbhor");
        region.add("Lulla Nagar");
        region.add("Mangalwar Peth");
        region.add("Manik Baug");
        region.add("Market Yard");
        region.add("Model Colony");
        region.add("Mukund Nagar");
        region.add("Mundhawa");
        region.add("Nana Peth");
        region.add("Narayan Peth");
        region.add("Narayangaon");
        region.add("Navi Peth");
        region.add("Navsahyadri");
        region.add("Nigdi");
        region.add("Padmavati");
        region.add("Parvati");
        region.add("Pashan");
        region.add("Paud Road");
        region.add("Phursungi");
        region.add("Pimple Gurav");
        region.add("Pimple Nilakh");
        region.add("Pimple Saudagar");
        region.add("Pimpri");
        region.add("Pirangut");
        region.add("Prabhat Road");
        region.add("Pune Railway Station");
        region.add("Range Hill");
        region.add("Rasta Peth");
        region.add("Raviwar Peth");
        region.add("S.P. College");
        region.add("Sadashiv Peth");
        region.add("Sahakar Nagar");
        region.add("Salunke Vihar");
        region.add("Sangavi");
        region.add("Senapati Bapat Marg");
        region.add("Shaniwar Peth");
        region.add("Shivaji Nagar");
        region.add("Shukrawar Peth");
        region.add("Sinhagad Road");
        region.add("Somwar Peth");
        region.add("Sus");
        region.add("Swargate");
        region.add("Talegaon Dabhade");
        region.add("Thergaon");
        region.add("Tilak Road");
        region.add("Undri");
        region.add("Uruli");
        region.add("Vadgaon Budruk");
        region.add("Wadgaon Sheri");
        region.add("Viman Nagar");
        region.add("Vishrantwadi");
        region.add("Wagholi");
        region.add("Wakad");
        region.add("Wakadewadi");
        region.add("Wanawrie");
        region.add("Warje");
        region.add("Warje Malwadi");
        region.add("Yerawada");
        region.add("Koregaon Bhima");
        region.add("Ranjangaon");
        region.add("Daund");
        region.add("Lonavala");
        region.add("Rajguru Nagar");

        //city 2
        region1.add("Fort");
        region1.add("Girgaon");
        region1.add("Lower-Parel");
        region1.add("Mahim");
        region1.add("Malabar");
        region1.add("Mandavi");
        region1.add("Matunga-Central");
        region1.add("Naigaon");
        region1.add("Parel");
        region1.add("Sion");
        region1.add("Tardev");
        region1.add("Varali");
        region1.add("Bhuleshwar");
        region1.add("Byculla");
        region1.add("Culaba R");
        region1.add("Dadar");
        region1.add("Mumbai C.S.T");
        region1.add("Masjid");
        region1.add("Sandhurst Road");
        region1.add("Chinchpokli");
        region1.add("Currey Road");
        region1.add("Cotton Green");
        region1.add("Lower Parel");
        region1.add("Sewari");
        region1.add("Vadala");
        region1.add("King Circle");
        region1.add("Chunabhatti");
        region1.add("Churchgate");
        region1.add("Marin Lines");
        region1.add("Charni Road");
        region1.add("Grant Road");
        region1.add(" Mumbai Central");
        region1.add("Mahalaxmi");
        region1.add("Elphinstone Road");
        region1.add("Worli");
        region1.add("Culaba");
        region1.add("GTBNagar");
        region1.add("Prabhadevi");
        region1.add("Rammandir");
        region1.add("Matunga-Western");



        //city 3

        region2.add("Pavai");
        region2.add("Kondivita");
        region2.add("Poisar");
        region2.add("Kurla");
        region2.add("Ghatkopar");
        region2.add("Madh");
        region2.add("Gorai");
        region2.add("Majas");
        region2.add("Goregaon");
        region2.add("Shimpavali");
        region2.add("Kandivali");
        region2.add("Tungaona");
        region2.add("Oshivara");
        region2.add("Kanheri");
        region2.add("Turbhe");
        region2.add("Sahar");
        region2.add("Varsova");
        region2.add("Kurar");
        region2.add("Magathane");
        region2.add("Alse");
        region2.add("Mahul");
        region2.add("Malad");
        region2.add("Akurli");
        region2.add("Malavani");
        region2.add("Ambivali");
        region2.add("Andheri");
        region2.add("Asalpha");
        region2.add("Bandra");
        region2.add("Mankhurd");
        region2.add("Bhandup");
        region2.add("Borivali");
        region2.add("Chandivali");
        region2.add("Maroshi");
        region2.add("Chakala");
        region2.add("Charkop");
        region2.add("Marve");
        region2.add("Chembur");
        region2.add("Mulund");
        region2.add("Gundavali");
        region2.add("Dahisar");
        region2.add("Nahur");
        region2.add("Juhu");
        region2.add("Devanar");
        region2.add("Pasapoli");
        region2.add("Khari");
        region2.add("Dindoshi");
        region2.add("Khar");
        region2.add("Santacruz");
        region2.add("Vile Parle");
        region2.add("Jogeshwari");
        region2.add("Vidhyavihar");
        region2.add("Vikhroli");
        region2.add("Kanjurmarg");
        region2.add("Tilaknagar");
        region2.add("Govandi");
        region2.add("Sakinaka");
        region2.add("Kelva Road");
        region2.add("Umroliroad");
        region2.add("Vangaon");

        //city 4
        region3.add("CBD Belapur");
        region3.add("Dahisar");
        region3.add("Diva");
        region3.add("Bamandomgiri");
        region3.add("Kalyan");
        region3.add("Nerul");
        region3.add("Palghar");
        region3.add("Thane");
        region3.add("Turbhe");
        region3.add("Ulhasnagar");
        region3.add("Vashi");
        region3.add("Murabad");
        region3.add("Shahapur");
        region3.add("Talasari");
        region3.add("Vasai");
        region3.add("Ambarnath");
        region3.add("Mira Road");
        region3.add("Bhayander");
        region3.add("Virar");
        region3.add("Vaitarna");
        region3.add("Saphale");
        region3.add("Boisar");
        region3.add("Dahanu Road");
        region3.add("Kalwa");
        region3.add("Mumbra");
        region3.add("Dombivali");
        region3.add("Thakurli");
        region3.add("Shahad");
        region3.add("Titwala");
        region3.add("Nilaje");
        region3.add("Shilphata");
        region3.add("Kasara");
        region3.add("Kopar");
        region3.add("Palava city");
        region3.add("Vithalwadi");
        region3.add("Balapur");
        region3.add("Vangani");
        region3.add("Shelu");
        region3.add("BhivpuriRoad");
        region3.add("Palasdhari");
        region3.add("Kelavli");
        region3.add("Dolavli");
        region3.add("Khadavali");
        region3.add("Vashind");
        region3.add("Asangaon");


        //city 5
        region4.add("Kharghar");
        region4.add("Mansarovar");
        region4.add("Khandeshwar");
        region4.add("Panvel");
        region4.add("New Panvel");
        region4.add("Komthe");
        region4.add("Taloja");
        region4.add("Kalamboli");
        region4.add("Karjat");
        region4.add("Khopoli");
        region4.add("Pen");
        region4.add("Alibaug");
        region4.add("Roha");
        region4.add("Karnala");
        region4.add("Khalapur");
        region4.add("Kharkopar");

        //city 6
        region5.add("Sanpada");
        region5.add("Seawoods");
        region5.add("Kopar-Khairane");
        region5.add("Ghansoli");
        region5.add("Rabale");
        region5.add("Airoli");

        //city7
        region6.add("Nashik");

        //city8
        region7.add("Nagpur");
//division

        division7.add("Akurdi");
        division6.add("Alandi Devachi");
        division7.add("Alandi Khed");
        division7.add("Alandi Road");
        division5.add("Ambegaon BK");
        division5.add("Anandnagar");
        division7.add("Aundh");
        division7.add("Aundh Road");
        division5.add("Balaji Nagar");
        division7.add("Balewadi");
        division7.add("Baner");
        division7.add("Baner Road");
        division5.add("Bavdhan");
        division5.add("Bhandarkar Road");
        division5.add("Bhavani Peth");
        division7.add("Bhosari");
        division5.add("Bibvewadi");
        division7.add("Bopodi");
        division5.add("Budhwar Peth");
        division6.add("Bund Garden Road");
        division6.add("Camp");
        division7.add("Chakan");
        division7.add("Chikhali");
        division7.add("Chinchwad");
        division7.add("Dapodi");
        division5.add("Dattawadi");
        division5.add("Deccan Gymkhana");
        division7.add("Dehu Road");
        division5.add("Dhankawadi");
        division6.add("Dhanori");
        division5.add("Dhayari");
        division5.add("Dhole Patil Road");
        division7.add("Dighi Camp");
        division5.add("Erandwana");
        division6.add("Fatima Nagar");
        division5.add("Ganesh Peth");
        division5.add("Ganeshkhind");
        division7.add("Ghorpade Peth");
        division5.add("Gokhale Nagar");
        division6.add("Gultekdi");
        division5.add("Guruwar Peth");
        division7.add("Hadapsar");
        division5.add("Handevadi");
        division5.add("Hingane Khurd");
        division5.add("Hinjewadi");
        division5.add("Kalewadi");
        division6.add("Kalyani Nagar");
        division6.add("Karve Nagar");
        division5.add("Karve Road");
        division7.add("Kasarwadi");
        division7.add("Kasba Peth");
        division6.add("Katraj");
        division5.add("Khadakwasla");
        division7.add("Khadki");
        division7.add("Kharadi");
        division6.add("Khed");
        division5.add("Kondhwa");
        division7.add("Kondhwa Budruk");
        division5.add("Kondhwa Khurd");
        division5.add("Koregaon Park");
        division5.add("Kothrud");
        division7.add("Law College Road");
        division6.add("Laxmi Road");
        division7.add("Lohegaon");
        division5.add("Loni Kalbhor");
        division5.add("Lulla Nagar");
        division5.add("Mangalwar Peth");
        division6.add("Manik Baug");
        division5.add("Market Yard");
        division5.add("Model Colony");
        division5.add("Mukund Nagar");
        division6.add("Mundhawa");
        division6.add("Nagar Road");
        division6.add("Nana Peth");
        division5.add("Narayan Peth");
        division5.add("Narayangaon");
        division5.add("Navi Peth");
        division5.add("Navsahyadri");
        division5.add("Nigdi");
        division6.add("Padmavati");
        division5.add("Parvati");
        division5.add("Pashan");
        division6.add("Paud Road");
        division5.add("Phursungi");
        division5.add("Pimple Gurav");
        division7.add("Pimple Nilakh");
        division5.add("Pimple Saudagar");
        division5.add("Pimpri");
        division5.add("Pirangut");
        division5.add("Prabhat Road");
        division6.add("Pune Railway Station");
        division7.add("Range Hill");
        division7.add("Rasta Peth");
        division7.add("Raviwar Peth");
        division7.add("S.P. College");
        division5.add("Sadashiv Peth");
        division5.add("Sahakar Nagar");
        division5.add("Salunke Vihar");
        division5.add("Sangavi");
        division5.add("Senapati Bapat Marg");
        division5.add("Shaniwar Peth");
        division5.add("Shivaji Nagar");
        division5.add("Shukrawar Peth");
        division5.add("Sinhagad Road");
        division6.add("Somwar Peth");
        division7.add("Sus");
        division5.add("Swargate");
        division5.add("Talegaon Dabhade");
        division5.add("Thergaon");
        division5.add("Tilak Road");
        division5.add("Undri");
        division5.add("Uruli");
        division5.add("Vadgaon Budruk");
        division5.add("Wadgaon Sheri");
        division7.add("Viman Nagar");
        division7.add("Vishrantwadi");
        division5.add("Wagholi");
        division6.add("Wakad");
        division6.add("Wakadewadi");
        division5.add("Wanawadi");
        division6.add("Warje");
        division6.add("Warje Malwadi");
        division6.add("Yerawada");
        division6.add("Fort");
        division7.add("Girgaon");
        division6.add("Lower-Parel");
        division6.add("Mahim");
        division5.add("Malabar");
        division5.add("Mandavi");
        division6.add("Matunga-Central");
        division3.add("Naigaon");
        division3.add("Parel");
        division3.add("Parela-shiwadi");
        division3.add("Sion");
        division3.add("Tardev");
        division3.add("Varali");
        division2.add("Bhuleshwar");
        division13.add("Byculla");
        division2.add("Culaba R");
        division2.add("Dadar");
        division3.add("Pavai");
        division3.add("Kondivita");
        division3.add("Poisar");
        division2.add("Kurla");
        division3.add("Ghatkopar");
        division2.add("Madh");
        division4.add("Gorai");
        division4.add("Majas");
        division4.add("Goregaon");
        division2.add("Shimpavali");
        division2.add("Kandivali");
        division4.add("Tungaona");
        division4.add("Oshivara");
        division4.add("Kanheri");
        division4.add("Turbhe");
        division4.add("Sahar");
        division4.add("Varsova");
        division4.add("Kurar");
        division4.add("Magathane");
        division4.add("Mahul");
        division2.add("Akse");
        division4.add("Malad");
        division4.add("Akurli");
        division4.add("Malavani");
        division4.add("Ambivali");
        division4.add("Andheri");
        division4.add("Asalpha");
        division4.add("Bandra");
        division4.add("Mankhurd");
        division4.add("Bhandup");
        division4.add("Borivali");
        division4.add("Chandivali");
        division4.add("Maroshi");
        division3.add("Chakala");
        division2.add("Charkop");
        division2.add("Marve");
        division13.add("Chembur");
        division4.add("Mulund");
        division4.add("Gundavali");
        division4.add("Dahisar");
        division4.add("Nahur");
        division4.add("Juhu");
        division2.add("Dindoshi");
        division2.add("CBD Belapur");
        division4.add("Dahisar");
        division13.add("Diva");
        division2.add("Bamandongri");
        division4.add("Kalyan");
        division4.add("Nerul");
        division11.add("Palghar");
        division13.add("Thane");
        division9.add("Turbhe");
        division11.add("Ulhasnagar");
        division9.add("Vashi");
        division11.add("Murabad");
        division13.add("Shahapur");
        division9.add("Talasari");
        division12.add("Vasai");
        division9.add("Ambarnath");
        division12.add("Mumbai C.S.T");
        division9.add("Masjid");
        division9.add("Sandhurst Road");
        division9.add("Chinchpokli");
        division13.add("Currey Road");
        division9.add("Cotton Green");
        division2.add("Sewari");
        division2.add("Vadala");
        division2.add("King Circle");
        division2.add("Chunabhatti");
        division2.add("Churchgate");
        division2.add("Marin Lines");
        division2.add("Chami Road");
        division2.add("Grant Road");
        division2.add(" Mumbai Central");
        division2.add("Mahalaxmi");
        division3.add("Elphinstone Road");
        division3.add("Worli");
        division3.add("Culaba");
        division3.add("Malabar Hill");
        division3.add("Kharghar");
        division3.add("Mansarovar");
        division3.add("Khandeshwar");
        division3.add("Panvel");
        division3.add("New Panvel");
        division11.add("Komthe");
        division11.add("Taloja");
        division11.add("Kalamboli");
        division11.add("Sanpada");
        division11.add("Seawoods");
        division11.add("Kopar-Khairane");
        division11.add("Ghansoli");
        division11.add("Rabale");
        division12.add("Airoli");
        division11.add("Khar");
        division12.add("Santacruz");
        division12.add("Vile Parle");
        division12.add("Jogeshwari");
        division12.add("Vidhyavihar");
        division3.add("Vikhroli");
        division3.add("Kanjurmarg");
        division3.add("Tilaknagar");
        division4.add("Govandi");
        division2.add("Sakinaka");
        division2.add("Mira Road");
        division2.add("Bhayander");
        division2.add("Virar");
        division2.add("Vaitarna");
        division4.add("Saphale");
        division13.add("Boisar");
        division13.add("Dahanu Road");
        division13.add("Kalwa");
        division13.add("Mumbra");
        division13.add("Dombivali");
        division13.add("Thakurli");
        division13.add("Shahad");
        division9.add("Titwala");
        division9.add("Nilaje");
        division9.add("Shilphata");
        division14.add("Nashik");
        division15.add("Nagpur");
        division9.add(" Koregaon Bhima");
        division11.add("Ranjangaon");
        division9.add("Daund");
        division6.add("Lonavala");
        division6.add(" Rajguru Nagar");
        division6.add("Kasara");
        division7.add("Karjat");
        division7.add("Khopoli");
        division9.add("Pen");
        division11.add("Alibaug");
        division9.add("Roha");
        division11.add("Karnala");
        division11.add("Khalapur");
        division11.add("GTBNagar");
        division11.add("Prabhadevi");
        division11.add("Rammandir");
        division2.add("Kopar");
        division3.add("Palava city");
        division4.add("Vithalwadi");
        division9.add("Balapur");
        division9.add("Vangani");
        division9.add("Shelu");
        division9.add("BhivpuriRoad");
        division9.add("Palasdhari");
        division9.add("Kelavli");
        division9.add("Dolavli");
        division9.add("Khadavali");
        division9.add("Vashind");
        division9.add("Asangaon");
        division9.add("Kelva Road");
        division13.add("Umroliroad");
        division13.add("Vangaon");
        division13.add("Kharkopar");
        division11.add("Matunga-Western");





        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(Appointmentbooking.this, R.layout.witnesslay, regionlist);

        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(dataAdapter2);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mregion = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ScrollView m_Scroll = new ScrollView(this);
        m_Scroll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));


        Toolbar toolbar = new Toolbar(this);
        toolbar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        toolbar.setBackgroundColor(Color.parseColor("#3b5998"));
        toolbar.setTitle("Appointment Booking");
        toolbar.setTitleTextColor(Color.WHITE);
        rootView.addView(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rootView.setOrientation(LinearLayout.VERTICAL);

        rootView.addView(citytext);
        rootView.addView(sp1);
        rootView.addView(regiontext);
        rootView.addView(sp2);
        rootView.addView(regiontext1);
        rootView.addView(update1);
        rootView.addView(slottime);
        rootView.addView(regiontext2);
        rootView.addView(sp3);
        rootView.addView(txt);
        rootView.addView(txtdate);
        rootView.addView(attendees);
        rootView.addView(tv);
        rootView.addView(check1);
        rootView.addView(tv1);
        rootView.addView(check2);


        m_Scroll.addView(rootView);

        setContentView(m_Scroll);


        update1.setOnClickListener(new View.OnClickListener() {

            final Calendar myCalendar = Calendar.getInstance();

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                StartTime = new DatePickerDialog(Appointmentbooking.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        slottime.setText(sdf.format(myCalendar.getTime()));
                        datevalue = slottime.getText().toString();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            date = dateFormat.parse(datevalue);
                            date2 = dateFormat.parse(current_Date);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }


                        if (datevalue != datevalue1) {
                            if (division1.contains(mregion)) {
                                division_id = "1";
                            } else if (division2.contains(mregion)) {
                                division_id = "2";
                            } else if (division3.contains(mregion)) {
                                division_id = "3";
                            } else if (division4.contains(mregion)) {
                                division_id = "4";
                            } else if (division5.contains(mregion)) {
                                division_id = "5";
                            } else if (division6.contains(mregion)) {
                                division_id = "6";
                            } else if (division7.contains(mregion)) {
                                division_id = "7";
                            } else if (division9.contains(mregion)) {
                                division_id = "9";
                            } else if (division11.contains(mregion)) {
                                division_id = "11";
                            } else if (division12.contains(mregion)) {
                                division_id = "12";
                            } else if (division13.contains(mregion)) {
                                division_id = "13";
                            }else if (division14.contains(mregion)) {
                                division_id = "14";
                            }else if (division15.contains(mregion)) {
                                division_id = "15";
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    txtdate.setVisibility(View.GONE);
                                    Intent i2 = new Intent(Appointmentbooking.this, getappointmentslot.class);
                                    Toast.makeText(Appointmentbooking.this, "GETTING TIME SLOTS", Toast.LENGTH_LONG).show();

                                    i2.putExtra("app_date", datevalue);
                                    i2.putExtra("division_id", division_id);
                                    i2.putExtra("doc",reportkey);
                                    startService(i2);

                                }
                            });
                            singleTask.schedule(new TimerTask() {

                                @Override
                                public void run() {

                                    if (GenericMethods.isConnected(getApplicationContext())) {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                DBOperation db = new DBOperation(Appointmentbooking.this);

                                                if (GenericMethods.app_value.equals("true")) {

                                                    if (db.getAppointmentslot(db).size() == 0) {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                regiontext2.setVisibility(View.GONE);
                                                                sp3.setVisibility(View.GONE);
                                                                txt.setVisibility(View.VISIBLE);
                                                                txtdate.setVisibility(View.INVISIBLE);
                                                            }
                                                        });
                                                    }


                                                    for (int i = 0; i < db.getAppointmentslot(db).size(); i++) {


                                                        if ((db.getAppointmentslot(db).get(i).get("Available").equals("true") && db.getAppointmentslot(db).get(i).get("Block").equals("0"))) {

                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {

                                                                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<>(Appointmentbooking.this, R.layout.witnesslay, categories1);
                                                                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                    sp3.setAdapter(dataAdapter3);
                                                                    sp3.setVisibility(View.VISIBLE);
                                                                    regiontext2.setVisibility(View.VISIBLE);
                                                                    txt.setVisibility(View.INVISIBLE);
                                                                    txtdate.setVisibility(View.INVISIBLE);

                                                                }
                                                            });


                                                            starttime = db.getAppointmentslot(db).get(i).get("start_time").substring(0, 8);
                                                            endtime = db.getAppointmentslot(db).get(i).get("End_time").substring(0, 8);

                                                            if (db.getAppointmentslot(db).get(i).get("Discount").equals("true")) {

                                                                finaltime = starttime + "-" + endtime + "(Discount%)";
                                                            } else if (db.getAppointmentslot(db).get(i).get("Discount").equals("false")) {

                                                                finaltime = starttime + "-" + endtime;
                                                            }

                                                            categories1.add(finaltime);


                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<>(Appointmentbooking.this, R.layout.witnesslay, categories1);
                                                                    dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                                    sp3.setAdapter(dataAdapter3);
                                                                    sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

                                                                    {
                                                                        @Override
                                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                            slot = parent.getSelectedItem().toString();
                                                                            DBOperation db = new DBOperation(Appointmentbooking.this);
                                                                            for (int i = 0; i < db.getAppointmentslot(db).size(); i++) {

                                                                                if (slot.substring(0, 8).equals(db.getAppointmentslot(db).get(i).get("start_time").substring(0, 8)) && (slot.substring(9).equals(db.getAppointmentslot(db).get(i).get("End_time").substring(0, 8)))) {


                                                                                    slotid = db.getAppointmentslot(db).get(i).get("slot_id");

                                                                                }


                                                                            }


                                                                        }


                                                                        @Override
                                                                        public void onNothingSelected(AdapterView<?> parent) {

                                                                        }
                                                                    });

                                                                }
                                                            });


                                                        }
                                                        if (db.getAppointmentslot(db).get(i).get("Available").equals("false") || db.getAppointmentslot(db).get(i).get("Block").equals("1")) {
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {

                                                                    regiontext2.setVisibility(View.GONE);
                                                                    sp3.setVisibility(View.GONE);
                                                                    txt.setVisibility(View.VISIBLE);
                                                                    txtdate.setVisibility(View.INVISIBLE);

                                                                }
                                                            });
                                                        }


                                                    }


                                                }
                                            }
                                        });


                                    } else if (!GenericMethods.isConnected(getApplicationContext())) {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                slottime.setText("");
                                                Toast.makeText(Appointmentbooking.this, "Please Turn On the Internet", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }


                                }


                            }, 2000);


                        }

                    }

                },myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                StartTime.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                StartTime.show();
                          }
        });


        addattendees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name1.getText().toString().length() > 0 && email2.getText().toString().length() > 0 && contact2.getText().toString().length() > 0 && attendesadress2.getText().toString().length() > 0) {
                    namelist.add(name1.getText().toString());
                    emaillist.add(email2.getText().toString());
                    contactlist.add(contact2.getText().toString());
                    addresslist.add(attendesadress2.getText().toString());
                    contactperson1.add(name1.getText().toString());
                    contactmaillist.add(email2.getText().toString());
                    contactphonelist.add(contact2.getText().toString());
                    contactaddresslist.add(attendesadress2.getText().toString());

                    Snackbar snackbar = Snackbar.make(rootView, name1.getText().toString() + " " + "Added Successfully!!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar7 = Snackbar.make(rootView, "Please Enter the Details", Snackbar.LENGTH_LONG);
                    snackbar7.show();
                }
                if (add1.isChecked()) {
                    add1.setChecked(false);
                }

                name.setVisibility(View.GONE);
                name1.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                email2.setVisibility(View.GONE);
                contact.setVisibility(View.GONE);
                contact2.setVisibility(View.GONE);
                addattendees.setVisibility(View.GONE);

            }
        });


        if (apptype.equals("1")) {

            namelist.clear();
            emaillist.clear();
            contactlist.clear();

            if (con1.equals("Today")) {

                for (int i = 0; i < db.getAllTodayList(db).size(); i++) {
                    if (db.getAllTodayList(db).get(i).get("docid").equals(document)) {


                        check1.setText(db.getAllTodayList(db).get(i).get("oname"));
                        value = check1.getText().toString();

                        contactmail = db.getAllTodayList(db).get(i).get("omail");
                        contactphone = db.getAllTodayList(db).get(i).get("ocontact");
                        contactaddress = db.getAllTodayList(db).get(i).get("oadd");
                        party_address = contactaddress;

                        check2.setText(db.getAllTodayList(db).get(i).get("tname"));
                        value1 = check2.getText().toString();

                        contactmail1 = db.getAllTodayList(db).get(i).get("tmail");
                        contactphone1 = db.getAllTodayList(db).get(i).get("tcontact");
                        contactaddress1 = db.getAllTodayList(db).get(i).get("tadd");
                        party_address = contactaddress1;


                    }


                }


            }

            if (con1.equals("Older")) {

                for (int i = 0; i < db.getAllOlderist(db).size(); i++) {
                    if (db.getAllOlderist(db).get(i).get("docid").equals(document)) {

                        check1.setText(db.getAllOlderist(db).get(i).get("oname"));
                        value = check1.getText().toString();

                        contactmail = db.getAllOlderist(db).get(i).get("omail");
                        contactphone = db.getAllOlderist(db).get(i).get("ocontact");
                        contactaddress = db.getAllOlderist(db).get(i).get("oadd");
                        party_address = contactaddress;


                        check2.setText(db.getAllOlderist(db).get(i).get("tname"));
                        value1 = check2.getText().toString();

                        contactmail1 = db.getAllOlderist(db).get(i).get("tmail");
                        contactphone1 = db.getAllOlderist(db).get(i).get("tcontact");
                        contactaddress1 = db.getAllOlderist(db).get(i).get("tadd");
                        party_address = contactaddress1;

                    }
                }

            }
            if (con1.equals("New")) {

                for (int i = 0; i < db.getAllNewList(db).size(); i++) {
                    if (db.getAllNewList(db).get(i).get("docid").equals(document)) {

                        check1.setText(db.getAllNewList(db).get(i).get("oname"));
                        value = check1.getText().toString();
                        contactmail = db.getAllNewList(db).get(i).get("omail");
                        contactphone = db.getAllNewList(db).get(i).get("ocontact");
                        contactaddress = db.getAllNewList(db).get(i).get("oadd");
                        party_address = contactaddress;


                        check2.setText(db.getAllNewList(db).get(i).get("tname"));
                        value1 = check2.getText().toString();
                        contactaddress1 = db.getAllNewList(db).get(i).get("tadd");
                        contactmail1 = db.getAllNewList(db).get(i).get("tmail");
                        contactphone1 = db.getAllNewList(db).get(i).get("tcontact");
                        party_address = contactaddress1;

                    }
                }

            }
            if (con1.equals("Completed")) {

                for (int i = 0; i < db.getAllList(db).size(); i++) {
                    if (db.getAllList(db).get(i).get("docid").equals(document))


                    {

                        check1.setText(db.getAllList(db).get(i).get("oname"));
                        value = check1.getText().toString();

                        contactmail = db.getAllList(db).get(i).get("omail");
                        contactphone = db.getAllList(db).get(i).get("ocontact");
                        contactaddress = db.getAllList(db).get(i).get("oadd");
                        party_address = contactaddress;


                        check2.setText(db.getAllList(db).get(i).get("tname"));
                        value1 = check2.getText().toString();

                        contactmail1 = db.getAllList(db).get(i).get("tmail");
                        contactphone1 = db.getAllList(db).get(i).get("tcontact");
                        contactaddress1 = db.getAllList(db).get(i).get("tadd");
                        party_address = contactaddress1;

                    }
                }
            }
        } else if (apptype.equals("2")) {

            int o_count = 0, t_count = 0, w_count = 0, aw_count = 0, poa_count = 0;


            for (int i = 0; i < getAlldataList.size(); i++) {


                if (document.equals(getAlldataList.get(i).get("document_id"))) {

                    TextView tv2 = new TextView(this);
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            getResources().getDimension(R.dimen.font_size_medium));
                    tv2.setTextColor(Color.parseColor("#545454"));

                    CheckBox check3 = new CheckBox(this);
                    check3.setId(i);
                    check3.setTextSize(18);
                    check3.setTextColor(Color.parseColor("#000000"));
                    check3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    LinearLayout.LayoutParams params7 = (LinearLayout.LayoutParams) check3.getLayoutParams();
                    params7.setMargins(40, 0, 50, 0);

                    check3.setLayoutParams(params7);


                    if (getAlldataList.get(i).get("party_type").equals("1")) {


                        if (o_count == 0) {
                            tv2.setText("  Owner:");
                            o_count = o_count + 1;
                            rootView.addView(tv2);
                        }

                        check3.setText(getAlldataList.get(i).get("name"));
                        party_address = getAlldataList.get(i).get("party_address");

                        rootView.addView(check3);

                    } else if (getAlldataList.get(i).get("party_type").equals("2")) {


                        if (t_count == 0) {
                            tv2.setText("  Tenant:");
                            t_count = t_count + 1;
                            rootView.addView(tv2);
                        }
                        check3.setText(getAlldataList.get(i).get("name"));
                        party_address = getAlldataList.get(i).get("party_address");

                        rootView.addView(check3);

                    } else if (getAlldataList.get(i).get("party_type").equals("4")) {

                        if (w_count == 0) {
                            tv2.setText("  Witness:");
                            w_count = w_count + 1;
                            rootView.addView(tv2);
                        }
                        check3.setText(getAlldataList.get(i).get("name"));
                        party_address = getAlldataList.get(i).get("party_address");

                        rootView.addView(check3);

                    } else if (getAlldataList.get(i).get("party_type").equals("5")) {

                        if (aw_count == 0) {
                            tv2.setText("  Anulom Witness:");
                            aw_count = aw_count + 1;
                            rootView.addView(tv2);
                        }
                        check3.setText(getAlldataList.get(i).get("name"));
                        party_address = getAlldataList.get(i).get("party_address");

                        rootView.addView(check3);

                    } else if (getAlldataList.get(i).get("party_type").equals("3")) {

                        if (poa_count == 0) {
                            tv2.setText("  POA:");
                            poa_count = poa_count + 1;
                            rootView.addView(tv2);
                        }
                        check3.setText(getAlldataList.get(i).get("name"));
                        party_address = getAlldataList.get(i).get("party_address");

                        rootView.addView(check3);

                    }


                }


                check1.setVisibility(View.GONE);
                check2.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
                tv1.setVisibility(View.GONE);

            }


        }


        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (check1.isChecked()) {

                    namelist.add(value);
                    emaillist.add(contactmail);
                    contactlist.add(contactphone);
                    addresslist.add(contactaddress);
                    contactperson1.add(value);
                    contactmaillist.add(contactmail);
                    contactphonelist.add(contactphone);
                    contactaddresslist.add(contactaddress);

                } else if (!check1.isChecked()) {

                    if (namelist.contains(value)) {
                        namelist.remove(value);
                        emaillist.remove(contactmail);
                        contactlist.remove(contactphone);
                        addresslist.remove(contactaddress);
                        contactperson1.remove(value);
                        contactmaillist.remove(contactmail);
                        contactphonelist.remove(contactphone);
                        contactaddresslist.remove(contactaddress);
                    }
                }

            }
        });
        check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (check2.isChecked()) {
                    namelist.add(value1);
                    emaillist.add(contactmail1);
                    contactlist.add(contactphone1);
                    addresslist.add(contactaddress1);
                    contactperson1.add(value1);
                    contactmaillist.add(contactmail1);
                    contactphonelist.add(contactphone1);
                    contactaddresslist.add(contactaddress1);

                } else if (!check2.isChecked()) {

                    if (namelist.contains(value1)) {
                        namelist.remove(value1);
                        emaillist.remove(contactmail1);
                        contactlist.remove(contactphone1);
                        addresslist.remove(contactaddress1);

                        contactperson1.remove(value1);
                        contactmaillist.remove(contactmail1);
                        contactphonelist.remove(contactphone1);
                        contactaddresslist.remove(contactaddress);
                    }
                }

            }
        });

        contactperson1.add("-SELECT-");

        ArrayAdapter<String> dataAdapter8 = new ArrayAdapter<>(Appointmentbooking.this, R.layout.witnesslay, contactperson1);
        dataAdapter8.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp5.setAdapter(dataAdapter8);
        sp5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contactmaillist.add("select");
                contactphonelist.add("select");
                contactaddresslist.add("select");
                mperson1 = parent.getItemAtPosition(position).toString();
                mposition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rootView.addView(add1);
        rootView.addView(name);
        rootView.addView(name1);
        rootView.addView(email);
        rootView.addView(email2);
        rootView.addView(contact);
        rootView.addView(contact2);
        rootView.addView(attendeesaddress);
        rootView.addView(attendesadress2);
        rootView.addView(addattendees);
        rootView.addView(tv3);
        rootView.addView(sp5);
        rootView.addView(buildingtype);
        rootView.addView(rg);
        rootView.addView(addresstype);
        rootView.addView(rg1);
        rootView.addView(address);
        rootView.addView(line1);
        rootView.addView(address1);
        rootView.addView(landmark);
        rootView.addView(landmark1);
        rootView.addView(confirm);

        add = address1.getText().toString();
        land = landmark1.getText().toString();

        for (j = 0; j < getAlldataList.size(); j++) {
            if (document.equals(getAlldataList.get(j).get("document_id"))) {
                checkk = checkk + 1;
                final CheckBox chec = findViewById(j);


                chec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (chec.isChecked()) {

                            namelist.add(getAlldataList.get(chec.getId()).get("name"));
                            emaillist.add(getAlldataList.get(chec.getId()).get("email"));
                            contactlist.add(getAlldataList.get(chec.getId()).get("contact_no"));
                            addresslist.add(getAlldataList.get(chec.getId()).get("party_address"));
                            contactperson1.add(getAlldataList.get(chec.getId()).get("name"));
                            contactmaillist.add(getAlldataList.get(chec.getId()).get("email"));
                            contactphonelist.add(getAlldataList.get(chec.getId()).get("contact_no"));
                            contactaddresslist.add(getAlldataList.get(chec.getId()).get("party_address"));

                        } else if (!chec.isChecked()) {

                            if (namelist.contains(getAlldataList.get(chec.getId()).get("name"))) {
                                namelist.remove(getAlldataList.get(chec.getId()).get("name"));
                                emaillist.remove(getAlldataList.get(chec.getId()).get("email"));
                                addresslist.remove(getAlldataList.get(chec.getId()).get("party_address"));
                                contactlist.remove(getAlldataList.get(chec.getId()).get("contact_no"));
                                contactperson1.remove(getAlldataList.get(chec.getId()).get("name"));
                                contactmaillist.remove(getAlldataList.get(chec.getId()).get("email"));
                                contactphonelist.remove(getAlldataList.get(chec.getId()).get("contact_no"));
                                contactaddresslist.remove(getAlldataList.get(chec.getId()).get("party_address"));


                            }
                        }

                    }


                });

            }
        }
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (differentaddress.isChecked()) {


                    address.setVisibility(View.VISIBLE);
                    line1.setVisibility(View.VISIBLE);
                    address1.setVisibility(View.VISIBLE);


                } else if (!differentaddress.isChecked()) {

                    address.setVisibility(View.GONE);
                    line1.setVisibility(View.GONE);
                    address1.setVisibility(View.GONE);
                }

                if (contactpersonaddress.isChecked()) {
                    line2.setText(party_address);

                } else if (!contactpersonaddress.isChecked()) {
                    line2.setText("");

                }
            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GenericMethods.isConnected(getApplicationContext())) {

                    if(!landmark1.getText().toString().equals("")) {
                        Snackbar snackbar2 = Snackbar.make(rootView, "Refreshing...", Snackbar.LENGTH_LONG);
                        snackbar2.show();


                        for (j = 0; j < doclist.size(); j++) {

                            if (contactpersonaddress.isChecked()) {
                                party_address = addresslist.get(j);
                            } else if (differentaddress.isChecked()) {

                                party_address = option1 + "," + line1.getText().toString() + "," + address1.getText().toString();
                            }

                            SQLiteDatabase complaint = db.getWritableDatabase();
                            ContentValues valuesw = new ContentValues();

                            valuesw.put(DBManager.TableInfo.KEYID, ID1);
                            valuesw.put(DBManager.TableInfo.request_no, reportkey);
                            valuesw.put(DBManager.TableInfo.timenew, document);
                            valuesw.put(DBManager.TableInfo.slotid, slotid);
                            valuesw.put(DBManager.TableInfo.app_date, datevalue);
                            valuesw.put(DBManager.TableInfo.division_id, division_id);
                            valuesw.put(DBManager.TableInfo.region_id, mregion);
                            valuesw.put(DBManager.TableInfo.free, option);
                            valuesw.put(DBManager.TableInfo.free_reason, free_reason1.getText().toString());
                            valuesw.put(DBManager.TableInfo.attendees, namelist.get(j));
                            valuesw.put(DBManager.TableInfo.attendeesemail, emaillist.get(j));
                            valuesw.put(DBManager.TableInfo.attendeescontact, contactlist.get(j));
                            valuesw.put(DBManager.TableInfo.address, party_address);
                            valuesw.put(DBManager.TableInfo.landmark, landmark1.getText().toString());
                            valuesw.put(DBManager.TableInfo.contactperson, mperson1);
                            valuesw.put(DBManager.TableInfo.contactpersonemail, contactmaillist.get(mposition));
                            valuesw.put(DBManager.TableInfo.contactpersoncont, contactphonelist.get(mposition));
                            valuesw.put(DBManager.TableInfo.KEY_LOGIN_USER, username2);
                            valuesw.put(DBManager.TableInfo.city, mspin);
                            valuesw.put(DBManager.TableInfo.AppointmentId, appointment_id);
                            String conditionw = DBManager.TableInfo.request_no + " =?";

                            Cursor cursorw = complaint.query(APPOINTMENTBOOKING, null, conditionw, new String[]{DBManager.TableInfo.slotid1}, null, null, null);
                            long statusw = complaint.insert(APPOINTMENTBOOKING, null, valuesw);

                            cursorw.close();
                            complaint.close();
                            finish();


                        }

                        String commentID = randomInteger(0, 999999);

                        db.InsertRecord3(db, reportkey, "New Appointment Booked" + free_reason1.getText().toString(), username2, username2, commentID, "0", current_Date, GenericMethods.AsyncStatus, datevalue, val, "", "", "");

                        Intent intent = new Intent(Appointmentbooking.this, SendCommentService.class);
                        startService(intent);

                        Intent i = new Intent(Appointmentbooking.this, postappointmentbooking.class);
                        startService(i);


                        namelist.clear();
                        emaillist.clear();
                        contactlist.clear();
//                        singleTask.schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//
//
//                                Intent intent = new Intent(Appointmentbooking.this, GetAllDataList.class);
//                                startService(intent);
//                                finish();
//
//                            }
//
//
//                        }, startdelay);

                    } else {
                        Snackbar snackbar4 = Snackbar.make(rootView, "Enter All the Required Fields!!", Snackbar.LENGTH_LONG);
                        snackbar4.show();
                    }

                } else {
                    Snackbar snackbar4 = Snackbar.make(rootView, "Please Check the Internet Connection!!", Snackbar.LENGTH_LONG);
                    snackbar4.show();
                }
            }
        });


    }


    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&location=19.7515, 75.7139");
            String temp= "&input=" + URLEncoder.encode(input, "utf8");
            sb.append(temp);



            URL url = new URL(sb.toString());


            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());


            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            return resultList;
        } catch (IOException e) {

            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {


            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            resultList = new ArrayList<>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        resultList = autocomplete(constraint.toString());

                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public String randomInteger(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum + "";
    }

}






