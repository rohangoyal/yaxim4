package com.hihello.app.getset;

/**
 * Created by rohan on 5/30/2016.
 */
public class get_join
{
    int id;
    String profile_name;
    String group_name;
    String group_id;

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String pic_url;
    String date;

    public get_join(int id, String group_name, String profile_name, String group_id,String pic_url,String date) {
        this.id = id;
        this.group_name = group_name;
        this.profile_name = profile_name;
        this.group_id = group_id;
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

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }
}
