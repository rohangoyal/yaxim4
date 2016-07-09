package com.namonamo.app.getset;

/**
 * Created by user on 11/5/2015.
 */
public class get_setter {
    private String wallimage;
    private String profileimage;
    private String contact;
    private String adminname;
    private String groupname;
    private String email;
    private String phone;
    private String landline;
    private String website;
    private String address;
    private String state;
    private String category;

    public get_setter(String wallimage, String profileimage, String contact, String adminname, String groupname, String email, String phone, String landline, String website, String address, String state, String category, String followers, String viewers) {

        super();
        this.wallimage = wallimage;
        this.profileimage = profileimage;
        this.contact = contact;
        this.adminname = adminname;
        this.groupname = groupname;
        this.email = email;
        this.phone = phone;
        this.landline = landline;
        this.website = website;
        this.address = address;
        this.state = state;
        this.category = category;
        this.followers = followers;
        this.viewers = viewers;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getViewers() {
        return viewers;
    }

    public void setViewers(String viewers) {
        this.viewers = viewers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getWallimage() {
        return wallimage;
    }

    public void setWallimage(String wallimage) {
        this.wallimage = wallimage;
    }

    private String followers;
    private String viewers;

}
