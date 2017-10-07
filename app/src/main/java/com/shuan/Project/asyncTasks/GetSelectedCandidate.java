package com.shuan.Project.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shuan.Project.adapter.SelectedListAdapter;
import com.shuan.Project.list.Sample;
import com.shuan.Project.parser.Connection;
import com.shuan.Project.parser.php;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class GetSelectedCandidate extends AsyncTask<String, String, String> {

    private Context mContext;
    private String jId;
    private ListView listView;
    private ProgressBar progressBar;
    private HashMap<String, String> sData;
    private ArrayList<Sample> list;
    private SelectedListAdapter adapter;
    private TextView textView;

    public GetSelectedCandidate(Context mContext, String jId, ListView listView, ProgressBar progressBar, TextView textView) {
        this.mContext = mContext;
        this.jId = jId;
        this.listView = listView;
        this.progressBar = progressBar;
        this.textView = textView;
        list = new ArrayList<Sample>();
    }

    @Override
    protected String doInBackground(String... params) {
        sData = new HashMap<String, String>();
        sData.put("jId", jId);
        try {

            JSONObject json = Connection.UrlConnection(php.slcted_candidate_list, sData);
            int succ = json.getInt("success");
            if (succ == 0) {

            } else {
                JSONArray jsonArray = json.getJSONArray("job");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject child = jsonArray.getJSONObject(i);
                    String pro_pic = child.optString("pro_pic");
                    String name = child.optString("name");
                    String venue = child.optString("venue");
                    String intervew_date = child.optString("intervew_date");
                    String intervew_time = child.optString("intervew_time");
                    String type = child.optString("type");
                    list.add(new Sample(pro_pic, name, venue, intervew_date, intervew_time, type));
                }

            }

        } catch (Exception e) {
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.setVisibility(View.GONE);
        if(textView.getVisibility()==View.VISIBLE){
            textView.setVisibility(View.GONE);
        }if(list.size()==0){
            textView.setText("No Data");
            textView.setVisibility(View.VISIBLE);
        }else {
            listView.setVisibility(View.VISIBLE);
            adapter = new SelectedListAdapter(mContext, list);
            listView.setAdapter(adapter);
        }
    }
}
