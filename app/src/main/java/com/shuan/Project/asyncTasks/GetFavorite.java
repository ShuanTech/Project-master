package com.shuan.Project.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shuan.Project.Utils.Common;
import com.shuan.Project.adapter.ConnectAdapter;
import com.shuan.Project.list.Sample;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Android on 9/4/2016.
 */
public class GetFavorite extends AsyncTask<String,String,String> {
    private Context mContext;
    private ListView listView;
    private TextView textView;
    private ProgressBar progressBar;
    private HashMap<String, String> cData;
    private Common mApp;
    private ArrayList<Sample> list;
    private ConnectAdapter adapter;
    private String u_id,s;


    public GetFavorite(Context mContext, ListView listView, ProgressBar progressBar,TextView textView,String id) {
        this.mContext = mContext;
        this.listView = listView;
        this.textView = textView;
        this.progressBar = progressBar;
        this.u_id=id;
        list=new ArrayList<Sample>();
    }

    @Override
    protected String doInBackground(String... params) {

        cData = new HashMap<String, String>();
        cData.put("u_id",u_id);

        try {
            JSONObject json= Connection.UrlConnection(php.getFav,cData);
            int succ=json.getInt("success");
            if(succ==1){
                s="false";
            }else{
                JSONArray jsonArray=json.getJSONArray("favorite");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject child=jsonArray.getJSONObject(i);

                    String u_id = child.optString("u_id");
                    String pro_pic = child.optString("pro_pic");
                    String name = child.optString("full_name");
                    String level = child.optString("level");
                    String sec = child.optString("sec");
                    list.add(new Sample(u_id,pro_pic,name,level,sec));
                }
                s="true";

            }
        }catch (Exception e){}
        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (progressBar.getVisibility()==View.VISIBLE){
            textView.setVisibility(View.GONE);
        }

        progressBar.setVisibility(View.GONE);
        if(s.equalsIgnoreCase("true")){
            listView.setVisibility(View.VISIBLE);
            adapter = new ConnectAdapter(mContext,list);
            listView.setAdapter(adapter);
        }else if (s.equalsIgnoreCase("false")){
            textView.setVisibility(View.VISIBLE);
            Toast.makeText(mContext,"No Data to show",Toast.LENGTH_SHORT).show();
        }
    }
}
