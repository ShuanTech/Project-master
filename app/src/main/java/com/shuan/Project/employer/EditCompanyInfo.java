package com.shuan.Project.employer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.shuan.Project.R;
import com.shuan.Project.Utils.Common;
import com.shuan.Project.list.Sample;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EditCompanyInfo extends AppCompatActivity {


    private Context mContext;
    private Toolbar toolbar;
    private Common mApp;
    private String u_id, level;

    private LinearLayout objective_layout;
    private ProgressBar progressBar;
    private ScrollView scroll;

    private HashMap<String, String> pData;

    private HashMap<String, String> rData;
    private ArrayList<Sample> list;
    private PopupMenu popupMenu;
    private ImageView objective_edit,contact_info_edit,company_detail_edit;
    private TextView comapnyname, website, companytype, industrytype, founded, emailid, phonenumber, contactperson, contactemail, contactphone, contacttime,num_wrkers, objective;


    Bundle contactData;
    Bundle companyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company_info);


        mApp = (Common) getApplicationContext();
        setTheme(R.style.AppBaseInfoEdit);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Update");


        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        scroll = (ScrollView) findViewById(R.id.scroll);
        website = (TextView) findViewById(R.id.web_site);
        companytype = (TextView) findViewById(R.id.company_type);
        industrytype = (TextView) findViewById(R.id.industry_type);
        founded = (TextView) findViewById(R.id.founded);
        emailid = (TextView) findViewById(R.id.email_id);
        phonenumber = (TextView) findViewById(R.id.phone_number);
        contactperson = (TextView) findViewById(R.id.contact_person);
        contactemail = (TextView) findViewById(R.id.contact_email);
        contactphone = (TextView) findViewById(R.id.contact_phone_number);
        num_wrkers = (TextView) findViewById(R.id.num_wrkers);
        contacttime = (TextView) findViewById(R.id.contact_time);
        objective = (TextView) findViewById(R.id.objective);
        comapnyname = (TextView) findViewById(R.id.company_name);
        objective.setMovementMethod(new ScrollingMovementMethod());

//        objective_edit = (ImageView) findViewById(R.id.objective_edit);
        contact_info_edit = (ImageView) findViewById(R.id.contact_info_edit);
        company_detail_edit = (ImageView) findViewById(R.id.company_detail_edit);

        contactData = new Bundle();
        companyData = new Bundle();

        contact_info_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EditCompanyInfo.this,EditContactDetails.class);
                intent.putExtras(contactData);
                startActivity(intent);
            }
        });

        company_detail_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditCompanyInfo.this,EditCompanyDetails.class);
                intent.putExtras(companyData);
                startActivity(intent);
            }
        });

        new profileView().execute();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public class profileView extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            pData = new HashMap<String, String>();
            pData.put("u_id", mApp.getPreference().getString(Common.u_id, ""));
            pData.put("level", mApp.getPreference().getString(Common.LEVEL, ""));


            try {

                JSONObject json = Connection.UrlConnection(php.profile, pData);

                int succ = json.getInt("success");

                if (succ == 0) {

                    Toast.makeText(getApplicationContext(), "No data..!!", Toast.LENGTH_SHORT).show();

                } else {


                    if (mApp.getPreference().getString(Common.LEVEL, "").equalsIgnoreCase("1")) {

                    } else if (mApp.getPreference().getString(Common.LEVEL, "").equalsIgnoreCase("2")) {

                    } else {


                        JSONArray jsonArray = json.getJSONArray("profile");
                        JSONObject child = jsonArray.getJSONObject(0);



                        JSONObject cnt = child.getJSONObject("cnt");
                        JSONArray cntArray = cnt.getJSONArray("cnt");
                        final JSONObject data1 = cntArray.getJSONObject(0);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                comapnyname.setText(data1.optString("cmpny_name"));
                                companyData.putString("comapnyname",data1.optString("cmpny_name"));

                                if (data1.optString("c_desc") != null && !data1.optString("c_desc").trim().isEmpty()) {
                                    companyData.putString("c_desc",data1.optString("c_desc"));
                                    objective.setText(data1.optString("c_desc"));
                                }

                                if (!data1.optString("c_type").equalsIgnoreCase("") || !data1.optString("i_type").equalsIgnoreCase("") ||
                                        !data1.optString("mail").equalsIgnoreCase("") || !data1.optString("ph_no").equalsIgnoreCase("") ||
                                        !data1.optString("c_website").equalsIgnoreCase("") || !data1.optString("year_of_establish").equalsIgnoreCase("")) {


                                    if (!data1.optString("c_type").equalsIgnoreCase("")) {
                                        companyData.putString("c_type",data1.optString("c_type"));
                                        companytype.setText(data1.optString("c_type"));
                                    }
                                    if (!data1.optString("i_type").equalsIgnoreCase("")) {
                                        companyData.putString("i_type",data1.optString("i_type"));
                                        industrytype.setText(data1.optString("i_type"));
                                    }
                                    if (!data1.optString("mail").equalsIgnoreCase("")) {
                                        companyData.putString("mail",data1.optString("mail"));
                                        emailid.setText(data1.optString("mail"));
                                    }
                                    if (!data1.optString("ph_no").equalsIgnoreCase("")) {
                                        companyData.putString("ph_no",data1.optString("ph_no"));
                                        phonenumber.setText(data1.optString("ph_no"));
                                    }
                                    if (!data1.optString("c_website").equalsIgnoreCase("")) {
                                        companyData.putString("website",data1.optString("c_website"));
                                        website.setText(data1.optString("c_website"));
                                    }

                                    if (!data1.optString("year_of_establish").equalsIgnoreCase("")) {
                                        companyData.putString("year_of_establish",data1.optString("year_of_establish"));
                                        founded.setText(data1.optString("year_of_establish"));
                                    }
                                    if (!data1.optString("num_wrkers").equalsIgnoreCase("")) {
                                        companyData.putString("num_wrkers",data1.optString("num_wrkers"));
                                        num_wrkers.setText(data1.optString("num_wrkers"));
                                    }
                                }

                                if (!data1.optString("contact_person").equalsIgnoreCase("") || !data1.optString("contact_mail").equalsIgnoreCase("") ||
                                        !data1.optString("contact_ph").equalsIgnoreCase("") || !data1.optString("contact_time").equalsIgnoreCase("")) {

                                    if (!data1.optString("contact_person").equalsIgnoreCase("")) {

                                        contactData.putString("contact_person",data1.optString("contact_person"));
                                        contactperson.setText(data1.optString("contact_person"));
                                    }
                                    if (!data1.optString("contact_mail").equalsIgnoreCase("")) {
                                        contactData.putString("contact_mail",data1.optString("contact_mail"));
                                        contactemail.setText(data1.optString("contact_mail"));
                                    }

                                    if (!data1.optString("contact_ph").equalsIgnoreCase("")) {
                                        contactData.putString("contact_ph",data1.optString("contact_ph"));
                                        contactphone.setText(data1.optString("contact_ph"));
                                    }

                                    if (!data1.optString("contact_time").equalsIgnoreCase("")) {
                                        contactData.putString("contact_time",data1.optString("contact_time"));
                                        contacttime.setText(data1.optString("contact_time"));
                                    }

                                }
                            }
                        });

                    }


                }


            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            scroll.setVisibility(View.VISIBLE);

        }
    }
}
