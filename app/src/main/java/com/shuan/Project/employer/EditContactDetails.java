package com.shuan.Project.employer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shuan.Project.R;
import com.shuan.Project.Utils.Common;
import com.shuan.Project.Utils.EmailValidator;
import com.shuan.Project.Utils.Helper;
import com.shuan.Project.asyncTasks.CompanyContactInfo;

public class EditContactDetails extends AppCompatActivity implements View.OnClickListener {

    private Helper helper = new Helper();
    private Common mApp;
    private boolean exit = false;
    private EditText cnt_prsn, cnt_mail, cnt_phn, avl_time, frm_time, to_time;
    private EmailValidator emailValidator;
    private Button cp_skip, cp_next;
    private String contctTime;
    private TextInputLayout layout_cnt_prsn,layout_cnt_mail,layout_cnt_phn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_company_contact);



         /*Contact Details */

        layout_cnt_mail = (TextInputLayout) findViewById(R.id.layout_cnt_mail);
        layout_cnt_phn = (TextInputLayout) findViewById(R.id.layout_cnt_phn);
        layout_cnt_prsn = (TextInputLayout) findViewById(R.id.layout_cnt_prsn);

        cnt_prsn = (EditText) findViewById(R.id.cnt_prsn);
        cnt_mail = (EditText) findViewById(R.id.cnt_mail);
        cnt_phn = (EditText) findViewById(R.id.cnt_phn);
        frm_time = (EditText) findViewById(R.id.frm_time);
        to_time = (EditText) findViewById(R.id.to_time);
        cp_skip = (Button) findViewById(R.id.cp_skip);
        cp_next = (Button) findViewById(R.id.cp_next);
        emailValidator=new EmailValidator();

        frm_time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showTimePicker("frm");
                }
                return false;
            }
        });
        to_time.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showTimePicker("to");
                }
                return false;
            }
        });


        cnt_prsn.setText(getIntent().getStringExtra("contact_person"));
        cnt_mail.setText(getIntent().getStringExtra("contact_mail"));
        cnt_phn.setText(getIntent().getStringExtra("contact_ph"));
        String time = getIntent().getStringExtra("contact_time");
        String[] parts = time.split("-");
        String part1 = parts[0]; // from time
        String part2 = parts[1]; // to time
        frm_time.setText(part1);
        to_time.setText(part2);

//        cp_skip.setOnClickListener(this);
        cp_next.setOnClickListener(this);

        cnt_prsn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*if (cnt_prsn.getText().toString().length() == 0) {
                    cp_next.setVisibility(View.GONE);
                    cp_skip.setVisibility(View.VISIBLE);
                } else {
                    cp_next.setVisibility(View.VISIBLE);
                    cp_skip.setVisibility(View.GONE);
                }*/
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cp_next:
                if (cnt_mail.getText().toString().length() == 0) {
                    cnt_mail.setError("Mail Id Mandatory");
                    cnt_mail.requestFocus();
                } else if (!emailValidator.validate(cnt_mail.getText().toString())) {
                    cnt_mail.setError("Invalid Email Id");
                    cnt_mail.requestFocus();
                } else if (cnt_phn.getText().toString().length() == 0) {
                    cnt_phn.setError("Phone Number Mandatory");
                    cnt_phn.requestFocus();
                } else if(cnt_phn.getText().toString().length()<10){
                    cnt_phn.setError("Enter Valid Phone Number");
                    cnt_phn.requestFocus();
                }else if (frm_time.getText().toString().length() == 0) {
                    frm_time.setError("Field Mandatory");
                    frm_time.requestFocus();
                } else if (to_time.getText().toString().length() == 0) {
                    to_time.setError("Field Mandatory");
                    to_time.requestFocus();
                } else {
                    contctTime = frm_time.getText().toString() + " - " + to_time.getText().toString();
                    cp_next.setEnabled(false);
                    new CompanyContactInfo(EditContactDetails.this,mApp.getPreference().getString(Common.u_id,""),
                            cnt_prsn.getText().toString(),cnt_mail.getText().toString(),cnt_phn.getText().toString(),
                            contctTime,cp_next).execute();
                }
                break;
            /*case R.id.cp_skip:
                startActivity(new Intent(getApplicationContext(),AboutCompanyActivity.class));
                finish();
                break;*/
        }
    }


    private void showTimePicker(final String where) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.time_picker, null, false);

        // the time picker on the alert dialog, this is how to get the value
        final TimePicker myTimePicker = (TimePicker) view
                .findViewById(R.id.myTimePicker);


        new AlertDialog.Builder(EditContactDetails.this).setView(view)
                .setTitle("Set Time")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int id) {

                        int currentHourText = myTimePicker.getCurrentHour();


                        int currentMinuteText = myTimePicker
                                .getCurrentMinute();
                        updateTime(currentHourText, currentMinuteText, where);
                        dialog.cancel();

                    }

                }).show();
    }

    private void updateTime(int hours, int mins, String where) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        if (where.equalsIgnoreCase("frm")) {
            frm_time.setText(aTime);
        } else {
            to_time.setText(aTime);
        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Press Back again to Cancel the Process.", Toast.LENGTH_SHORT).show();
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

