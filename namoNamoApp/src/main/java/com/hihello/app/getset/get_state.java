package com.hihello.app.getset;

/**
 * Created by rohan on 5/23/2016.
 */
public class get_state
{
    int id;
    String cid,state;

    public get_state(int id, String cid, String state) {
        this.id = id;
        this.cid = cid;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
