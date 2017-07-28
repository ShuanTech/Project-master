package com.shuan.Project.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shuan.Project.Utils.Common;
import com.shuan.Project.adapter.PostAdapter;
import com.shuan.Project.list.Sample;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Android on 10/27/2016.
 */

public class GetHome extends AsyncTask<String, String, String> {
    private Context mContext;
    private ListView listView;
    private TextView textView;
    private ProgressBar progressBar;
    private HashMap<String, String> cData;
    private Common mApp;
    private ArrayList<Sample> list;
    private PostAdapter adapter;
    private String u_id, type,s;
    private SwipeRefreshLayout swipe;

    public GetHome(Context mContext, ListView listView, ProgressBar progressBar, TextView textView,String u_id,String type,SwipeRefreshLayout swipe) {
        this.mContext = mContext;
        this.listView = listView;
        this.textView = textView;
        this.progressBar = progressBar;
        this.u_id = u_id;
        this.type=type;
        list = new ArrayList<Sample>();
        this.swipe=swipe;

    }

    @Override
    protected String doInBackground(String... params) {
        cData = new HashMap<String, String>();
        cData.put("u_id", u_id);
        cData.put("type",type);

        try {
            JSONObject json = Connection.UrlConnection(php.get_post, cData);
            int succ = json.getInt("success");
            if (succ == 0) {
                s="false";
            } else {
                s="true";
                JSONArray jsonArray = json.getJSONArray("post");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject child = jsonArray.getJSONObject(i);

                    String cName = child.optString("cmpny_name");
                    String pPic = child.optString("pro_pic");
                    String jId = child.optString("job_id");
                    String jTitle = child.optString("title");
                    String jSkill = child.optString("skill");
                    String jLevel = child.optString("level");
                    String jLoc = child.optString("location");
                    String jDated = child.optString("date_created");
                    String jView = child.optString("viewed");
                    String jShare = child.optString("shared");
                    String jApplied = child.optString("applied");
                    String jFrmId = child.optString("frm_id");
                    String jImp = child.optString("is_important");
                    String fPropic=child.optString("fp");
                    String flvl=child.optString("lvl");
                    String fshrd=child.optString("shred");
                    String fshrddate = child.optString("sharedDate");
//                    This was orginally here
//                    list.add(new Sample(cName, pPic, jId, jTitle, jSkill, jLevel, jLoc, jDated, jView, jApplied, jShare, jFrmId,jImp,fPropic,fshrd,flvl));

                    list.add(new Sample(cName, pPic, jId, jTitle, jSkill, jLevel, jLoc, jDated, jView, jApplied, jShare, jFrmId,jImp,fPropic,fshrd,flvl,fshrddate));
                }
            }

        } catch (Exception e) {
        }
        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(progressBar.getVisibility()==View.VISIBLE){
            textView.setVisibility(View.GONE);
        }
        if (s.equalsIgnoreCase("true")) {
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            adapter = new PostAdapter(mContext, list);
            listView.setAdapter(adapter);
            swipe.setRefreshing(false);
        }else if (s.equalsIgnoreCase("false")){
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            swipe.setRefreshing(false);
        }
    }
}
