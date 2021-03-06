package com.yakovlaptev.vkr;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yakovlaptev.vkr.Models.Request;
import com.yakovlaptev.vkr.Services.JSONController;
import com.yakovlaptev.vkr.Services.PostTaskListener;

import org.json.JSONArray;

import static com.yakovlaptev.vkr.LoginActivity.SERVER;

public class DetailRequest extends BaseActivity implements View.OnClickListener {

    private Request request;
    PostTaskListener<JSONArray> postTaskListener;

    private TextView eventField;
    private TextView userField;
    private Button button1, button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        eventField = findViewById(R.id.event);
        userField = findViewById(R.id.user);

        button1 = findViewById(R.id.accept);
        button2 = findViewById(R.id.deny);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);

        request = (Request) this.getIntent().getExtras().get("request");

        eventField.setText(String.format("Event: %s : %s", request.getEvent().getName(), request.getEvent().getDate()));
        userField.setText(String.format("User: %s : %s",
                request.getEvent().getUsers().get(request.getEvent().getUsers().size()-1).getName(),
                request.getEvent().getUsers().get(request.getEvent().getUsers().size()-1).getEmail()));

       /* postTaskListener = new PostTaskListener<JSONArray>() {
            @Override
            public void onPostTask(JSONArray result) {
                //Log.d("JSON RES", result.toString());
                hideProgressDialog();

                JSONArray jsonArray = new JSONArray();
                result = jsonArray;

                try {
                    Event eventRes = Event.parseJsonData((JSONObject) result.get(0));
                    Log.d("JSON USER", eventRes.toString());
                    Toast.makeText(DetailRequest.this, eventRes.getName() + "has been added", Toast.LENGTH_SHORT).show();
                } catch (JSONException | ParseException e) {
                    Log.e("onPostTask", e.getLocalizedMessage());
                }

            }
        };*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accept: {
                request(true);
                startActivity(LoginActivity.mainIntent);
                break;
            }
            case R.id.deny: {
                request(false);
                startActivity(LoginActivity.mainIntent);
                break;
            }
        }
    }

    public void request(boolean accept) {
        //showProgressDialog();

        if (accept) {
            new JSONController(SERVER+"/requests/" + request.getId() + "/accept", null, "GET", postTaskListener).execute(null, null, null);
        } else if (!accept) {
            new JSONController(SERVER+"/requests/" + request.getId() + "/deny", null, "GET", postTaskListener).execute(null, null, null);
        }
    }
}
