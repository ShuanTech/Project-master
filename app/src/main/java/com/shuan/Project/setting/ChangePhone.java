package com.shuan.Project.setting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shuan.Project.R;
import com.shuan.Project.Utils.Common;
import com.shuan.Project.Utils.PhoneNumberValidator;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;

import org.json.JSONObject;

import java.util.HashMap;

public class ChangePhone extends AppCompatActivity {

    private Toolbar toolbar;
    private Common mApp;
    private EditText new_phone, old_phone;
    private Button change;
    private PhoneNumberValidator phoneValue;
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
        setContentView(R.layout.activity_change_phone);

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
        toolbar.setTitle("Change Primary Phone");

        scroll = (LinearLayout) findViewById(R.id.scroll);
        new_phone = (EditText) findViewById(R.id.new_phone);
        old_phone = (EditText) findViewById(R.id.old_phone);

        phoneValue = new PhoneNumberValidator();


        change = (Button) findViewById(R.id.update);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (old_phone.getText().toString().trim().length() == 0) {
                    old_phone.setError("Enter Current Phone");
                    old_phone.requestFocus();
                } else if (!phoneValue.validate(old_phone.getText().toString())) {
                    old_phone.setError("Invalid Phone Number");
                    old_phone.requestFocus();
                } else if (new_phone.getText().toString().trim().length() == 0) {
                    new_phone.setError("Enter New Phone");
                    new_phone.requestFocus();
                } else if (!phoneValue.validate(new_phone.getText().toString())) {
                    new_phone.setError("Invalid Phone Number");
                    new_phone.requestFocus();
                } else if (new_phone.getText().toString().equalsIgnoreCase(old_phone.getText().toString())) {
                    new_phone.setError("Current Phone Number and New Phone Number cannot be same..!");
                    new_phone.requestFocus();
                } else {
                    new ChangePhone.UpdatePhone().execute();
                }
            }
        });
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

    public class UpdatePhone extends AsyncTask<String, String, String> {

        String cPhone = new_phone.getText().toString();
        String oPhone = old_phone.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChangePhone.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setMessage("Updating...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            cData = new HashMap<String, String>();
            cData.put("u_id", mApp.getPreference().getString(Common.u_id, ""));
            cData.put("newPhoneNo", cPhone);
            cData.put("old", oPhone);


            try {
                JSONObject json = Connection.UrlConnection(php.changePrimaryPhoneNo, cData);
                int succ = json.getInt("success");
                if (succ == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Phone Number Not Update...Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (succ == 3) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Old Phone Number does not match with records..!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (succ == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "You are using the same old Phone", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Primary Phone Number Successfully Updated", Toast.LENGTH_SHORT).show();
                            //mApp.getPreference().edit().putBoolean(Common.USRINFO, false).commit();
                            startActivity(new Intent(getApplicationContext(), SettingActivity.class));
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
    public void onBackPressed() {
        super.onBackPressed();
    }
}
