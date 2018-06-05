package com.yakovlaptev.vkr;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.yakovlaptev.vkr.MainActivity.SERVER;

public class Events extends Fragment {

    View rootView;
    List<Event> events = new ArrayList<>();

    PostTaskListener<JSONArray> postTaskListener;
    ListView listView;

    public Events() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_events, container, false);
        listView = rootView.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(rootView.getContext(), DetailEvent.class);
                intent.putExtra("event", events.get(position));
                startActivity(intent);
            }
        });

        View mainView = inflater.inflate(R.layout.app_bar_main, container, false);
        FloatingActionButton fabMain = mainView.findViewById(R.id.fab);
        fabMain.setVisibility(View.INVISIBLE);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(rootView.getContext(), AddEvent.class);
                startActivity(intent);
            }
        });

        postTaskListener = new PostTaskListener<JSONArray>() {
            @Override
            public void onPostTask(JSONArray result) {
                Log.d("JSON RES", result.toString());
                if (result.length() > 0) {
                    events = new ArrayList<>();
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            events.add(Event.parseJsonData(result.getJSONObject(i)));
                        } catch (JSONException | ParseException e) {
                            Log.e("JSON Parser", "Error parsing data " + e.toString());
                        }
                    }
                    Log.d("EVENTS events", events.toString());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        events.sort(new Comparator<Event>() {
                            @Override
                            public int compare(Event o1, Event o2) {
                                return Long.compare(o2.getDate().getTime(), o1.getDate().getTime());
                            }
                        });
                    }
                    Log.d("EVENTS events", events.toString());

                    ArrayAdapter adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, events);
                    listView.setAdapter(adapter);
                }
            }
        };



        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        requestEvents();
        super.onActivityCreated(savedInstanceState);
    }

    public void requestEvents() {
        new JSONController(SERVER+"/events", null, "GET", postTaskListener).execute(null, null, null);
        Log.d("JSON INFO ", "+++++++++");
    }


}
