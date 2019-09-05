package anulom.executioner.com3.anulom.fragment;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;

import android.graphics.Color;

import android.location.LocationManager;

import android.os.Bundle;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import anulom.executioner.com3.anulom.R;

import anulom.executioner.com3.anulom.database.DBOperation;




public class taskfragment extends Fragment {

    public static TodayDetails thisToday = null;
    String year, month, day, hour, min, id, owner, tenant, w1, w2, rkey, timevalue, timevalue1 = "", not_id, from = "Today", biocontent = "", content = "", o_content = "", t_content = "", w1_content = "", w2_content = "",
            o_ = "", t_ = "", w1_ = "", w2_ = "", Date, content1 = "Today", O_ = "", T_ = "", W_ = "", POA_ = "", AW_ = "", news = "Today's Appointments", A, nodetails1 = "No Today Appointments!!!";

    int year1, month1, day1, hour1, h2, h3, min1, id1, count = 0;

    ArrayList<HashMap<String, String>> getAlldataList = new ArrayList<>();
    ArrayList<HashMap<String, String>> getAlldataList1 = null;
    ArrayList<HashMap<String, String>> getCommentDatalist = null;
    String curdate1, appid, time1, docid1, currentcall, ID1;
    ListView tasklist1;
    DBOperation db;
    TextView comment;
    private Calendar mcurrenttime;
    Timer repeatTask = new Timer();
    long repeatInterval = 10800000;
    long startdelay = 3600000;
    private SharedPreferences usermail;
    LocationManager locationManager;
    String provider;
    private String username2 = "", password2 = "";
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    ArrayList<HashMap<String, String>> paymentlist = null;
    ArrayList<String> reportkey = new ArrayList<>();
    ListView tasklist;
    ArrayList<String> documentidpay = new ArrayList<>();
    ArrayList<String> documentidcomment = new ArrayList<>();
    ArrayAdapter<String> adpt;
    ArrayAdapter<String> adpt1;
    double currentLat;
    double currentLong;
    Button time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = new DBOperation(getContext());

        View view = inflater.inflate(R.layout.salesdetails, container, false);

        ViewPager viewPager = view.findViewById(R.id.frag2_viewpager);
        setupViewPager(viewPager);

        TabLayout tabs = view.findViewById(R.id.frag2_tabs);
        tabs.setupWithViewPager(viewPager);
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        curdate1 = df2.format(c.getTime());

        tasklist = view.findViewById(R.id.tasklist);
        for (int i = 0; i < tabs.getTabCount(); i++) {

            TabLayout.Tab tab = tabs.getTabAt(i);
            RelativeLayout relativeLayout = (RelativeLayout)
                    LayoutInflater.from(getContext()).inflate(R.layout.tab_layout, tabs, false);

            TextView tabTextView = relativeLayout.findViewById(R.id.tab_title);
            tabTextView.setText(tab.getText());
            tabTextView.setTextColor(Color.parseColor("#3b5998"));
            tab.setCustomView(relativeLayout);
            tab.select();


        }
        viewPager.setCurrentItem(0, false);
        return view;


    }

    private void setupViewPager(ViewPager viewPager) {

        Adapter adapter = new Adapter(getChildFragmentManager());

        if (db.getadhocdetails(db).size() > 0) {
            adapter.addFragment(new adhocdetails(), "MyTask" + "(" + db.getadhocdetails(db).size() + ")");

        } else if (db.getadhocdetails(db).size() == 0) {
            adapter.addFragment(new notaskdetails(), "MyTask" + "(" + db.getadhocdetails(db).size() + ")");


        }
        if (db.getWitnessdetails(db).size() > 0) {
            adapter.addFragment(new witnesspending(), "Witness" + "(" + db.getpendingwitness(db).size() + ")");
        } else if (db.getWitnessdetails(db).size() == 0) {
            adapter.addFragment(new notaskdetails(), "Witness" + "(" + db.getpendingwitness(db).size() + ")");

        }
        if (db.getComplaintdetails(db).size() > 0) {
            adapter.addFragment(new Complaintdetails(), "Complaint" + "(" + db.getComplaintdetails(db).size() + ")");
        } else if (db.getComplaintdetails(db).size() == 0) {
            adapter.addFragment(new notaskdetails(), "Complaint" + "(" + db.getComplaintdetails(db).size() + ")");

        }

        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public Adapter(FragmentManager childFragmentManager) {
            super(childFragmentManager);
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


}
