package com.yakovlaptev.vkr.Services;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class JSONParser {

    private InputStream is = null;
    private JSONArray jObj = null;
    private String json = "";

    public JSONParser() {

    }

/*    public static JSONObject makeRequest(final String uri, final JSONObject json) {
        final HttpURLConnection[] urlConnection = new HttpURLConnection[1];
        String url;
        final JSONObject[] result = {null};
        Thread t = new Thread() {

            public void run() {
                try {
                    //Connect
                    urlConnection[0] = (HttpURLConnection) ((new URL(uri).openConnection()));
                    urlConnection[0].setDoOutput(true);
                    urlConnection[0].setRequestProperty("Content-Type", "application/json");
                    urlConnection[0].setRequestProperty("Accept", "application/json");
                    urlConnection[0].setRequestMethod("POST");
                    urlConnection[0].connect();

                    //Write
                    OutputStream outputStream = urlConnection[0].getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    writer.write(json.toString());
                    Log.d("JSON REQ: ", json.toString());

                    writer.close();
                    outputStream.close();

                    //Read
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection[0].getInputStream(), "UTF-8"));

                    String line = null;
                    StringBuilder sb = new StringBuilder();

                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }

                    Log.d("JSON RES: ", sb.toString());


                    bufferedReader.close();
                    result[0] = new JSONObject(sb.toString());

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();
        return result[0];
    }*/


    public JSONArray makeHttpRequest(final String url, final String method, final JSONObject jsonObject) {
        Thread t = new Thread() {

            public void run() {
                try {
                    Log.d("makeHttpRequest", url + " : " + method + " : " + jsonObject);
                    if (Objects.equals(method, "POST")) {
                        DefaultHttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);
                        httpPost.setHeader("Content-type", "application/json");

                        StringEntity se = new StringEntity(jsonObject.toString());
                        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                        httpPost.setEntity(se);

                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        is = httpEntity.getContent();

                    } else if (Objects.equals(method, "GET")) {

                        DefaultHttpClient httpClient = new DefaultHttpClient();
//                String paramString = URLEncodedUtils.format(params, "utf-8");
//                url += "?" + paramString;
                        HttpGet httpGet = new HttpGet(url);

                        HttpResponse httpResponse = httpClient.execute(httpGet);
                        HttpEntity httpEntity = httpResponse.getEntity();
                        is = httpEntity.getContent();
                    }

                } catch (IOException e) {
                    Log.e("Buffer Error", "Error converting result " + e.getLocalizedMessage());

                }

                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    is.close();
                    json = sb.toString();
                    //Log.d("JSON", json.toString());

                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }

                // пробуем распарсит JSON объект
                try {
                    jObj = new JSONArray(json);
                } catch (JSONException e) {
                    jObj = new JSONArray();
                    try {
                        jObj.put(new JSONObject(json));
                    } catch (JSONException e1) {
                        Log.e("JSON Parser", "Error parsing JSONObject " + e1.toString());
                    }
                }

            }
        };

        synchronized (this) {
            t.start();
            try {
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("JSON RETURN",jObj.toString());
        return jObj;

    }
}