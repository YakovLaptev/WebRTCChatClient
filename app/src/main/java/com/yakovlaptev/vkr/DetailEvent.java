package com.yakovlaptev.vkr;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Objects;

import static com.yakovlaptev.vkr.LoginActivity.SERVER;

public class DetailEvent extends BaseActivity {

    private Event event;
    PostTaskListener<JSONArray> postTaskListener;

    private TextView nameField;
    private TextView aboutField;
    private TextView dateField;
    private TextView creatorField;
    private ListView listView;
    private Button button;
    private Button buttonAdd;
    private boolean subscribed = false;
    private boolean connect = false;




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
        buttonAdd = findViewById(R.id.buttonConnect);

        event = (Event) Objects.requireNonNull(this.getIntent().getExtras()).get("event");
        Log.d("-----event-----", event.toString());

        nameField.setText("Name: "+ event.getName());
        aboutField.setText("About: "+ event.getAbout());
        dateField.setText("Date: "+ event.getDate().toString());
        if(event.getCreator().getEmail().equals(MainActivity.currentUser.getEmail())) {
            connect = true;
            button.setVisibility(View.INVISIBLE);
            creatorField.setText("Creator: Me");
        } else {
            connect = false;
            creatorField.setText(String.format("Creator: %s", String.format("%s : %s", event.getCreator().getName(), event.getCreator().getEmail())));
        }
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, event.getUsers());
        listView.setAdapter(adapter);


        for(User user : Objects.requireNonNull(event).getUsers()) {
            if(user.getEmail().equals(MainActivity.currentUser.getEmail())) {
                subscribed = true;
                button.setText("UNSUBSCRIBE");
                connect = true;
                break;
            }
        }

        if(connect) {
            buttonAdd.setVisibility(View.VISIBLE);
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailEvent.this, VideoChatActivity.class);
                    intent.putExtra("event_id", event.getId());
                    MainActivity.chatIntent = intent;
                    startActivity(intent);
                }
            });
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
                hideProgressDialog();
                try {
                    Event eventRes = Event.parseJsonData((JSONObject) result.get(0));
                    if(subscribed) {
                        Log.d("JSON EVENT", eventRes.toString()+"subscribed");
                        Toast.makeText(DetailEvent.this, "Unubscribed", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("JSON EVENT", eventRes.toString()+"unsubscribed");
                        Toast.makeText(DetailEvent.this, "Subscribed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException | ParseException e) {
                    Log.e("onPostTask", e.getLocalizedMessage());
                }
                startActivity(new Intent(DetailEvent.this, MainActivity.class));
            }
        };
    }


    public void requestSubscribe() {
        if(subscribed) {
            subscribed = false;
            button.setText("SUBSCRIBE");
            showProgressDialog();
            new JSONController(SERVER+"/events/unsubscribe/"+event.getId()+"/"+MainActivity.currentUser.getId(), null, "GET", postTaskListener).execute(null, null, null);
        } else {
            showProgressDialog();
            new JSONController(SERVER+"/events/subscribe/"+event.getId()+"/"+MainActivity.currentUser.getId(), null, "GET", postTaskListener).execute(null, null, null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(LoginActivity.mainIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
