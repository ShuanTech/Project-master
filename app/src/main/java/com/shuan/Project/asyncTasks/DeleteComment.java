package com.shuan.Project.asyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.shuan.Project.Utils.Common;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Android on 27-Jul-17.
 */

public class DeleteComment extends AsyncTask<String,String,String> {

    private Context mContext;
    private String id,s;
    private ProgressDialog pDialog;
    private HashMap<String, String> pData;

    private Common mApp;

    public DeleteComment(Context mContext, String id) {
        this.mContext = mContext;
        this.id = id;
        mApp = (Common) mContext.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Deleting Comment");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        pData = new HashMap<String, String>();
        pData.put("id", id);

        try {

            JSONObject json = Connection.UrlConnection(php.removeComment, pData);
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


                Toast.makeText(mContext, "Successfully Deleted", Toast.LENGTH_SHORT).show();
//                Intent in = new Intent(mContext, CommentsActivity.class);
//                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                mContext.startActivity(in);
                ((AppCompatActivity) mContext).finish();

        } else {
            Toast.makeText(mContext, "Something went wrong!... Try Again", Toast.LENGTH_SHORT).show();
        }
    }
}
