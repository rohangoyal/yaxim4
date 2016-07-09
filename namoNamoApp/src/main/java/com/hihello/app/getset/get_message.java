package com.hihello.app.getset;

/**
 * Created by rohan on 6/16/2016.
 */
public class get_message
{
    String group_id,group_name,message;

    public get_message(String group_id, String group_name, String message) {
        this.group_id = group_id;
        this.group_name = group_name;
        this.message = message;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
