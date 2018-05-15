package com.yakovlaptev.vkr.Services;


import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONController extends AsyncTask<String, Void, JSONArray> {

    private JSONObject jso;
    private String url;
    private String metod;


    public JSONArray res = new JSONArray();
    private PostTaskListener<JSONArray> postTaskListener;

    public JSONController(String url,JSONObject jso, String metod, PostTaskListener<JSONArray> postTaskListener) {
        super();
        this.url = url;
        this.jso = jso;
        this.metod = metod;
        this.postTaskListener = postTaskListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONArray doInBackground(String... urls) {
        JSONArray jsonObject = loadJSON(url);
        //Log.d("JSON doInBackground",jsonObject.toString());
        return jsonObject;
        //return JSONParser.makeRequest(urls[0], jso);
    }

    private JSONArray loadJSON(String url) {
        JSONParser jsonParser = new JSONParser();
        return jsonParser.makeHttpRequest(url, metod, jso);
    }

    @Override
    protected void onPostExecute(JSONArray jsonData) {
        //Log.d("JSON onPostExecute",jsonData.toString());
        res = jsonData;
        if (postTaskListener != null)
            postTaskListener.onPostTask(res);
        super.onPostExecute(jsonData);
    }

}