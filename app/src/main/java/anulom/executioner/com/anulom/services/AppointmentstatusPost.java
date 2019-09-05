package anulom.executioner.com.anulom.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;

import android.widget.Toast;

import androidx.annotation.Nullable;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import anulom.executioner.com.anulom.GenericMethods;
import anulom.executioner.com.anulom.Login;
import anulom.executioner.com.anulom.database.DBManager;
import anulom.executioner.com.anulom.database.DBOperation;
import anulom.executioner.com.anulom.fragment.CompletedDetails;
import anulom.executioner.com.anulom.fragment.NewDetails;
import anulom.executioner.com.anulom.fragment.OlderDetails;
import anulom.executioner.com.anulom.fragment.TodayDetails;
import anulom.executioner.com.anulom.fragment.marriagecompleted;
import anulom.executioner.com.anulom.fragment.marriagenew;
import anulom.executioner.com.anulom.fragment.marriageolder;
import anulom.executioner.com.anulom.fragment.marriagetoday;

import static com.google.android.gms.internal.zzagz.runOnUiThread;


public class AppointmentstatusPost extends Service {
    DBOperation db;

    public String umail = Login.umailid;
    GenericMethods mResponse;
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=UTF8");
    String username3, status1 = "1";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    public int onStartCommand(Intent intent, int flags, int startId) {

        mResponse = new GenericMethods();

        if (GenericMethods.isConnected(getApplicationContext())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new MyAsyncTask()
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            } else {
                new MyAsyncTask().execute();
            }
        }
        return START_STICKY;


    }




    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        DBOperation db = new DBOperation(getApplicationContext());

        String token = "DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7";

        @Override
        protected Double doInBackground(String... params) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String username2 = sharedPreferences.getString("username", "");
            username3 = username2;

            ArrayList<HashMap<String, String>> multipartychecklist = db.getmultipartycheck(db);
            for (int i = 0; i < multipartychecklist.size(); i++) {
                String exec_email = multipartychecklist.get(i).get("exec_email");
                String docid = multipartychecklist.get(i).get("docid");
                String appointmentid = multipartychecklist.get(i).get("appointment_id");
                String status = multipartychecklist.get(i).get("status");
                String reason = multipartychecklist.get(i).get("reason");
                String reach_time = multipartychecklist.get(i).get("reach_time");
                String location = multipartychecklist.get(i).get("location");
                String update_time = multipartychecklist.get(i).get("update_time");
                postData( exec_email, docid, reach_time, location, update_time, appointmentid, status , reason);


            }


            return null;
        }

        protected void onPostExecute(Double result) {

            stopSelf();

            if (TodayDetails.thisToday != null) {
                TodayDetails.thisToday.reFreshReload();
            }
            if (OlderDetails.thisOlderDetails != null) {
                OlderDetails.thisOlderDetails.reFreshReload();
            }
            if (NewDetails.thisnewDetails != null) {
                NewDetails.thisnewDetails.reFreshReload();
            }
            if (CompletedDetails.thiscompleteDetails != null) {
                CompletedDetails.thiscompleteDetails.reFreshReload();
            }

            if (marriagetoday.thisToday != null) {
                marriagetoday.thisToday .reFreshReload();
            }
            if (marriageolder.thisToday  != null) {
                marriageolder.thisToday .reFreshReload();
            }
            if (marriagenew.thisToday != null) {
                marriagenew.thisToday .reFreshReload();
            }
            if (marriagecompleted.thisToday != null) {
                marriagecompleted.thisToday.reFreshReload();
            }

        }

        protected void onProgressUpdate(Integer... progress) {

        }

        public void postData(String exec_email, String docid, String reachtime, String location, String updatetime, String appid,String poststatus ,String reason) {

            DBOperation db = new DBOperation(getApplicationContext());


            String URL = GenericMethods.MAIN_URL+"/api/v3/appointment_data/post_status_update";
            try {
                JSONObject app = new JSONObject();
                app.put("exec_email", exec_email);
                app.put("docid", docid);
                app.put("reach_time", reachtime);
                app.put("update_location", location);
                app.put("update_time", updatetime);
                app.put("appointment_id", appid);
                app.put("post_status", poststatus);
                app.put("reason", reason);
                JSONObject tokenjson = new JSONObject();
                tokenjson.put("token", token);

                JSONObject main = new JSONObject();
                main.put("appointment", app);
                main.put("auth_token", tokenjson);

                String json = "";
                json = main.toString();

                System.out.println("json"+json);
                String strResponsePost = mResponse.doPostRequest(URL, json);


                if (!strResponsePost.equals("")) {
                    JSONObject jResult = new JSONObject(strResponsePost);
                    String strStatus = jResult.getString("status");
                      System.out.println("response"+strResponsePost);
                    System.out.println("status"+strStatus);
                    if (strStatus.equals("1")) {

                        SQLiteDatabase sqldb = db.getWritableDatabase();
                        int count = sqldb.delete(DBManager.TableInfo.MULTIWORK, null, null);
                        System.out.println("deleted");
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(AppointmentstatusPost.this, "Appointment Data Updated  Successfully  ", Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                    if(strStatus.equals("0")){
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(AppointmentstatusPost.this, " From appointment status,Please Contact Support Team ", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    if (strStatus.equals("-1")) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(AppointmentstatusPost.this, "From appointment status,Contact support Team", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Network Problem.", Toast.LENGTH_SHORT).show();
                }




            } catch (ClientProtocolException e) {

                // TODO Auto-generated catch block
            } catch (IOException e) {

                // TODO Auto-generated catch block
            } catch (JSONException e) {

                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }
}