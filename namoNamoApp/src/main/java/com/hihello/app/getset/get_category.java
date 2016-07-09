package com.hihello.app.getset;

/**
 * Created by rohan on 5/23/2016.
 */
public class get_category
{
    int id;
    String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public get_category(int id, String category) {
        this.id = id;
        this.category = category;
    }
}
