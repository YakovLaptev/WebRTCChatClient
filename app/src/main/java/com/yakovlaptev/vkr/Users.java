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

        postTaskListener = new PostTaskListener<JSONArray>() {
            @Override
            public void onPostTask(JSONArray result) {
                Log.d("JSON RES", result.toString());
                //JSONArray jsonArray = new JSONArray();
                //result = jsonArray;
                if (result.length() > 0) {
                    users = new ArrayList<>();
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            users.add(User.parseJsonData(result.getJSONObject(i)));
                           // Log.d("EVENTS event", Event.parseJsonData(result.getJSONObject(i)).toString());
                        } catch (JSONException | ParseException e) {
                            Log.e("JSON Parser", "Error parsing data " + e.toString());
                        }
                    }
                 /*   Log.d("EVENTS events", users.toString());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        users.sort(new Comparator<Event>() {
                            @Override
                            public int compare(Event o1, Event o2) {
                                return Long.compare(o1.getDate().getTime(), o2.getDate().getTime());
                            }
                        });
                    }
                    Log.d("EVENTS events", users.toString());*/


                    ArrayAdapter adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, users);
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
        requestUsers();
        super.onActivityCreated(savedInstanceState);
    }

    public void requestUsers() {

        //new JSONController("http://192.168.137.103:8080/users",User.getJsonData(user), "POST", postTaskListener).execute(null, null, null);
        new JSONController("http://192.168.137.103:8080/users", null, "GET", postTaskListener).execute(null, null, null);

        Log.d("JSON INFO ", "+++++++++");
    }


}
