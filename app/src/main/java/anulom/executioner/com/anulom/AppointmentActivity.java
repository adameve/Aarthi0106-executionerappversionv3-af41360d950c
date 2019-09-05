package anulom.executioner.com.anulom;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;

import anulom.executioner.com.anulom.database.DBOperation;
import anulom.executioner.com.anulom.fragment.biometricappointments;
import anulom.executioner.com.anulom.fragment.marriageappointments;

import static anulom.executioner.com.anulom.Login.umailid;

public class AppointmentActivity extends AppCompatActivity {

    String umail = umailid;
    EditText etusermailid = Login.etusermailid;

    private final Handler handler = new Handler();
    private TabLayout tabLayout;
    DBOperation database;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String Date, ID, val = "";
    Context context;
    TextView text1;
    private static long TIME_OUT_IN_SECONDS = 120;
    public static ProgressDialog pDialog;
    public static AlertDialog alertDialog;

    SearchManager searchManager;
    SearchView searchView;
    private SharedPreferences Date1;
    private String currentdate = "";
    public static NextActivity thisnext = null;
    DBOperation db;
    Timer singleTask = new Timer();
    int startdelay = 7000;
    // Tab titles
    String valuenext = "0", valuefromback = "";
    //search
    SearchView sv;
    int i = 0, pos;
    TextView tabTextView;
    BroadcastReceiver receiver1;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointmentlayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.drawable.icon2);


        ViewPager viewPager = findViewById(R.id.viewpager5);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs5);
        tabLayout.setupWithViewPager(viewPager);
        SharedPreferences usermail = PreferenceManager.getDefaultSharedPreferences(this);
        String username2 = usermail.getString("username", "");
        String password2 = usermail.getString("password", "");


        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            TabLayout.Tab tab = tabLayout.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(AppointmentActivity.this).inflate(R.layout.tab_layout, tabLayout, false);
            tabTextView = relativeLayout.findViewById(R.id.tab_title);
            tabTextView.setText(tab.getText());
            tabTextView.setTextColor(Color.parseColor("#ffffff"));
            tab.setCustomView(relativeLayout);
            tab.select();


        }


        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (GenericMethods.globaltabposition.equals("false")) {
                    GenericMethods.tabpos = tabLayout.getSelectedTabPosition();

                }
                super.onTabSelected(tab);
            }
        });



        viewPager.setCurrentItem(GenericMethods.tabpos, false);


    }








    private void setupViewPager(ViewPager viewPager) {


        db = new DBOperation(this);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this.getSupportFragmentManager());

        adapter.addFragment(new biometricappointments(), "Biometric");
        adapter.addFragment(new marriageappointments(), "Marriage");



        viewPager.setAdapter(adapter);


    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        finish();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected void onPostResume() {

        super.onPostResume();

        GenericMethods.pd_value = "true";

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pDialog != null)
            pDialog.dismiss();
        thisnext = null;

        GenericMethods.pd_value = "null";
    }


}