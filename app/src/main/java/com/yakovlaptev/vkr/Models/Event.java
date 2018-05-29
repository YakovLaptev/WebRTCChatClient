package com.yakovlaptev.vkr.Models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Event implements Serializable {

    private Long id;

    private String name;

    private String about;

    private Date date;

    private User creator;

    private List<User> users = new ArrayList<>();

    public Event() {
    }

    public static Event parseJsonData(JSONObject response) throws JSONException, ParseException {
        Event result = new Event();

        result.id = response.getLong("id");
        result.name = response.getString("name");
        result.about = response.getString("about");
        result.date = new Date(response.getLong("date"));
        result.creator = new User();
        result.creator.setName("test");
        result.creator.setEmail("test@test.ru");
        if(!response.isNull(("creator"))) {
            result.creator = User.parseJsonData(response.getJSONObject("creator"));
        }
        JSONArray usersArray = response.getJSONArray("users");
        for (int i = 0; i < usersArray.length(); i++) {
            result.users.add(User.parseJsonData(usersArray.getJSONObject(i)));
        }

        return result;
    }

    public static JSONObject getJsonData(Event event) throws JSONException {
        JSONObject result = new JSONObject();

        result.put("name", event.getName());
        result.put("about", event.getAbout());
        result.put("date", event.getDate().getTime());
        result.put("creator", event.getCreator().getId());

        return result;
    }

    @Override
    public String toString() {
        return name +
                "\n" + date +
                "\nAbout: " + about +
                "\nCreator: " + creator.getName() + " : " + creator.getEmail();
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
