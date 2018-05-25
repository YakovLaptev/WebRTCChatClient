package com.yakovlaptev.vkr;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.List;

public class DetailEvent extends BaseActivity {

    private Event event;
    PostTaskListener<JSONArray> postTaskListener;

    private TextView nameField;
    private TextView aboutField;
    private TextView dateField;
    private TextView creatorField;
    private ListView listView;
    private Button button;
    private boolean subscribed = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        nameField = findViewById(R.id.name);
        aboutField = findViewById(R.id.about);
        dateField = findViewById(R.id.date);
        creatorField = findViewById(R.id.creator);
        listView = findViewById(R.id.subscribers);
        button = findViewById(R.id.button);

        nameField.setText("Name: "+ event.getName());
        aboutField.setText("About: "+ event.getAbout());
        dateField.setText("Date: "+ event.getDate().toString());
        if(event.getCreator().getEmail().equals("test@yandex.ru")) {
            button.setVisibility(View.INVISIBLE);
            creatorField.setText("Me");
        } else {
            creatorField.setText(String.format("%s : %s", event.getCreator().getName(), event.getCreator().getEmail()));
        }
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, event.getUsers());
        listView.setAdapter(adapter);

        event = (Event) this.getIntent().getExtras().get("event");
        for(User user : event.getUsers()) {
            if(user.getEmail().equals("test@yandex.ru")) {
                subscribed = true;
                button.setText("UNSUBSCRIBE");
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestSubscribe();
            }
        });

        postTaskListener = new PostTaskListener<JSONArray>() {
            @Override
            public void onPostTask(JSONArray result) {
                //Log.d("JSON RES", result.toString());
                hideProgressDialog();

                JSONArray jsonArray = new JSONArray();
                result = jsonArray;

                try {
                    Event eventRes = Event.parseJsonData((JSONObject) result.get(0));
                    Log.d("JSON USER", eventRes.toString());
                    Toast.makeText(DetailEvent.this, eventRes.getName() + "has been added", Toast.LENGTH_SHORT).show();
                } catch (JSONException | ParseException e) {
                    Log.e("onPostTask", e.getLocalizedMessage());
                }

            }
        };
    }


    public void requestSubscribe() {
        if(subscribed) {
            subscribed = false;
            button.setText("SUBSCRIBE");
        }

        showProgressDialog();

      /*  try {
            Toast.makeText(this, Event.getJsonData(event).toString(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Log.e("Toast.makeText", e.getLocalizedMessage());
        }*/

        try {
            new JSONController("http://192.168.137.103:8080/events/subscribe/"+event.getId()+"/"+"1", Event.getJsonData(event), "GET", postTaskListener).execute(null, null, null);
        } catch (JSONException e) {
            Log.e("JSONController", e.getLocalizedMessage());

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
