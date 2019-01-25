package com.yakovlaptev.vkr.Services;


import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class JSONController extends AsyncTask<String, Void, JSONArray> {

    private JSONObject jso;
    private String url;
    private String metod;


    public JSONArray res = new JSONArray();
    private PostTaskListener<JSONArray> postTaskListener;

    public JSONController(String url, JSONObject jso, String metod, PostTaskListener<JSONArray> postTaskListener) {
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
        //JSONArray jsonObject = loadJSON(url);
        //Log.d("JSON doInBackground",jsonObject.toString());
        //return jsonObject;
        //return JSONParser.makeRequest(urls[0], jso);
        JSONArray jsonArray = new JSONArray();
        try {
            URL request_url = new URL(url); //in the real code, there is an ip and a port
            HttpURLConnection conn = (HttpURLConnection) request_url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            Log.d("REQUEST", String.valueOf(conn.getRequestProperties()));

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jso.toString());

            os.flush();
            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null)
            {
                sb.append(line).append("\n");
            }
            String response = sb.toString();
            Log.d("STATUS", String.valueOf(conn.getResponseCode()));
            Log.d("MSG", response);

            try {
                jsonArray = new JSONArray(response);
            } catch (JSONException e) {
                jsonArray = new JSONArray();
                try {
                    jsonArray.put(new JSONObject(response));
                } catch (JSONException e1) {
                    Log.e("JSON Parser", "Error parsing JSONObject " + e1.toString());
                }
            }

            conn.disconnect();
        } catch (Exception ignored) {
            Log.e("Connect Error", "Error getting result " + ignored.getLocalizedMessage());
        }
        return jsonArray;
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