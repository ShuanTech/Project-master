package com.shuan.Project.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.shuan.Project.employer.EmployerActivity;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;

import org.json.JSONObject;

import java.util.HashMap;


public class PostJob extends AsyncTask<String, String, String> {

    private ProgressDialog pDialog;
    private Context mContext;
    private String uId, title, skill, type, category, salary, location, level, descr, what, qual, vacancy;
    private HashMap<String, String> jData;
    private String s;
    private Button but;

    public PostJob(Context mContext, String uId, String title, String skill, String type, String category, String salary, String location,
                   String level, String qual, String descr,  String vacancy, Button but) {
        this.mContext = mContext;
        this.uId = uId;
        this.title = title;
        this.skill = skill;
        this.type = type;
        this.category = category;
        this.salary = salary;
        this.location = location;
        this.level = level;
        this.qual = qual;
        this.descr = descr;
        this.vacancy = vacancy;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Job Posting...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        jData = new HashMap<String, String>();
        jData.put("u_id", uId);
        jData.put("title", title);
        jData.put("skill", skill);
        jData.put("type", type);
        jData.put("cate", category);
        jData.put("sal", salary);
        jData.put("loc", location);
        jData.put("level", level);
        jData.put("qual",qual);
        jData.put("descr", descr);
        jData.put("vacancy",vacancy);

        try {
            JSONObject json = Connection.UrlConnection(php.jobPost, jData);
            int succ = json.getInt("success");
            if (succ == 0) {
                s = "false";
            } else {
                s = "true";
            }
        } catch (Exception e) {
        }

        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pDialog.cancel();
        if (s.equalsIgnoreCase("true")) {
            Toast.makeText(mContext, "Job Posted Successfully", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(mContext, EmployerActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(in);
            ((AppCompatActivity) mContext).finish();
        } else {
            but.setEnabled(true);
            Toast.makeText(mContext, "Something went Wrong. Try again.", Toast.LENGTH_SHORT).show();
        }
    }


}
