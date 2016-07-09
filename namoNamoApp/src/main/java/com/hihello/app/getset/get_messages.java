package com.hihello.app.getset;

/**
 * Created by delaine on 7/7/2016.
 */
public class get_messages
{
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String msg;

    public get_messages(String msg, String type) {
        this.msg = msg;
        this.type = type;
    }

    String type;
}
