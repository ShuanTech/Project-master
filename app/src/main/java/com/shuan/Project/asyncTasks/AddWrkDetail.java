package com.shuan.Project.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.shuan.Project.Utils.Common;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;
import com.shuan.Project.resume.ResumeEditActivity;

import org.json.JSONObject;

import java.util.HashMap;


public class AddWrkDetail extends AsyncTask<String, String, String> {

    public Context mContext;
    public String uId, org, loc, pos, frmDate, toDate, s = "";
    public HashMap<String, String> rData;
    public ProgressDialog pDialog;
    public Common mApp;
    public boolean Ins;

    public AddWrkDetail(Context mContext, String uId, String org, String loc, String pos, String frmDate, String toDate, boolean Ins) {
        this.mContext = mContext;
        this.uId = uId;
        this.org = org;
        this.loc = loc;
        this.pos = pos;
        this.frmDate = frmDate;
        this.toDate = toDate;
        this.Ins = Ins;
        this.mApp = (Common) mContext.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Adding Work Detail");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }


    @Override
    protected String doInBackground(String... params) {

        rData = new HashMap<String, String>();
        rData.put("u_id", uId);
        rData.put("org_name", org);
        rData.put("position", pos);
        rData.put("loc", loc);
        rData.put("frm", frmDate);
        rData.put("to", toDate);
        rData.put("type", "add");
        if (Ins == true) {
            rData.put("ins", "false");
        } else {
            rData.put("ins", "true");
        }


        try {
            JSONObject json = Connection.UrlConnection(php.work_info, rData);
            Log.d("Json",json.toString());
            int succ = json.getInt("success");
            if (succ == 0) {
                s = "false";
            } else if (succ == 2) {
                s="already";

            } else {
                s = "true";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception",e.toString());
        }
        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pDialog.cancel();
        if (s.equalsIgnoreCase("true")) {
            int val = mApp.getPreference().getInt(Common.PROFILESTRENGTH, 0);
            mApp.getPreference().edit().putInt(Common.PROFILESTRENGTH, val + 2).commit();

            if (mApp.getPreference().getString(Common.LEVEL, "").equalsIgnoreCase("1")) {
                new UpdateLevel(mContext, uId).execute();
                mApp.getPreference().edit().putBoolean(Common.WORKINFO, true).commit();
                mApp.getPreference().edit().putString(Common.LEVEL, "2").commit();
                AppCompatActivity activity = (AppCompatActivity) mContext;
                Intent i = activity.getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(activity.getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(i);
                ((AppCompatActivity) mContext).finish();
            } else {
                mApp.getPreference().edit().putBoolean(Common.WORKINFO, true).commit();


                Toast.makeText(mContext, "Successfully Work Details Added", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(mContext, ResumeEditActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(in);
                ((AppCompatActivity) mContext).finish();
            }


        }else if (s.equalsIgnoreCase("already")){
            Toast.makeText(mContext, "Already added to your profile", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mContext, "Something went wrong!... Try Again", Toast.LENGTH_SHORT).show();
        }
    }
}
