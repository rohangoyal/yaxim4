package com.hihello.app.getset;

/**
 * Created by delaine on 7/3/2016.
 */
public class getsetjoin
{
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

    public String getJoiner_name() {
        return joiner_name;
    }

    public void setJoiner_name(String joiner_name) {
        this.joiner_name = joiner_name;
    }

    public String getJoiner_pic() {
        return joiner_pic;
    }

    public void setJoiner_pic(String joiner_pic) {
        this.joiner_pic = joiner_pic;
    }

    String group_name,joiner_name,joiner_pic,message;

    public getsetjoin(String group_name, String message, String joiner_name, String joiner_pic) {
        this.group_name = group_name;
        this.message = message;
        this.joiner_name = joiner_name;
        this.joiner_pic = joiner_pic;
    }
}
