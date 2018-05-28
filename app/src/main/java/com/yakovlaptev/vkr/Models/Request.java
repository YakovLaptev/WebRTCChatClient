package com.yakovlaptev.vkr.Models;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;

public class Request implements Serializable {

    private Long id;
    private Event event;
    private User user;

    public Request() {}

    public static Request parseJsonData(JSONObject response) throws JSONException, ParseException {
        Request result = new Request();

        result.id = response.getLong("id");
        result.event = Event.parseJsonData(response.getJSONObject("event"));
        result.user = User.parseJsonData(response.getJSONObject("user"));

        return result;
    }

    public static JSONObject getJsonData(Request request) throws JSONException {
        JSONObject result = new JSONObject();

        result.put("event", Event.getJsonData(request.getEvent()));
        result.put("user", User.getJsonData(request.getUser()));

        return result;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", event=" + event +
                ", user=" + user +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
