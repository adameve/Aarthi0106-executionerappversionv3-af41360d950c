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

import static anulom.executioner.com.anulom.database.DBManager.TableInfo.TABLE_PENDINGWITNESS;
import static anulom.executioner.com.anulom.database.DBManager.TableInfo.TABLE_REASSIGN_WITNESS;
import static com.google.android.gms.internal.zzagz.runOnUiThread;


public class postreassignwitness extends Service {
    DBOperation db;

    public String umail = Login.umailid;
    GenericMethods mResponse;
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=UTF8");
    String username3, status = "1", docid, new_witness, old_witness, execemail;


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

            ArrayList<HashMap<String, String>> postreassignwitnesslist = db.postreassignwitness(db);


            try {


                for (int i = 0; i < postreassignwitnesslist.size(); i++) {



                    execemail = postreassignwitnesslist.get(i).get("execemail");
                    docid = postreassignwitnesslist.get(i).get("reass_doc");
                    new_witness = postreassignwitnesslist.get(i).get("reass_new");
                    old_witness = postreassignwitnesslist.get(i).get("reass_old");




                    // TODO Auto-generated catch block



                    postData(docid,old_witness,new_witness);





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

        public void postData(String docid,String oldwitness,String newwitness) {

            DBOperation db = new DBOperation(getApplicationContext());
            String token = "DOtUBMhv5pk51tl0D37uBcezq85cXNN7hZQ7";
            String URL =GenericMethods.MAIN_URL+"/api/v3/appointment_data/reassign_witness";


            try {

                JSONObject app1 = new JSONObject();
                app1.put("document_id", docid);
                app1.put("old_witness", oldwitness);
                app1.put("new_witness", newwitness);
                JSONObject tokenjson = new JSONObject();
                tokenjson.put("token", token);
                JSONObject main = new JSONObject();
                main.put("witness",app1);
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

                        int count = sqldb.delete(TABLE_REASSIGN_WITNESS, DBManager.TableInfo.reassign_docid + "=?", new String[]{docid});

                        System.out.println("Deleted:"+count);

                        int count1 = sqldb.delete(TABLE_PENDINGWITNESS, DBManager.TableInfo.db_PW_doc_id + "=?", new String[]{docid});
                        System.out.println("Deleted:"+count1);

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(postreassignwitness.this, "Witness Reassigned Successfully!!", Toast.LENGTH_LONG).show();
                            }
                        });


                    }if(strStatus.equals("0")){
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(postreassignwitness.this, "From Reassign Witness,Please Contact Support Team ", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    if (strStatus.equals("-1")) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(postreassignwitness.this, "From Reassign Witness,Contact support Team", Toast.LENGTH_LONG).show();
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
