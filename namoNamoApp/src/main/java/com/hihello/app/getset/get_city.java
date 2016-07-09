package com.hihello.app.getset;

/**
 * Created by rohan on 5/23/2016.
 */
public class get_city
{
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    String state_id,city;

    public get_city(int id, String state_id, String city) {
        this.id = id;
        this.state_id = state_id;
        this.city = city;
    }
}
