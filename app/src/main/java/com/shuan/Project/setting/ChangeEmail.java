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
import com.shuan.Project.Utils.EmailValidator;
import com.shuan.Project.launcher.LoginActivity;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;

import org.json.JSONObject;

import java.util.HashMap;

public class ChangeEmail extends AppCompatActivity {

    private Toolbar toolbar;
    private Common mApp;
    private EditText new_email;
    private Button change;
    private EmailValidator emailValue;
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
        setContentView(R.layout.activity_change_email);

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
        toolbar.setTitle("Change Primary Email");

        scroll = (LinearLayout) findViewById(R.id.scroll);
        new_email = (EditText) findViewById(R.id.new_email);

        emailValue = new EmailValidator();


        change = (Button) findViewById(R.id.update);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new_email.getText().toString().trim().length() == 0) {
                    new_email.setError("Enter New Email");
                    new_email.requestFocus();
                }  else if (!emailValue.validate(new_email.getText().toString())) {
                    new_email.setError("Invalid Email Address");
                    new_email.requestFocus();
                }  else {
                    new ChangeEmail.UpdateEmail().execute();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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


    public class UpdateEmail extends AsyncTask<String, String, String> {

        String cEmail = new_email.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChangeEmail.this);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setMessage("Updating...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            cData = new HashMap<String, String>();
            cData.put("u_id", mApp.getPreference().getString(Common.u_id, ""));
            cData.put("newEmail", cEmail);


            try {
                JSONObject json = Connection.UrlConnection(php.changePrimaryEmail, cData);
                int succ = json.getInt("success");
                if (succ == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Email Not Update...Try Again!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } /*else if(succ == 2){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Old password doesnot match with records..!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }*/else if(succ==1){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "You are using the same old Email", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Confirmation message has been sent to your new email.", Toast.LENGTH_SHORT).show();
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
}
