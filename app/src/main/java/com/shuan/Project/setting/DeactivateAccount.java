package com.shuan.Project.setting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shuan.Project.R;
import com.shuan.Project.Utils.Common;
import com.shuan.Project.launcher.LoginActivity;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;

import org.json.JSONObject;

import java.util.HashMap;

public class DeactivateAccount extends AppCompatActivity {

    private Toolbar toolbar;
    private Common mApp;
    private Button change;
    private LinearLayout scroll;
    private HashMap<String, String> cData;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mApp = (Common) getApplicationContext();

        if (mApp.getPreference().getString(Common.LEVEL, "").equalsIgnoreCase("1")) {
            setTheme(R.style.Junior);
        } else if (mApp.getPreference().getString(Common.LEVEL, "").equalsIgnoreCase("2")) {
            setTheme(R.style.Senior);
        } else {
            setTheme(R.style.AppBaseTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivate_account);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        if (mApp.getPreference().getString(Common.LEVEL, "").equalsIgnoreCase("1")) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.junPrimary));
        } else if (mApp.getPreference().getString(Common.LEVEL, "").equalsIgnoreCase("2")) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.senPrimary));
        } else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Deactivate Account");

        scroll = (LinearLayout) findViewById(R.id.scroll);


        change = (Button) findViewById(R.id.update);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DeactivateAccount.this);
                alertDialogBuilder.setMessage("Are you sure You wanted to make decision ?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                new DeactivateAccount.Deactivate().execute();
//                                        Toast.makeText(DeactivateAccount.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                /*if (new_phone.getText().toString().trim().length() == 0) {
                    new_phone.setError("Enter New Phone");
                    new_phone.requestFocus();
                }  else if (!phoneValue.validate(new_phone.getText().toString())) {
                    new_phone.setError("Invalid Phone Number");
                    new_phone.requestFocus();
                }  else {
                    new ChangePhone.UpdatePhone().execute();
                }*/
            }
        });
    }


    public class Deactivate extends AsyncTask<String, String, String> {

//        String cPhone = new_phone.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DeactivateAccount.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setMessage("Deactivating...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            cData = new HashMap<String, String>();
            cData.put("u_id", mApp.getPreference().getString(Common.u_id, ""));
//            cData.put("new_phone_number", cPhone);


            try {
                JSONObject json = Connection.UrlConnection(php.deactivateAccount, cData);
                int succ = json.getInt("success");
                if (succ == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Some error occured..Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Deactivated Successfully", Toast.LENGTH_SHORT).show();
//                            mApp.getPreference().edit().putBoolean(Common.Login, false).commit();
//                            mApp.getPreference().edit().putBoolean(Common.USRINFO, false).commit();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }
                    });
                }
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.cancel();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
