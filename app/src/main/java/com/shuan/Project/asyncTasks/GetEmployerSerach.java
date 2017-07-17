package com.shuan.Project.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shuan.Project.R;
import com.shuan.Project.adapter.SearchAdapter;
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
public class GetEmployerSerach extends AsyncTask<String, String, String> {

    private Context mContext;
    private ProgressBar progressBar;
    private AutoCompleteTextView txt;
    private ArrayList<Sample> list;
    private SearchAdapter adapter;
    private String s;
    private HashMap<String, String> eData;

    public GetEmployerSerach(Context mContext, ProgressBar progressBar, AutoCompleteTextView txt) {
        this.mContext = mContext;
        this.progressBar = progressBar;
        this.txt = txt;
        list = new ArrayList<Sample>();
    }

    @Override
    protected String doInBackground(String... params) {

        eData = new HashMap<String, String>();
        eData.put("u_id", "employer");
        try {

            JSONObject json = Connection.UrlConnection(php.emp_search, eData);

            int succ = json.getInt("success");
            if (succ == 0) {
                s = "false";
            } else {
                s = "true";
                JSONArray jsonArray = json.getJSONArray("employer");

                JSONObject child = jsonArray.getJSONObject(0);

                JSONObject skill = child.getJSONObject("skill");
                JSONArray skillArr = skill.getJSONArray("skill");
                for (int i = 0; i < skillArr.length(); i++) {
                    JSONObject data = skillArr.getJSONObject(i);
                    String name = data.optString("skill");
                    String level = data.optString("name");
                    list.add(new Sample(name, level));
                }

                JSONObject pos = child.getJSONObject("pos");
                JSONArray posArr = pos.getJSONArray("pos");
                for (int i = 0; i < posArr.length(); i++) {
                    JSONObject data = posArr.getJSONObject(i);
                    String name=data.optString("position");
                    String level=data.optString("name");
                    list.add(new Sample(name,level));
                }

                JSONObject loc = child.getJSONObject("loc");
                JSONArray locArr = loc.getJSONArray("loc");
                for (int i = 0; i < locArr.length(); i++) {
                    JSONObject data = locArr.getJSONObject(i);
                    String name=data.optString("city");
                    String level=data.optString("name");
                    list.add(new Sample(name,level));
                }
            }

        } catch (Exception e) {
        }


        return s;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("ActivityInfo","GetEmpSearch");
        progressBar.setVisibility(View.GONE);
        if (s.equalsIgnoreCase("true")) {
            adapter = new SearchAdapter(mContext, R.layout.custom_auto_complete_item, R.id.display, list);
            txt.setThreshold(1);
            txt.setAdapter(adapter);
        } else {
            Toast.makeText(mContext, "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }
}
