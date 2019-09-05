package anulom.executioner.com3.anulom.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.util.ArrayList;

import java.util.HashMap;

import anulom.executioner.com3.anulom.R;
import anulom.executioner.com3.anulom.database.DBOperation;


public class notaskdetails extends Fragment {
    ArrayList<HashMap<String, String>> getAlldataList = null;
    String curdate1, value1 = "Adhoc";
    ListView tasklist1;
    DBOperation db;
    ArrayList<String> reportkey = new ArrayList<>();
    ArrayList<String> documentidpay = new ArrayList<>();
    ArrayList<String> documentidcomment = new ArrayList<>();
    ArrayAdapter<String> adpt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.appointment1, container, false);

        return rootView;

    }


}