package org.rmad.wikindia;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rmad.wikindia.org.rmad.wikindia.beans.ResponseBean;
import org.rmad.wikindia.org.rmad.wikindia.beans.WikiDataBean;

import java.io.IOException;
import java.util.ArrayList;


public class BaseActivity extends ActionBarActivity {

    public static final String TAG = "BaseActivity";

    Button reloadButton;
    ListView articlesListView;

    ArticleArrayAdapter adapter;

    ArrayList<WikiDataBean> results;
    String status;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        articlesListView = (ListView) findViewById(R.id.articlesListView);
        reloadButton = (Button) findViewById(R.id.reloadButton);
        DataHelper dataHelper = new DataHelper();
        dataHelper.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setupUI();
    }

    private void setupUI(){
        adapter = new ArticleArrayAdapter(getApplication(),0);
        articlesListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });
    }

    private void fetchData(){
        if (isNetworkAvailable()){
            DataHelper dataHelper = new DataHelper();
            dataHelper.execute();
        }
        else {
            showToast("No Internet!");
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public class DataHelper extends AsyncTask {

        private static final String TEST_URL = "http://127.0.0.1:5000";
        private static final String LIVE_URL = "http://souravbasu17.pythonanywhere.com/";

        Context context;

        @Override
        protected JSONObject doInBackground(Object[] params) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(LIVE_URL)
                        .build();
                Response response = client.newCall(request).execute();
                String json = response.body().string();
                Log.d(TAG,json);
                Gson gson = new Gson();
                ResponseBean responseBean = gson.fromJson(json,ResponseBean.class);
                //if (responseBean.getStatus().equals("success"))
                results = responseBean.getResults();
                /*JSONObject js = new JSONObject(json);
                status = js.getString("status");
                message = js.getString("message");*/
                /*JSONArray jsonArray = new JSONArray(js.getJSONObject("results"));
                WikiDataBean wikiDataBean = new WikiDataBean();
                for (int i=0; i<jsonArray.length(); i++){
                    wikiDataBean.setUrl(jsonArray.getJSONObject(i).getString("url"));
                    wikiDataBean.setTitle(jsonArray.getJSONObject(i).getString("title"));
                    wikiDataBean.setContent(jsonArray.getJSONObject(i).getString("content"));
                    results.add(i,wikiDataBean);
                }*/
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "exception:" + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            setupUI();
        }
    }

    private class ArticleArrayAdapter extends ArrayAdapter {


        public ArticleArrayAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null){
                convertView = getLayoutInflater().inflate(R.layout.item_article,parent,false);
            }
            TextView urlTextView = (TextView) convertView.findViewById(R.id.urlTextView);
            TextView titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
            TextView contentTextView = (TextView) convertView.findViewById(R.id.contentTextView);
            urlTextView.setText(results.get(position).getUrl());
            titleTextView.setText(results.get(position).getTitle());
            contentTextView.setText(results.get(position).getContent());
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
