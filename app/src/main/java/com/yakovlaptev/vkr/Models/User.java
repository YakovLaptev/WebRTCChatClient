package com.yakovlaptev.vkr.Models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class User implements Serializable {

    private Long id;

    private String email;

    private String name;

    private String avatar;

    private String about;

    private List<Event> myEvents = new ArrayList<>();

    private List<Event> events = new ArrayList<>();

    public User() {}

    public static User parseJsonData(JSONObject response) throws JSONException, ParseException {
        User result = new User();

        //JSONObject jsonObject = (JSONObject) response.get("");
        result.id = response.getLong("id");
        result.email = response.getString("email");
        result.name = response.getString("name");
        result.avatar = response.getString("avatar");
        result.about = response.getString("about");
        if (response.has("myEvents")) {
            JSONArray myEventsArray = response.getJSONArray("myEvents");
            for(int i = 0; i < myEventsArray.length(); i++) {
                result.myEvents.add(Event.parseJsonData(myEventsArray.getJSONObject(i)));
            }
        }
        if (response.has("events")) {
            JSONArray eventsArray = response.getJSONArray("events");
            for(int i = 0; i < eventsArray.length(); i++) {
                result.events.add(Event.parseJsonData(eventsArray.getJSONObject(i)));
            }
        }

        return result;
    }

    public static JSONObject getJsonData(User user) throws JSONException {
        JSONObject result = new JSONObject();

        result.put("email", user.getEmail());
        result.put("name", user.getName());
        result.put("avatar", user.getAvatar());
        result.put("about", user.getAbout());

        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", about='" + about + '\'' +
                ", myEvents=" + myEvents +
                ", events=" + events +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<Event> getMyEvents() {
        return myEvents;
    }

    public void setMyEvents(List<Event> myEvents) {
        this.myEvents = myEvents;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
