package com.yakovlaptev.vkr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yakovlaptev.vkr.Models.Event;
import com.yakovlaptev.vkr.Models.User;
import com.yakovlaptev.vkr.Services.JSONController;
import com.yakovlaptev.vkr.Services.PostTaskListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MyEvents extends Fragment {

    View rootView;
    boolean my_events;

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
                //Toast.makeText(rootView.getContext(), ((TextView) itemClicked).getText(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(rootView.getContext(), position+"  "+id, Toast.LENGTH_SHORT).show();
            }
        });
        /*Button button = rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(rootView.getContext(), "button", Toast.LENGTH_SHORT).show();
                requestMyEvents();
            }
        });*/


        postTaskListener = new PostTaskListener<JSONArray>() {
            @Override
            public void onPostTask(JSONArray result) {
                //Log.d("JSON RES", result.toString());
                JSONArray jsonArray = new JSONArray();
                if(my_events) {
                    try {
                        jsonArray = new JSONArray("[{\"id\":1,\"name\":\"event1\",\"about\":\"sdf\",\"date\":1526158800000,\"creator\":{\"id\":1,\"email\":\"sdf\",\"name\":\"sdf\",\"avatar\":\"sdf\",\"about\":\"sdf\"},\"users\":[{\"id\":1,\"email\":\"sdf\",\"name\":\"sdf\",\"avatar\":\"sdf\",\"about\":\"sdf\"}]},{\"id\":2,\"name\":\"event2\",\"about\":\"tet\",\"date\":1525986000000,\"creator\":{\"id\":1,\"email\":\"sdf\",\"name\":\"sdf\",\"avatar\":\"sdf\",\"about\":\"sdf\"},\"users\":[]}]");
                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }
                } else {
                    try {
                        jsonArray = new JSONArray("[{\"id\":1,\"name\":\"event1\",\"about\":\"sdf\",\"date\":1526158800000,\"creator\":{\"id\":1,\"email\":\"sdf\",\"name\":\"sdf\",\"avatar\":\"sdf\",\"about\":\"sdf\"},\"users\":[{\"id\":1,\"email\":\"sdf\",\"name\":\"sdf\",\"avatar\":\"sdf\",\"about\":\"sdf\"}]},{\"id\":2,\"name\":\"event2\",\"about\":\"tet\",\"date\":1525986000000,\"creator\":{\"id\":1,\"email\":\"sdf\",\"name\":\"sdf\",\"avatar\":\"sdf\",\"about\":\"sdf\"},\"users\":[]},{\"id\":3,\"name\":\"event3\",\"about\":\"sdf\",\"date\":1525899600000,\"creator\":{\"id\":2,\"email\":\"sdf\",\"name\":\"sdf\",\"avatar\":\"sdf\",\"about\":\"sdf\"},\"users\":[]},{\"id\":4,\"name\":\"event4\",\"about\":\"dfgd\",\"date\":1525294800000,\"creator\":{\"id\":3,\"email\":\"sdf\",\"name\":\"sdf\",\"avatar\":\"sdf\",\"about\":\"sdf\"},\"users\":[]}]");
                    } catch (JSONException e) {
                        Log.e("JSON Parser", "Error parsing data " + e.toString());
                    }
                }
                result = jsonArray;
                if (result.length() > 1) {
                    List<Event> events = new ArrayList<>();
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            events.add(Event.parseJsonData(result.getJSONObject(i)));
                        } catch (JSONException | ParseException e) {
                            Log.e("JSON Parser", "Error parsing data " + e.toString());
                        }
                    }
                    ArrayAdapter adapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_list_item_1, events);
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
        Bundle bundle = getArguments();
        my_events = bundle.getBoolean("my_events");
        requestMyEvents();
        super.onActivityCreated(savedInstanceState);
    }

    public void requestMyEvents() {
        User user = new User();
        user.setName("123");
        user.setAbout("123");
        user.setAvatar("123");
        user.setEmail("123");

        //new JSONController("http://192.168.137.103:8080/users",User.getJsonData(user), "POST", postTaskListener).execute(null, null, null);
        if(my_events) {
            new JSONController("http://192.168.137.103:8080/users/my_events/1", new JSONObject(), "GET", postTaskListener).execute(null, null, null);
        } else {
            new JSONController("http://192.168.137.103:8080/events", new JSONObject(), "GET", postTaskListener).execute(null, null, null);
        }

       /* try {
            user = User.parseJsonData(response);
        } catch (Exception e) {
            Log.d("WIFI INFO ","+++++++++");
        }*/

        Log.d("JSON INFO ", "+++++++++");
    }


}
