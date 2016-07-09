package com.hihello.app.getset;

/**
 * Created by rohan on 5/24/2016.
 */
public class get_comment
{
    int id;
    String group_name;
    String username;
    String comment;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    String pic_url;

    public get_comment(int id, String group_name, String username, String comment,String pic_url,String date) {
        this.id = id;
        this.group_name = group_name;
        this.username = username;
        this.comment = comment;
        this.pic_url=pic_url;
        this.date=date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
