package com.yakovlaptev.vkr.Models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Event {

    private Long id;

    private String name;

    private String about;

    private Date date;

    private User creator;

    private List<User> users = new ArrayList<>();

    public Event() {}

    public static Event parseJsonData(JSONObject response) throws JSONException, ParseException {
        Event result = new Event();

        //JSONObject jsonObject = (JSONObject) response.get("");
        result.id = response.getLong("id");
        result.name = response.getString("name");
        result.about = response.getString("about");
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        result.date = format.parse(response.getString("date"));
        result.creator = User.parseJsonData(response.getJSONObject("creator"));
        JSONArray usersArray = response.getJSONArray("users");
        for(int i = 0; i < usersArray.length(); i++) {
            result.users.add(User.parseJsonData(usersArray.getJSONObject(i)));
        }

        return result;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
