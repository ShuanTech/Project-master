package com.shuan.Project.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.shuan.Project.Utils.Common;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Android on 7/6/2017.
 */

public class RemoveProfilePic extends AsyncTask<String, String, String> {

    private Context mContext;
    private String proPic;
    private Common mApp;
    public String uId, stus, s;
    ;
    public HashMap<String, String> uData;
    private static JSONObject jsonObj = null;

    public RemoveProfilePic(Context mContext, String uId) {
        this.mContext = mContext;
        this.uId = uId;
    }

    @Override
    protected String doInBackground(String... params) {
        uData = new HashMap<String, String>();
        uData.put("u_id", uId);
        //uData.put("stus",stus);
        try {
            JSONObject json = Connection.UrlConnection(php.removeProPic, uData);
//            Log.d("RemoveProfilePic", json.toString());
            int succ = json.getInt("success");
            if (succ == 0) {
                s = "false";
            } else {
                s = "true";
                proPic = json.getString("pro_pic");
            }
        } catch (Exception e) {
        }
        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s.equalsIgnoreCase("true")) {
            Toast.makeText(mContext, "Successfully Removed", Toast.LENGTH_SHORT).show();
            //Toast.makeText(mContext,proPic,Toast.LENGTH_SHORT).show();


//            JSONArray jsonArray = jsonObj.getJSONArray("data");
//            JSONObject child = jsonArray.getJSONObject(0);
//            data = child.optString("data");

//            Intent myIntent = new Intent(mContext, ProfileActivity.class);
//            mContext.startActivity(myIntent);
        } else {
            Toast.makeText(mContext, "Couldn't Remove! Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
