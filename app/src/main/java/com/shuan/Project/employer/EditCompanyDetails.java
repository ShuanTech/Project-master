package com.shuan.Project.employer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.shuan.Project.R;
import com.shuan.Project.Utils.Common;
import com.shuan.Project.Utils.Helper;
import com.shuan.Project.asyncTasks.AboutCompany;

public class EditCompanyDetails extends AppCompatActivity {


    private Helper helper = new Helper();
    private Common mApp;

    private Spinner cmpnyType, indusType;
    private String[] cType;
    private String[] iType;
    private String c, i;

    private Button submit;

    private ProgressBar progressBar;
    private ScrollView scroll;

    private EditText c_des, yr_st, website, no_wrkr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mApp = (Common) getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company_details);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        scroll = (ScrollView) findViewById(R.id.scroll);
        cmpnyType = (Spinner) findViewById(R.id.cmpny_type);
        indusType = (Spinner) findViewById(R.id.indus_type);
        cType = getResources().getStringArray(R.array.cmpny_type);
        iType = getResources().getStringArray(R.array.industry_type);

        c_des = (EditText) findViewById(R.id.c_des);
        yr_st = (EditText) findViewById(R.id.yr_st);
        website = (EditText) findViewById(R.id.website);
        no_wrkr = (EditText) findViewById(R.id.no_wrkr);
        submit = (Button) findViewById(R.id.submit);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, cType);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, iType);


        c_des.setText(getIntent().getStringExtra("c_desc"));
        yr_st.setText(getIntent().getStringExtra("year_of_establish"));
        website.setText(getIntent().getStringExtra("website"));
        no_wrkr.setText(getIntent().getStringExtra("num_wrkers"));
        cmpnyType.setAdapter(adapter);
        indusType.setAdapter(adapter1);


        cmpnyType.setSelection(adapter.getPosition(getIntent().getStringExtra("c_type")));

        indusType.setSelection(adapter1.getPosition(getIntent().getStringExtra("i_type")));

        cmpnyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                c = cmpnyType.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        indusType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i = indusType.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c_des.getText().toString().length() == 0) {
                    c_des.setError("Field Mandatory");
                    c_des.requestFocus();
                } else if (yr_st.getText().toString().length() == 0) {
                    yr_st.setError("Field Mandatory");
                    yr_st.requestFocus();
                } else if (website.getText().toString().trim().length() == 0) {
                    website.setError("Field Mandatory");
                    website.requestFocus();
                }else if (no_wrkr.getText().toString().length() == 0) {
                    no_wrkr.setError("Field Mandatory");
                    no_wrkr.requestFocus();
                } else {
                    submit.setEnabled(false);
                    new AboutCompany(EditCompanyDetails.this, mApp.getPreference().getString(Common.u_id, ""),
                            yr_st.getText().toString(),no_wrkr.getText().toString().trim(), c_des.getText().toString().trim(),website.getText().toString().trim(),submit).execute();
                }

            }
        });

    }
}
