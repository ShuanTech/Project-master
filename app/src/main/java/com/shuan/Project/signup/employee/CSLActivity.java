package com.shuan.Project.signup.employee;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shuan.Project.R;
import com.shuan.Project.Utils.AlphabetValidator;
import com.shuan.Project.Utils.Common;
import com.shuan.Project.Utils.TextfieldValidator;
import com.shuan.Project.asyncTasks.CslIns;
import com.shuan.Project.asyncTasks.GetCollege;
import com.shuan.Project.asyncTasks.GetCourse;
import com.shuan.Project.asyncTasks.GetLocation;
import com.shuan.Project.asyncTasks.GetSkillSet;

public class CSLActivity extends AppCompatActivity {

    private Common mApp;
    private AutoCompleteTextView clgName, conCent, loc;
    private MultiAutoCompleteTextView skill;
    private String[] qulify = new String[]{"Select Highest Qualification", "PG", "UG", "DIPLOMA"};
    public String abc, abcd;
    public ScrollView scrollView;
    public ProgressBar progressBar;
    private String q;
    private Spinner level;
    private int ab = 1990;
    public EditText fullName, frm_yr, to_yr;
    private boolean ins = false;
    private boolean cIns = false;
    private Button next;
    private TextfieldValidator textfieldValidator;
    private AlphabetValidator alphabetValidator;
    private int i = 0, j = 0;
    private boolean exit = false;
    private String get;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csl);

        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        fullName = (EditText) findViewById(R.id.fullName);
        level = (Spinner) findViewById(R.id.level);
        clgName = (AutoCompleteTextView) findViewById(R.id.clg_name);
        conCent = (AutoCompleteTextView) findViewById(R.id.concent);
        frm_yr = (EditText) findViewById(R.id.frm_yr);
        to_yr = (EditText) findViewById(R.id.to_yr);
        skill = (MultiAutoCompleteTextView) findViewById(R.id.skill);
        loc = (AutoCompleteTextView) findViewById(R.id.location);
        textfieldValidator = new TextfieldValidator();
        alphabetValidator = new AlphabetValidator();
        next = (Button) findViewById(R.id.next);


        frm_yr.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //abc = frm_yr.getText().toString();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //i = Integer.parseInt(frm_yr.getText().toString());
                //i = Integer.parseInt(abc.toString());
                if (frm_yr.getText().toString().length() != 0){
                    i = Integer.parseInt(frm_yr.getText().toString());
                }

            }
        });


        to_yr.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //j = Integer.parseInt(to_yr.getText().toString());
                //j = Integer.parseInt(abcd.toString());
               if (to_yr.getText().toString().length() != 0){
                  j = Integer.parseInt(to_yr.getText().toString());
               }
            }

        });


        new GetCollege(CSLActivity.this, progressBar, scrollView, clgName).execute();
        new GetCourse(CSLActivity.this, conCent).execute();
        new GetSkillSet(CSLActivity.this, scrollView, progressBar, skill).execute();
        new GetLocation(CSLActivity.this, scrollView, loc, progressBar).execute();


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, qulify);
        level.setAdapter(adapter1);
        level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                get = level.getSelectedItem().toString();
                if (get.equalsIgnoreCase("PG")) {
                    q = "1";
                } else if (get.equalsIgnoreCase("UG")) {
                    q = "2";
                } else {
                    q = "3";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        clgName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt = (TextView) view.findViewById(R.id.ins_name);
                clgName.setText(txt.getText().toString());
                conCent.requestFocus();
                ins = true;

            }
        });

        conCent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt = (TextView) view.findViewById(R.id.display);
                conCent.setText(txt.getText().toString());
                cIns = true;
                frm_yr.requestFocus();
            }
        });

        loc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txt1 = (TextView) view.findViewById(R.id.ins_name);
                loc.setText(txt1.getText().toString());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.next:

                        if (fullName.getText().toString().length() == 0) {
                            fullName.setError("Field Mandatory");
                            fullName.requestFocus();
                        } else if (!alphabetValidator.validate(fullName.getText().toString())) {
                            fullName.setError("Enter a Valid Name");
                            fullName.requestFocus();
                        } else if (get.equalsIgnoreCase("Select Highest Qualification")) {
                            level.requestFocus();
                            Toast.makeText(getApplicationContext(), "select Qualification", Toast.LENGTH_SHORT).show();
                        } else if (clgName.getText().toString().length() == 0) {
                            clgName.setError("Field Mandatory");
                            clgName.requestFocus();
                        } else if (conCent.getText().toString().length() == 0) {
                            conCent.setError("Field Mandatory");
                            conCent.requestFocus();
                        } else if (frm_yr.getText().toString().length() == 0) {
                            frm_yr.setError("Field Mandatory");
                            frm_yr.requestFocus();
                        } else if (to_yr.getText().toString().length() == 0) {
                            to_yr.setError("Field Mandatory");
                            to_yr.requestFocus();
                        } else if (i > j) {
                            to_yr.setError("Passed out year less than join year");
                            to_yr.requestFocus();
                        } else if (i == j) {
                            to_yr.setError("Check the year entered");
                            to_yr.requestFocus();
                        } else if (i <= ab) {
                            frm_yr.setError("Enter a valid Year");
                            frm_yr.requestFocus();
                        } else if (j <= ab) {
                            to_yr.setError("Enter a valid Year");
                            to_yr.requestFocus();
                        } else if (skill.getText().toString().length() == 0) {
                            skill.setError("Field Mandatory");
                            skill.requestFocus();
                        } else if (!textfieldValidator.validate(skill.getText().toString())) {
                            skill.setError("Enter a Valid Skill");
                            skill.requestFocus();
                        } else if (loc.getText().toString().length() == 0) {
                            loc.setError("Field Mandatory");
                            loc.requestFocus();
                        } else if (!textfieldValidator.validate(loc.getText().toString())) {
                            loc.setError("Enter a Valid Location");
                        } else {
                            next.setEnabled(false);
                            new CslIns(CSLActivity.this, mApp.getPreference().getString(Common.u_id, ""), fullName.getText().toString(), q,
                                    clgName.getText().toString(), conCent.getText().toString(), frm_yr.getText().toString(), to_yr.getText().toString(),
                                    skill.getText().toString(), loc.getText().toString(), ins, cIns).execute();
                                    /*startActivity(new Intent(getApplicationContext(), MailVerify.class));
                            finish();*/
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            mApp.getPreference().edit().putBoolean(Common.PAGE1, false).commit();
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Press Back again to Cancel Signup Process.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2000);
        }
    }
}


