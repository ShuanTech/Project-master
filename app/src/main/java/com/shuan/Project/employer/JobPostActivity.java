package com.shuan.Project.employer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shuan.Project.R;
import com.shuan.Project.Utils.Common;
import com.shuan.Project.asyncTasks.GetCourse;
import com.shuan.Project.asyncTasks.GetLocation;
import com.shuan.Project.asyncTasks.GetSkillSet;
import com.shuan.Project.asyncTasks.PostJob;
import com.shuan.Project.asyncTasks.UpdateJob;
import com.shuan.Project.list.Sample;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JobPostActivity extends AppCompatActivity implements View.OnClickListener {

    private Common mApp;
    private EditText title, salary, descr, vacancy;
    private MultiAutoCompleteTextView skill;
    private AutoCompleteTextView qual, location;
    private LinearLayout jobLay, descLay, mLayout;
    private Button cancel, save, post, addloc;
    private Spinner job_level, job_cat;
    private String j, category;
    private RadioGroup job_radio;
    private RadioButton job, part, full, contr;
    private String[] exprnc = new String[]{"Select a Job level", "Fresher", "1-2 years", "2-3 years", "3-4 years", "4-5 years", "5-6 years", "6-7 years", "7-8 years", "8-9 years", "9-10 years", "above 10 years"};
    private String[] jobcat = new String[]{"Select an Industry Type", "Airline", "Banking/FinancialServices/Broking", "IT- Software Services", "IT- Hardware/ Networking", "Medical/ Healthcare/ Hospitality", "Education/Teaching/Training", "Education/Teaching/Training", "Engineering/ Construction", "Real Estate/ Property", "Travel/ Hotels/ Restaurants/ Tourism", "ITES- BPO/ Call center", "Advertising/PR/MR/Event Management", "Textiles/ Garments/ Accessories", "Media/ Entertainment", "Insurance", "Accounting/ Taxation", "Law/ Legal Firms", "Gems/ Jewellery"};
    private ArrayList<Sample> list;
    private ScrollView scroll;
    private ProgressBar progressBar;
    private HashMap<String, String> jData;
    private ArrayAdapter<String> adapter1, adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mApp = (Common) getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post);

        jobLay = (LinearLayout) findViewById(R.id.job_lay);
        descLay = (LinearLayout) findViewById(R.id.desc_lay);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        scroll = (ScrollView) findViewById(R.id.scroll);

        list = new ArrayList<Sample>();

        job_level = (Spinner) findViewById(R.id.job_level);
        job_radio = (RadioGroup) findViewById(R.id.job_radio);
        job_cat = (Spinner) findViewById(R.id.job_cat);
        part = (RadioButton) findViewById(R.id.part);
        full = (RadioButton) findViewById(R.id.full);
        contr = (RadioButton) findViewById(R.id.part);

        title = (EditText) findViewById(R.id.title);
        skill = (MultiAutoCompleteTextView) findViewById(R.id.skill);
        salary = (EditText) findViewById(R.id.salary);
        vacancy = (EditText) findViewById(R.id.no_ofvc);
        location = (AutoCompleteTextView) findViewById(R.id.location);
        descr = (EditText) findViewById(R.id.descr);
        qual = (AutoCompleteTextView) findViewById(R.id.qualify);

        cancel = (Button) findViewById(R.id.cancel);
        save = (Button) findViewById(R.id.save);
        post = (Button) findViewById(R.id.post);

        if (mApp.getPreference().getBoolean("jEdit", false) == true) {
            mApp.getPreference().edit().putBoolean("jEdit", false).commit();
            new GetJob().execute();
            post.setText("Update");
        }


        adapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, exprnc);
        adapter2 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, jobcat);

        job_level.setAdapter(adapter1);
        job_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                j = job_level.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        new GetSkillSet(JobPostActivity.this, scroll, progressBar, skill).execute();
        new GetLocation(getApplicationContext(), scroll, location, progressBar).execute();
        new GetCourse(getApplicationContext(), qual).execute();

        location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt1 = (TextView) view.findViewById(R.id.ins_name);
                location.setText(txt1.getText().toString());
            }
        });
        job_cat.setAdapter(adapter2);
        job_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = job_cat.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        post.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                startActivity(new Intent(getApplicationContext(), EmployerActivity.class));
                finish();
                break;
            case R.id.save:
                int getjob_radio = job_radio.getCheckedRadioButtonId();
                job = (RadioButton) findViewById(getjob_radio);
                if (title.getText().toString().length() == 0) {
                    title.setError("Job Title Mandatory");
                    title.requestFocus();
                } else if (skill.getText().length() == 0) {
                    skill.setError("Job Skill Mandatory");
                    skill.requestFocus();
                } else if (qual.getText().toString().length() == 0) {
                    qual.setError("Qualification is Mandatory");
                    qual.requestFocus();
                } else if (category.equalsIgnoreCase("Select an Industry Type")){
                    Toast.makeText(getApplicationContext(), "Select an Industry Type", Toast.LENGTH_SHORT).show();
                } else if (location.getText().toString().length() == 0) {
                    location.setError("Job Location Mandatory");
                    location.requestFocus();
                } else if (j.equalsIgnoreCase("Job level")) {
                    Toast.makeText(getApplicationContext(), "Select a Job level", Toast.LENGTH_SHORT).show();
                } else {
                    jobLay.setVisibility(View.GONE);
                    descLay.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.post:
                if (descr.getText().toString().length() == 0) {
                    descr.setError("Job Description Mandatory");
                    descr.requestFocus();
                } else {
                    // Toast.makeText(getApplicationContext(),job.getText().toString(), Toast.LENGTH_SHORT).show();
                    post.setEnabled(false);
                    if (post.getText().toString().equalsIgnoreCase("Update")) {
                        new UpdateJob(JobPostActivity.this, mApp.getPreference().getString("jId", ""),
                                title.getText().toString(), skill.getText().toString(), job.getText().toString(),
                                category.toString(), salary.getText().toString(), location.getText().toString(),
                                j.toString(), qual.getText().toString(), descr.getText().toString(), vacancy.getText().toString(), post).execute();
                    } else {
                        new PostJob(JobPostActivity.this, mApp.getPreference().getString(Common.u_id, ""),
                                title.getText().toString(), skill.getText().toString(), job.getText().toString(),
                                category.toString(), salary.getText().toString(), location.getText().toString(),
                                j.toString(), qual.getText().toString(), descr.getText().toString(), vacancy.getText().toString(), post).execute();
                    }

                }
                break;
        }
    }

    public class GetJob extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            jData = new HashMap<String, String>();
            jData.put("jId", mApp.getPreference().getString("jId", ""));
            try {

                JSONObject json = Connection.UrlConnection(php.getJob, jData);
                int succ = json.getInt("success");

                if (succ == 0) {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong! Try Again", Toast.LENGTH_SHORT).show();
                } else {
                    JSONArray jsonArray = json.getJSONArray("job");
                    final JSONObject child = jsonArray.getJSONObject(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            title.setText(child.optString("title"));
                            skill.setText(child.optString("skill"));
                            if (child.optString("type").equalsIgnoreCase("full time")) {
                                full.setChecked(true);
                            } else if (child.optString("type").equalsIgnoreCase("Part time")) {
                                part.setChecked(true);
                            } else {
                                contr.setChecked(true);
                            }
                            salary.setText("package");
                            location.setText(child.optString("location"));
                            int spinPos = adapter1.getPosition(child.optString("type"));
                            job_level.setSelection(spinPos);
                            int spinpos1 = adapter2.getPosition(child.optString("type"));
                            job_cat.setSelection(spinpos1);
                            qual.setText(child.optString("qual"));
                            descr.setText(child.optString("description"));
                            vacancy.setText("vacancy");
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
            progressBar.setVisibility(View.GONE);
            scroll.setVisibility(View.VISIBLE);
        }
    }

}
