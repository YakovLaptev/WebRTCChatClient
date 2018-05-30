package com.yakovlaptev.vkr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yakovlaptev.vkr.Models.Event;
import com.yakovlaptev.vkr.Models.Request;
import com.yakovlaptev.vkr.Models.User;
import com.yakovlaptev.vkr.Services.JSONController;
import com.yakovlaptev.vkr.Services.PostTaskListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MyRequests extends Fragment {

    View rootView;

    List<Request> requests = new ArrayList<>();

    PostTaskListener<JSONArray> postTaskListener;

    public MyRequests() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_my_requests, container, false);
        final ListView listView = rootView.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(rootView.getContext(), DetailRequest.class);
                intent.putExtra("request", requests.get(position));
                startActivity(intent);
                //Toast.makeText(rootView.getContext(), ((TextView) itemClicked).getText(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(rootView.getContext(), position+"  "+id, Toast.LENGTH_SHORT).show();
            }
        });

        View mainView = inflater.inflate(R.layout.app_bar_main, container, false);
        FloatingActionButton fab = mainView.findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        postTaskListener = new PostTaskListener<JSONArray>() {
            @Override
            public void onPostTask(JSONArray result) {
                //Log.d("JSON RES", result.toString());
                //JSONArray jsonArray = new JSONArray();
                //result = jsonArray;
                if (result.length() > 0) {
                    requests = new ArrayList<>();
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            requests.add(Request.parseJsonData(result.getJSONObject(i)));
                        } catch (JSONException | ParseException e) {
                            Log.e("JSON Parser", "Error parsing data " + e.toString());
                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, requests);
                    listView.setAdapter(adapter);
                }
                //User user = User.parseJsonData(result);
                //Log.d("JSON USER", user.toString());
                //Toast.makeText(rootView.getContext(), result.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //Bundle bundle = getArguments();
        requestMyEvents();
        super.onActivityCreated(savedInstanceState);
    }

    public void requestMyEvents() {

        //new JSONController("http://192.168.137.103:8080/users",User.getJsonData(user), "POST", postTaskListener).execute(null, null, null);

        new JSONController("http://192.168.137.103:8080/users/my_requests/" + MainActivity.currentUser.getId(), null, "GET", postTaskListener).execute(null, null, null);

        Log.d("JSON INFO ", "+++++++++");
    }
}
