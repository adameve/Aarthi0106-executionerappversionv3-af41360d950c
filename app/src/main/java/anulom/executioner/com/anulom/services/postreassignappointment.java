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

import static anulom.executioner.com.anulom.database.DBManager.TableInfo.TABLE_NAME_APPOINTMENT;
import static anulom.executioner.com.anulom.database.DBManager.TableInfo.TABLE_REASSIGN_APPOINTMENTS;
import static com.google.android.gms.internal.zzagz.runOnUiThread;


public class postreassignappointment extends Service {
    DBOperation db;

    public String umail = Login.umailid;
    GenericMethods mResponse;
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=UTF8");
    String username3, status = "1", docid, new_witness, old_witness, execemail,appid,reason;


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


        @Override
        protected Double doInBackground(String... params) {

            DBOperation db = new DBOperation(getApplicationContext());
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String username2 = sharedPreferences.getString("username", "");
            username3 = username2;

            ArrayList<HashMap<String, String>> postreassignappointmentlist = db.postreassignappointments(db);


            try {


                for (int i = 0; i < postreassignappointmentlist.size(); i++) {



                    execemail = postreassignappointmentlist.get(i).get("execemail");
                    docid = postreassignappointmentlist.get(i).get("re_docid");
                    new_witness = postreassignappointmentlist.get(i).get("re_new_executioner");
                    old_witness = postreassignappointmentlist.get(i).get("re_cu_executioner");
                    appid=postreassignappointmentlist.get(i).get("re_appid");
                    reason=postreassignappointmentlist.get(i).get("re_reason");



                    // TODO Auto-generated catch block



                    postData(docid,appid,old_witness,new_witness,reason);





                }




            } catch (Exception e) {

                // TODO Auto-generated catch block


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

        }

        protected void onProgressUpdate(Integer... progress) {

        }

        public void postData(String docid,String appid,String oldwitness,String newwitness,String reason) {

            DBOperation db = new DBOperation(getApplicationContext());
            String token = "DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7";
            String URL =GenericMethods.MAIN_URL+"/api/v3/appointment_data/reassign_appointment";


            try {



                JSONObject app1 = new JSONObject();
                app1.put("document_id", docid);
                app1.put("app_id", appid);
                app1.put("cur_executioner", oldwitness);
                app1.put("new_executioner", newwitness);
                app1.put("reason", reason);
                JSONObject tokenjson = new JSONObject();
                tokenjson.put("token", token);
                JSONObject main = new JSONObject();
                main.put("appointment",app1);
                main.put("auth_token", tokenjson);
                String json = "";
                json = main.toString();

                System.out.println("json"+main);
                String strResponsePost = mResponse.doPostRequest(URL, json);

                if (!strResponsePost.equals("")) {
                    JSONObject jResult = new JSONObject(strResponsePost);
                    String strStatus = jResult.getString("status");

                    System.out.println("status"+strStatus);
                    if (strStatus.equals("1")) {


                        SQLiteDatabase sqldb = db.getWritableDatabase();

                        int count = sqldb.delete(TABLE_REASSIGN_APPOINTMENTS, DBManager.TableInfo.rea_docid + "=?", new String[]{docid});

                        System.out.println("Deleted:"+count);

                        int count1 = sqldb.delete(TABLE_NAME_APPOINTMENT, DBManager.TableInfo.rea_appid + "=?", new String[]{appid});

                        System.out.println("Deleted:"+count1);

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(postreassignappointment.this, "Appointments Reassigned Successfully!!", Toast.LENGTH_LONG).show();
                            }
                        });


                    }if(strStatus.equals("0")){
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(postreassignappointment.this, "From Reassign Appointments,Please Contact Support Team ", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    if (strStatus.equals("-1")) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(postreassignappointment.this, "From Reassign Appointments,Contact support Team", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Network Problem.", Toast.LENGTH_SHORT).show();
                }



            } catch (JSONException e) {

                // TODO Auto-generated catch block


            } catch (ClientProtocolException e) {

                // TODO Auto-generated catch block
            } catch (IOException e) {

            }
        }


    }


}
