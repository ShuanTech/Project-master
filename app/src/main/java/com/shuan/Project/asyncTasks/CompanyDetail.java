package com.shuan.Project.asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.shuan.Project.Utils.Common;
import com.shuan.Project.employer.EmployerActivity;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;

import org.json.JSONObject;

import java.util.HashMap;


public class CompanyDetail extends AsyncTask<String, String, String> {

    private Context mContext;
    private String uId, orgName,website, cType, iType, city, dis, ste, cntry;
    private boolean ins;
    private HashMap<String, String> cData;
    private String s = "";
    private Common mApp;
    private Button but;

    public CompanyDetail(Context mContext, String u_id, String orgName,String website, String cType, String iType, String city, String dis, String ste,
                         String cntry, boolean ins, Button but) {
        this.mContext = mContext;
        this.uId = u_id;
        this.orgName = orgName;
        this.website = website;
        this.cType = cType;
        this.iType = iType;
        this.city = city;
        this.dis=dis;
        this.ste = ste;
        this.cntry = cntry;
        this.ins = ins;
        this.but = but;
        mApp = (Common) mContext.getApplicationContext();
    }

    @Override
    protected String doInBackground(String... params) {

        cData = new HashMap<String, String>();
        cData.put("u_id", uId);
        cData.put("compname", orgName);
        cData.put("website", website);
        cData.put("compnytype", cType);
        cData.put("indusType", iType);
        cData.put("country", cntry);
        cData.put("state", ste);
        cData.put("dis",dis);
        cData.put("city", city);
        if (ins == true) {
            cData.put("insrt", "false");
        } else {
            cData.put("insrt", "true");
        }

        try {

            JSONObject json = Connection.UrlConnection(php.cmpnyDetail, cData);
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
        if (s.equalsIgnoreCase("true")) {
            mApp.getPreference().edit().putBoolean(Common.COMPANY, true).commit();
            Intent in = new Intent(mContext, EmployerActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(in);
            ((AppCompatActivity) mContext).finish();

            new Follower(mContext, uId, orgName, city).execute();
        } else {
            but.setEnabled(true);
            Toast.makeText(mContext, "Something went wrong! Try again...", Toast.LENGTH_SHORT).show();
        }
    }
}
