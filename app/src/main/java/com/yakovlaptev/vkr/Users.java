package com.yakovlaptev.vkr;

import android.content.Intent;
import android.os.Build;
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
import com.yakovlaptev.vkr.Models.User;
import com.yakovlaptev.vkr.Services.JSONController;
import com.yakovlaptev.vkr.Services.PostTaskListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.yakovlaptev.vkr.MainActivity.SERVER;

public class Users extends Fragment {

    View rootView;
    List<User> users = new ArrayList<>();

    PostTaskListener<JSONArray> postTaskListener;
    ListView listView;

    public Users() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_users, container, false);
        listView = rootView.findViewById(R.id.listView);

        View mainView = inflater.inflate(R.layout.app_bar_main, container, false);
        FloatingActionButton fab = mainView.findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        postTaskListener = new PostTaskListener<JSONArray>() {
            @Override
            public void onPostTask(JSONArray result) {
                Log.d("JSON RES", result.toString());
                if (result.length() > 0) {
                    users = new ArrayList<>();
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            users.add(User.parseJsonData(result.getJSONObject(i)));
                        } catch (JSONException | ParseException e) {
                            Log.e("JSON Parser", "Error parsing data " + e.toString());
                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, users);
                    listView.setAdapter(adapter);
                }
            }
        };
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        requestUsers();
        super.onActivityCreated(savedInstanceState);
    }

    public void requestUsers() {
        new JSONController(SERVER+"/users", null, "GET", postTaskListener).execute(null, null, null);
        Log.d("JSON INFO ", "+++++++++");
    }


}
