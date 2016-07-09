package com.hihello.app.getset;

/**
 * Created by rohan on 5/28/2016.
 */
public class get_createdgroup
{
    int id;
    String admin_name,group_name,about,alt_number,category,subcategory,email,weburl,state,city,address,date,admin_pic,group_pic,title,comment,follow,profile_name;

    public get_createdgroup(int id, String admin_name, String group_name, String about, String alt_number, String category, String subcategory, String email, String weburl, String state, String city, String address, String date, String admin_pic, String group_pic, String title, String comment, String follow, String profile_name) {
        this.id = id;
        this.admin_name = admin_name;
        this.group_name = group_name;
        this.about = about;
        this.alt_number = alt_number;
        this.category = category;
        this.subcategory = subcategory;
        this.email = email;
        this.weburl = weburl;
        this.state = state;
        this.city = city;
        this.address = address;
        this.date = date;
        this.admin_pic = admin_pic;
        this.group_pic = group_pic;
        this.title = title;
        this.comment = comment;
        this.follow = follow;
        this.profile_name = profile_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAlt_number() {
        return alt_number;
    }

    public void setAlt_number(String alt_number) {
        this.alt_number = alt_number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdmin_pic() {
        return admin_pic;
    }

    public void setAdmin_pic(String admin_pic) {
        this.admin_pic = admin_pic;
    }

    public String getGroup_pic() {
        return group_pic;
    }

    public void setGroup_pic(String group_pic) {
        this.group_pic = group_pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }
}
