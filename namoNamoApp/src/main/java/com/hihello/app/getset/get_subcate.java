package com.hihello.app.getset;

/**
 * Created by rohan on 5/23/2016.
 */
public class get_subcate
{
    int id;
    String cat_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    String subcategory;

    public get_subcate(int id, String cat_id, String subcategory) {
        this.id = id;
        this.cat_id = cat_id;
        this.subcategory = subcategory;
    }
}
