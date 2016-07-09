package com.hihello.app.getset;

/**
 * Created by rohan on 5/20/2016.
 */
public class get_allgroup
{
    int id;
    String admin_name;
    String group_name;

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    String about;
    String alt_mob;
    String cate;
    String subcate;
    String email;
    String web_url;
    String state;
    String city;
    String address;
    String admin_pic;
    String group_pic;

    public String getAdmin_pich() {
        return admin_pich;
    }

    public void setAdmin_pich(String admin_pich) {
        this.admin_pich = admin_pich;
    }

    public String getGroup_pich() {
        return group_pich;
    }

    public void setGroup_pich(String group_pich) {
        this.group_pich = group_pich;
    }

    String admin_pich;
    String group_pich;

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    String follow;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String date;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    String comment;

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    String profile_name;

    public get_allgroup(int id, String admin_name, String group_name, String about, String alt_mob, String cate, String subcate, String email, String web_url, String state, String city, String address, String admin_pic, String group_pic,String admin_pich,String group_pich,String title,String comment,String date,String follow,String profile_name) {
        this.id = id;
        this.admin_name = admin_name;
        this.group_name = group_name;
        this.about = about;
        this.alt_mob = alt_mob;
        this.cate = cate;
        this.subcate = subcate;
        this.email = email;
        this.web_url = web_url;
        this.state = state;
        this.city = city;
        this.address = address;
        this.admin_pic = admin_pic;
        this.group_pic = group_pic;
        this.admin_pich=admin_pich;
        this.group_pich=group_pich;
        this.title = title;
        this.comment = comment;
        this.date=date;
        this.follow=follow;
        this.profile_name=profile_name;
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

    public String getAlt_mob() {
        return alt_mob;
    }

    public void setAlt_mob(String alt_mob) {
        this.alt_mob = alt_mob;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getSubcate() {
        return subcate;
    }

    public void setSubcate(String subcate) {
        this.subcate = subcate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
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
}
