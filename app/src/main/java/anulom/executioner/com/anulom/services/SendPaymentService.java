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


import static anulom.executioner.com.anulom.database.DBManager.TableInfo.UPDATEPAYMENT1;
import static com.google.android.gms.internal.zzagz.runOnUiThread;


/**
 * Created by Admin on 7/12/2016.
 */
public class SendPaymentService extends Service {
    DBOperation db;

    public String umail = Login.umailid;
    GenericMethods mResponse;
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=UTF8");
    String username3, status = "1";

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

    public void onCreate() {


        // TODO Auto-generated method stub
        super.onCreate();


    }


    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {
        DBOperation db = new DBOperation(getApplicationContext());

        String token = "DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7";

        @Override
        protected Double doInBackground(String... params) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String username2 = sharedPreferences.getString("username", "");
            username3 = username2;

            ArrayList<HashMap<String, String>> paymentlist = db.getPaymentReport(db);

            for (int i = 0; i < paymentlist.size(); i++) {
                String docid = paymentlist.get(i).get("docid");
                String email = paymentlist.get(i).get("email");
                String date = paymentlist.get(i).get("date");
                String item1 = paymentlist.get(i).get("amount");
                String item2 = paymentlist.get(i).get("radiotype");
                String item3 = paymentlist.get(i).get("comment");

                postData(docid, email, date, item1, item2, token, item3);
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

        public void postData(String docid, String email, String date, String amount, String radiotype, String token, String comm) {

            DBOperation db = new DBOperation(getApplicationContext());


            String URL = GenericMethods.MAIN_URL + "/api/v2/biometric_data/update_payment";
            try {
                JSONObject app = new JSONObject();
                app.put("docid", docid);
                app.put("email", email);
                app.put("amount", amount);
                app.put("type", radiotype);
                app.put("time", date);
                app.put("comment", comm);

                JSONObject tokenjson = new JSONObject();
                tokenjson.put("token", token);

                JSONObject main = new JSONObject();
                main.put("document", app);
                main.put("auth_token", tokenjson);

                String json = "";
                json = main.toString();
                System.out.println("jsom:"+json);

                String strResponsePost = mResponse.doPostRequest(URL, json);


                if (!strResponsePost.equals("")) {
                    JSONObject jResult = new JSONObject(strResponsePost);
                    String strStatus = jResult.getString("status");
                    System.out.println("status:"+strStatus);
                    if (strStatus.equals(status)) {
                        SQLiteDatabase sqldb = db.getWritableDatabase();
                        int count = sqldb.delete(UPDATEPAYMENT1, DBManager.TableInfo.DOCID + "=?", new String[]{docid});
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(SendPaymentService.this, " Payment Inserted Successfully!! ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }if(strStatus.equals("0")){
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(SendPaymentService.this, "From Payment,Please Contact Support Team ", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    if (strStatus.equals("-1")) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(SendPaymentService.this, "From Payment,Contact support Team", Toast.LENGTH_SHORT).show();
                            }
                        });
//
                    }

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