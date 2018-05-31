package com.yakovlaptev.vkr;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.yakovlaptev.vkr.Models.Event;
import com.yakovlaptev.vkr.Models.User;
import com.yakovlaptev.vkr.Services.JSONController;
import com.yakovlaptev.vkr.Services.PostTaskListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddEvent extends BaseActivity {

    Event event = new Event();
    PostTaskListener<JSONArray> postTaskListener;

    private EditText nameField;
    private EditText aboutField;
    private TextView dateField;
    private Calendar dateAndTime = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        nameField = findViewById(R.id.name);
        aboutField = findViewById(R.id.about);
        dateField = findViewById(R.id.date);

        findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestAdd();
            }
        });

        setInitialDateTime();

        postTaskListener = new PostTaskListener<JSONArray>() {
            @Override
            public void onPostTask(JSONArray result) {
                hideProgressDialog();
                startActivity(new Intent(AddEvent.this, MainActivity.class));
            }
        };
    }


    public void requestAdd() {
        showProgressDialog();

        event.setName(nameField.getText().toString());
        event.setAbout(aboutField.getText().toString());
        event.setDate(dateAndTime.getTime());
        event.setCreator(MainActivity.currentUser);

        try {
            Toast.makeText(this, Event.getJsonData(event).toString(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Log.e("Toast.makeText", e.getLocalizedMessage());
        }

        try {
            new JSONController("http://192.168.137.103:8080/events", Event.getJsonData(event), "POST", postTaskListener).execute(null, null, null);
        } catch (JSONException e) {
            Log.e("JSONController", e.getLocalizedMessage());

        }

    }

    private void setInitialDateTime() {
        dateField.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }

    public void setDate(View v) {
        new DatePickerDialog(this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void setTime(View v) {
        new TimePickerDialog(this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

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
