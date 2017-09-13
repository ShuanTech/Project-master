package com.shuan.Project.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.shuan.Project.Utils.Common;
import com.shuan.Project.employer.ShortListActivity;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;

import org.json.JSONObject;

import java.util.HashMap;

public class SelectCandidate extends AsyncTask<String, String, String> {

    private Context mContext;
    private String jId, aId, rId, loc, dt, tme, cmmts, s;
    private HashMap<String, String> sData;
    private Common mApp;
    private Button but;
    private ProgressDialog pDialog;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Scheduling...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public SelectCandidate(Context mContext, String jId, String aId, String rId, String loc, String dt, String tme, String cmmts,
                           Button but) {
        this.mContext = mContext;
        this.jId = jId;
        this.aId = aId;
        this.rId = rId;
        this.loc = loc;
        this.dt = dt;
        this.tme = tme;
        this.cmmts = cmmts;
        this.but=but;
        mApp = (Common) mContext.getApplicationContext();
    }

    @Override
    protected String doInBackground(String... params) {
        sData = new HashMap<String, String>();
        sData.put("j_id", jId);
        sData.put("a_id", aId);
        sData.put("r_id", rId);
        sData.put("loc", loc);
        sData.put("dt", dt);
        sData.put("tme", tme);
        sData.put("cmmts", cmmts);

        try {

            JSONObject json = Connection.UrlConnection(php.slct_candidate, sData);
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
            Toast.makeText(mContext, "Interview Scheduled Successfully ", Toast.LENGTH_SHORT).show();
            Intent in = new Intent(mContext, ShortListActivity.class);
//            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle b = new Bundle();
            b.putInt("page",1);
            in.putExtras(b);
            AppCompatActivity activity = (AppCompatActivity) mContext;
            new GetInfo(mContext, mApp.getPreference().getString(Common.u_id, "")).execute();
            mContext.startActivity(in);
            activity.finish();

        } else {
            but.setEnabled(true);
            Toast.makeText(mContext, "Something Wrong! Try Again", Toast.LENGTH_SHORT).show();
        }
    }
}
