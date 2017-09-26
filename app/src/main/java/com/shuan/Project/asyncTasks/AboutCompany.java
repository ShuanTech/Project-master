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

public class AboutCompany extends AsyncTask<String, String, String> {

    private Common mApp;
    private Context mContext;
    private String uId,yrSt, noWk, cDes,website, s="";
    private HashMap<String, String> aData;
    private Button but;

    public AboutCompany(Context mContext, String u_id,String yrSt, String noWk, String cDes,String website,Button but) {
        this.mContext = mContext;
        this.uId=u_id;
        this.yrSt = yrSt;
        this.noWk = noWk;
        this.cDes = cDes;
        this.website = website;
        this.but=but;
        mApp = (Common) mContext.getApplicationContext();
    }

    @Override
    protected String doInBackground(String... params) {

        aData = new HashMap<String, String>();
        aData.put("u_id",uId);
        aData.put("yearstrt",yrSt);
        aData.put("workers",noWk);
        aData.put("descrp",cDes);
        aData.put("website",website);

        try {

            JSONObject json= Connection.UrlConnection(php.abtCmpny,aData);
            int succ=json.getInt("success");
            if(succ==0){
                s="false";
            }else{
                s="true";
            }
        }catch (Exception e){}
        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        save(s);
    }

    private void save(String s) {
        if(s.equalsIgnoreCase("true")){
            if(but.getText().toString().equalsIgnoreCase("SUBMIT")){
                Toast.makeText(mContext,"Successfully Updated",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext,"Successfully Completed Your Signup Process. Let's begin",Toast.LENGTH_SHORT).show();
                mApp.getPreference().edit().putBoolean("start",false).commit();
            }

            new GetInfo(mContext,mApp.getPreference().getString(Common.u_id,"")).execute();
            Intent in=new Intent(mContext, EmployerActivity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(in);
            ((AppCompatActivity)mContext).finish();
        }else{
            but.setEnabled(false);
            Toast.makeText(mContext,"Something went Wrong. Try Again...",Toast.LENGTH_SHORT).show();
        }
    }

}

