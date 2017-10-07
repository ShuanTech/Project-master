package com.shuan.Project.signup;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shuan.Project.R;
import com.shuan.Project.Utils.Common;
import com.shuan.Project.Utils.EmailValidator;
import com.shuan.Project.Utils.Helper;
import com.shuan.Project.Utils.PasswordValidator;
import com.shuan.Project.Utils.PhoneNumberValidator;
import com.shuan.Project.Utils.TextfieldValidator;
import com.shuan.Project.Utils.UsernameValidator;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Helper helper = new Helper();
    private Common mApp;
    private EditText name, emailId, phNo, pass, confrmPass;
    private RadioGroup level;
    private RadioButton radio, r1, r2;
    private Button signUp;
    private String type;
    private HashMap<String, String> sData;
    private ProgressDialog pDialog;
    private AlertDialog.Builder builder;
    private String select;
    private EmailValidator emailValidator;
    private PasswordValidator pasval;
    private TextfieldValidator textfieldValidator;
    private PhoneNumberValidator phoneNumberValidator;
    private UsernameValidator usernameValidator;
    private CheckBox agree;
    private TextView term;
    private String app_server_url = "http://192.168.1.111/fcmtest/fcm_insert.php";
    private boolean agre = true;
    CheckBox mCbShowPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mApp = (Common) getApplicationContext();
        select = getIntent().getStringExtra("select");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /*builder = new AlertDialog.Builder(SignupActivity.this)
                .setCancelable(false)
                .setTitle("NOTE :")
                .setIcon(R.drawable.logo)
                .setMessage("Use Phone Number which is available for verification process");
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();*/

        name = (EditText) findViewById(R.id.name);
        emailId = (EditText) findViewById(R.id.email);
        phNo = (EditText) findViewById(R.id.phone);
        pass = (EditText) findViewById(R.id.pass);

        //mCbShowPwd = (CheckBox) findViewById(R.id.cbShowPwd);

        confrmPass = (EditText) findViewById(R.id.confrm_pass);
        level = (RadioGroup) findViewById(R.id.level);
        r1 = (RadioButton) findViewById(R.id.r1);
        r2 = (RadioButton) findViewById(R.id.r2);
        signUp = (Button) findViewById(R.id.sign_up);
        emailValidator = new EmailValidator();
        pasval = new PasswordValidator();
        textfieldValidator = new TextfieldValidator();
        usernameValidator = new UsernameValidator();
        phoneNumberValidator = new PhoneNumberValidator();
        agree = (CheckBox) findViewById(R.id.agree);

        term = (TextView) findViewById(R.id.term);

        /*mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // checkbox status is changed from uncheck to checked.
                if (!isChecked) {
                    // show password
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pass.setSelection(pass.length());
                } else {
                    // hide password
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pass.setSelection(pass.length());
                }
            }
        });*/

        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView myWebView = new WebView(SignupActivity.this);
                myWebView.loadUrl("file:///android_asset/privacy.html");
                myWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });

                new AlertDialog.Builder(SignupActivity.this).setView(myWebView)
                        .setTitle("Terms and Conditions")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                            }

                        }).show();

            }
        });


        if (select.equalsIgnoreCase("employer")) {
            level.setVisibility(View.GONE);
        }


        emailId.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        agree.setOnClickListener(this);

        signUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.agree:
                if (((CheckBox) v).isChecked()) {
                    agre = true;
                } else {
                    agre = false;
                }
                break;
            case R.id.sign_up:
                if (select.equalsIgnoreCase("employer")) {
                    type = "3";
                } else {
                    int getLevel = level.getCheckedRadioButtonId();
                    radio = (RadioButton) findViewById(getLevel);
                    if (radio.getText().toString().equalsIgnoreCase("fresher")) {
                        type = "1";
                    } else {
                        type = "2";
                    }
                }
                if (name.getText().toString().length() == 0) {
                    name.setError("Name Cannot Be empty");
                    name.requestFocus();
                } else if (!usernameValidator.validate(name.getText().toString())) {
                    name.setError("Enter a Valid Name(without spaces and special characters)");
                    name.requestFocus();
                } else if (name.getText().toString().length() < 6) {
                    name.setError("User Name must contain 6 characters");
                    name.requestFocus();
                } else if (emailId.getText().toString().length() == 0) {
                    emailId.setError("Email Id Cannot Be empty");
                    emailId.requestFocus();
                } else if (!emailValidator.validate(emailId.getText().toString())) {
                    emailId.setError("Invalid Email Id");
                } else if (phNo.getText().toString().length() == 0) {
                    phNo.setError("Phone Number Cannot Be empty");
                    phNo.requestFocus();
                } else if (!phoneNumberValidator.validate(phNo.getText().toString())) {
                    phNo.setError("Enter a valid Phone Number");
                    phNo.requestFocus();
                } else if (phNo.getText().length() < 10) {
                    phNo.setError("Invalid Phone Number");
                    phNo.requestFocus();
                } else if (pass.getText().toString().length() == 0) {
                    pass.setError("Password Cannot Be empty");
                    pass.requestFocus();
                } else if (!pasval.validate(pass.getText().toString())) {
                    pass.setError("Password must contain an Alphabet and a Number");
                    pass.requestFocus();
                } else if (pass.getText().toString().length() < 8) {
                    pass.setError("Password must contain at least 8 Characters");
                    pass.requestFocus();
                } else if (confrmPass.getText().toString().length() == 0) {
                    confrmPass.setError("Confirm Password Cannot Be empty");
                } else if (!pass.getText().toString().equals(confrmPass.getText().toString())) {
                    confrmPass.setError("Password Miss match");
                    confrmPass.requestFocus();
                    //
                } else if (agre) {
                    signUp.setEnabled(false);

                    new Signup().execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Agree the Terms and Conditions.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public class Signup extends AsyncTask<String, String, String> {

        String uname = name.getText().toString();
        String umail = emailId.getText().toString();
        String uph = phNo.getText().toString();
        String upass = pass.getText().toString();
        String ucpass = confrmPass.getText().toString();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignupActivity.this);
            pDialog.setMessage("Signing up!...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            sData = new HashMap<String, String>();
            sData.put("name", uname);
            sData.put("email", umail);
            sData.put("phno", uph);
            sData.put("pass", upass);
            sData.put("cnfrmpass", ucpass);
            sData.put("level", type);

            try {
                JSONObject json = Connection.UrlConnection(php.signup, sData);

                int succ = json.getInt("success");

                if (succ == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Error...Try Again!", Toast.LENGTH_SHORT).show();
                            signUp.setEnabled(true);

                        }
                    });
                } else if (succ == 2) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            emailId.setError("Email Already Exist");
                            emailId.requestFocus();
                            signUp.setEnabled(true);
                        }
                    });

                } else if (succ == 3) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            phNo.setError("Phone Already Exist");
                            phNo.requestFocus();
                            signUp.setEnabled(true);
                        }
                    });
                } else {
                    JSONArray jsonArray = json.getJSONArray("user");
                    JSONObject child = jsonArray.getJSONObject(0);

                    final String u_id = child.optString("u_id");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Log.d("uId", u_id);
                            mApp.getPreference().edit().putString(Common.u_id, u_id).commit();
                            mApp.getPreference().edit().putString(Common.LEVEL, type).commit();
                            mApp.getPreference().edit().putBoolean(Common.Login, true).commit();
                            mApp.getPreference().edit().putBoolean(Common.USRINFO, true).commit();
                            if (type.toString().equalsIgnoreCase("1")) {

                                mApp.getPreference().edit().putInt(Common.PROFILESTRENGTH, 35).commit();
                                //startActivity(new Intent(getApplicationContext(), CSLActivity.class));
                                //new Push();
                                startActivity(new Intent(getApplicationContext(), MailVerify.class));
                                finish();
                            } else if (type.toString().equalsIgnoreCase("2")) {

                                mApp.getPreference().edit().putInt(Common.PROFILESTRENGTH, 35).commit();
                                //startActivity(new Intent(getApplicationContext(), WorkActivity.class));
                                startActivity(new Intent(getApplicationContext(), MailVerify.class));
                                finish();
                            } else {
                                startActivity(new Intent(getApplicationContext(), MailVerify.class));
                                //startActivity(new Intent(getApplicationContext(), CompanyDetails.class));
                                finish();
                            }
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
            //new Push();
            pDialog.cancel();

        }

        /*private class Push {

            RequestQueue queue = MySingleTon.getInstance(this).getRequestQueue();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
            final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), "");
            StringRequest stringRequest = new StringRequest(Request.Method.POST, app_server_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("fcm_token", token);

                    Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
                    return params;
                }
            };
           // MySingleTon.getInstance(this).addToRequestQueue(stringRequest);

        }*/
    }
}
