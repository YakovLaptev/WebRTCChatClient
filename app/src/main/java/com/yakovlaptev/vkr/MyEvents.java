package com.yakovlaptev.vkr;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.yakovlaptev.vkr.Services.JSONController;
import com.yakovlaptev.vkr.Services.PostTaskListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.yakovlaptev.vkr.LoginActivity.SERVER;

public class MyEvents extends Fragment {

    View rootView;

    PostTaskListener<JSONArray> postTaskListener;

    public MyEvents() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_my_events, container, false);
        final ListView listView = rootView.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(rootView.getContext(), VideoChatActivity.class);
                String pos = String.valueOf(position);
                intent.putExtra("event_id", pos);
                startActivity(intent);
            }
        });

        View mainView = inflater.inflate(R.layout.app_bar_main, container, false);
        FloatingActionButton fab = mainView.findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        postTaskListener = new PostTaskListener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onPostTask(JSONArray result) {
                if (result.length() > 0) {
                    List<Event> events = new ArrayList<>();
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            events.add(Event.parseJsonData(result.getJSONObject(i)));
                        } catch (JSONException | ParseException e) {
                            Log.e("JSON Parser", "Error parsing data " + e.toString());
                        }
                    }

                    events.sort(new Comparator<Event>() {
                        @Override
                        public int compare(Event o1, Event o2) {
                            return Long.compare(o1.getDate().getTime(), o2.getDate().getTime());
                        }
                    });

                    ArrayAdapter adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, events);
                    listView.setAdapter(adapter);
                }
            }
        };

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        requestMyEvents();
        super.onActivityCreated(savedInstanceState);
    }

    public void requestMyEvents() {
        new JSONController(SERVER+"/users/my_events/" + MainActivity.currentUser.getId(), new JSONObject(), "GET", postTaskListener).execute(null, null, null);
        Log.d("JSON INFO ", "+++++++++");
    }


}
