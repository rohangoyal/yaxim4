package com.hihello.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hihello.app.getset.get_category;
import com.hihello.app.getset.get_city;
import com.hihello.app.getset.get_message;
import com.hihello.app.getset.get_messages;
import com.hihello.app.getset.get_set_groups;
import com.hihello.app.getset.get_state;
import com.hihello.app.getset.get_subcate;
import com.hihello.app.getset.getsetjoin;


import java.util.ArrayList;


/**
 * Created by rohan on 1/6/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "hihello.db";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table state " +
                        "(id integer primary key,stateid text,statename text)");

        db.execSQL(
                "create table city " +
                        "(id integer primary key,stateid text,cityname text)");

        db.execSQL(
                "create table category " +
                        "(id integer primary key,category_name text)");

        db.execSQL(
                "create table subcategory " +
                        "(id integer primary key,cat_id text ,subcat text)");
        db.execSQL(
                "create table joinerchat " +
                        "(group_name text,msg text ,joiner_name text,joiner_pic text,type text)");
        db.execSQL(
                "create table groupchat " +
                        "(id integer primary key,group_id text ,group_name text,msg text,type text,joiner_name text,joiner_pic text)");

        db.execSQL(
                "create table groups " +
                        "(id integer primary key,admin_name text ,group_name text,about text,alt_mobile text,category text,subcategory text,email text,weburl text,state text,city text,address text,date text,admin_pic text,group_pic text,title text,comment text,follow text,profile_name text,admin_pich text,group_pich text)");
        db.execSQL(
                "create table joingroups " +
                        "(id integer primary key,admin_name text ,group_name text,about text,alt_mobile text,category text,subcategory text,email text,weburl text,state text,city text,address text,date text,admin_pic text,group_pic text,title text,comment text,follow text,profile_name text,admin_pich text,group_pich text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }
    public void insertstate  (int id, String stateid, String statename)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("stateid", stateid);
        contentValues.put("statename",statename);
        db.insert("state", null, contentValues);
    }

    public void insertgroupchat  (String group_id,String group_name ,String msg,String type,String joiner_name,String joiner_pic)
    {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("group_id", group_id);
        contentValues.put("group_name",group_name);
        contentValues.put("msg",msg);
        contentValues.put("type",type);
        contentValues.put("joiner_name", joiner_name);
        contentValues.put("joiner_pic",joiner_pic);
      long n=  db.insert("groupchat", null, contentValues);

        Log.e("ereeeeeee",n+"");
    }
    public void deleteGroupChat(String group_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        // db.delete("delete from orderlist where id= "+'"id"', null);
        android.util.Log.e("delete from groupchat where group_name =",group_name);
        db.rawQuery("delete from groupchat where group_name ='"+group_name+"'",null);
        android.util.Log.e("delete successfull","delete");
        db.delete("groupchat", "group_name = ?", new String[]{group_name});
        db.close();


    }

    public void deleteJoinerChat(String group_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        // db.delete("delete from orderlist where id= "+'"id"', null);
        Log.e("delete from joinerchat where group_name =",group_name);
        db.rawQuery("delete from joinerchat where group_name ='"+group_name+"'",null);
        Log.e("delete successfull","delete");
        db.delete("joinerchat", "group_name = ?", new String[]{group_name});
        db.close();


    }

    public void insertcity  (int id, String stateid, String cityname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("stateid", stateid);
        contentValues.put("cityname",cityname);
        db.insert("city", null, contentValues);
    }
    public void insertjoinerchat  (String group_name,String joiner_name, String msg,String joiner_pic,String type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("group_name", group_name);
        contentValues.put("msg",msg);
        contentValues.put("joiner_name", joiner_name);
        contentValues.put("joiner_pic",joiner_pic);
        contentValues.put("type",type);
        db.insert("joinerchat", null, contentValues);
    }
    public void insertgroup  (int id, String admin_name, String group_name, String about,String alt_mobile,String category,String subcategory,String email,String weburl,String state,String city,String address,String date,String admin_pic,String group_pic,String title,String comment,String follow,String profile_name,String admin_pich,String group_pich)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("admin_name", admin_name);
        contentValues.put("group_name",group_name);
        contentValues.put("about", about);

        contentValues.put("alt_mobile",alt_mobile);
        contentValues.put("category", category);
        contentValues.put("subcategory",subcategory);
        contentValues.put("email", email);
        contentValues.put("weburl",weburl);
        contentValues.put("state", state);
        contentValues.put("city",city);
        contentValues.put("address", address);
        contentValues.put("date",date);
        contentValues.put("admin_pic", admin_pic);
        contentValues.put("group_pic",group_pic);
        contentValues.put("title", title);
        contentValues.put("comment",comment);
        contentValues.put("follow", follow);
//        Log.e("profile_name",profile_name);
        contentValues.put("profile_name",profile_name);
        contentValues.put("admin_pich", admin_pich);
        contentValues.put("group_pich",group_pich);
        db.insert("groups", null, contentValues);
    }
    public void insertjoingroup  (int id, String admin_name, String group_name, String about,String alt_mobile,String category,String subcategory,String email,String weburl,String state,String city,String address,String date,String admin_pic,String group_pic,String title,String comment,String follow,String profile_name,String admin_pich,String group_pich)
    {
        SQLiteDatabase db = this.getWritableDatabase();
try {
    ContentValues contentValues = new ContentValues();
    contentValues.put("id", id);
    contentValues.put("admin_name", admin_name);
    contentValues.put("group_name", group_name);
    contentValues.put("about", about);
    contentValues.put("alt_mobile", alt_mobile);
    contentValues.put("category", category);
    contentValues.put("subcategory", subcategory);
    contentValues.put("email", email);
    contentValues.put("weburl", weburl);
    contentValues.put("state", state);
    contentValues.put("city", city);
    contentValues.put("address", address);
    contentValues.put("date", date);
    contentValues.put("admin_pic", admin_pic);
    contentValues.put("group_pic", group_pic);
    contentValues.put("title", title);
    contentValues.put("comment", comment);
    contentValues.put("follow", follow);
    contentValues.put("profile_name", profile_name);
    contentValues.put("admin_pich", admin_pich);
    contentValues.put("group_pich", group_pich);

    Log.e("profile_name_insert", contentValues + "");

    db.insert("joingroups", null, contentValues);
}
catch (Exception e)
{
    Log.e("error",e.getMessage()+"");
    Log.e("exception",Log.getStackTraceString(e)+"");

}
    }


    public void insertcategory  (int id,String category_name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("category_name",category_name);

        db.insert("category", null, contentValues);
    }

    public void insertsubcategory  (int id,String cat_id,String subcat)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("cat_id", cat_id);
        contentValues.put("subcat", subcat);

        db.insert("subcategory", null, contentValues);
    }

    public void updatejoingroup  (String admin_name, String group_name, String about,String alt_mobile,String category,String subcategory,String email,String weburl,String state,String city,String address,String date,String admin_pic,String group_pic,String title,String comment,String follow,String profile_name,String admin_pich,String group_pich)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("admin_name", admin_name);
        contentValues.put("group_name",group_name);
        contentValues.put("about", about);
        contentValues.put("alt_mobile",alt_mobile);
        contentValues.put("category", category);
        contentValues.put("subcategory",subcategory);
        contentValues.put("email", email);
        contentValues.put("weburl",weburl);
        contentValues.put("state", state);
        contentValues.put("city",city);
        contentValues.put("address", address);
        contentValues.put("date",date);
        contentValues.put("admin_pic", admin_pic);
        contentValues.put("group_pic",group_pic);
        contentValues.put("title", title);
        contentValues.put("comment",comment);
        contentValues.put("follow", follow);
        contentValues.put("profile_name",profile_name);
        contentValues.put("admin_pich", admin_pich);
        contentValues.put("group_pich",group_pich);
        db.update("joingroups", contentValues, "group_name = ? ", new String[]{group_name});

    }

    public ArrayList<get_message> getAllMessage()
    {
        ArrayList<get_message> array_list = new ArrayList<get_message>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        String id="false";
        Cursor res =  db.rawQuery( "select * from category", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            array_list.add(new get_message(res.getString(0),
                            res.getString(1),res.getString(2))
            );
            res.moveToNext();
        }

        return array_list;
    }


    public ArrayList<get_messages> getGroupMessage(String group_id)
    {
        ArrayList<get_messages> array_list = new ArrayList<get_messages>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        String id="false";
        Cursor res =  db.rawQuery( "select * from groupchat where group_name='"+group_id+"'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(new get_messages(res.getString(3),
                    res.getString(4))
            );
            res.moveToNext();
        }

        return array_list;
    }

    public ArrayList<getsetjoin> getJoinerMessage(String group_name)
    {
        ArrayList<getsetjoin> array_list = new ArrayList<getsetjoin>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        String id="false";
        Cursor res =  db.rawQuery( "select * from joinerchat where group_name='"+group_name+"'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            array_list.add(new getsetjoin(res.getString(0),res.getString(1),res.getString(2),
                    res.getString(3))
            );
            res.moveToNext();
        }

        return array_list;
    }

    public ArrayList<get_category> getAllCategory()
    {
        ArrayList<get_category> array_list = new ArrayList<get_category>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        String id="false";
        Cursor res =  db.rawQuery( "select * from category", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            array_list.add(new get_category(res.getInt(0),
                            res.getString(1))
            );
            res.moveToNext();
        }

        return array_list;
    }

    public ArrayList<get_set_groups> getAllGroups()
    {
        ArrayList<get_set_groups> array_list = new ArrayList<get_set_groups>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        String id="false";
        Cursor res =  db.rawQuery( "select * from groups", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            array_list.add(new get_set_groups(res.getInt(0),
                            res.getString(1),res.getString(2),
                            res.getString(3),res.getString(4),
                            res.getString(5),res.getString(6),
                            res.getString(7),res.getString(8),
                            res.getString(9),res.getString(10),
                            res.getString(11),res.getString(12),
                            res.getString(13),res.getString(14),
                            res.getString(15),res.getString(16),
                            res.getString(17),res.getString(18),
                    res.getString(19),res.getString(20))
            );
            res.moveToNext();
        }

        return array_list;
    }


    public ArrayList<get_subcate> getAllSubCategory()
    {
        ArrayList<get_subcate> array_list = new ArrayList<get_subcate>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        String id="false";
        Cursor res =  db.rawQuery( "select * from subcategory", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            array_list.add(new get_subcate(res.getInt(0),
                            res.getString(1),res.getString(2))
            );
            res.moveToNext();
        }

        return array_list;
    }
    public ArrayList<get_state> getAllState()
    {
        ArrayList<get_state> array_list = new ArrayList<get_state>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        String id="false";
        Cursor res =  db.rawQuery( "select * from state", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            array_list.add(new get_state(res.getInt(0),
                            res.getString(1),res.getString(2))
            );
            res.moveToNext();
        }

        return array_list;
    }
    public ArrayList<get_city> getAllCity()
    {
        ArrayList<get_city> array_list = new ArrayList<get_city>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        String id="false";
        Cursor res =  db.rawQuery( "select * from city", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            array_list.add(new get_city(res.getInt(0),
                            res.getString(1),res.getString(2))
            );
            res.moveToNext();
        }

        return array_list;
    }





//    public void updatecheckout (int id, String username, String password, String productname, String price, String quantity, String total,String paymentstatus)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("username", username);
//        contentValues.put("password", password);
//        contentValues.put("productname", productname);
//        contentValues.put("price", price);
//        contentValues.put("quantity", quantity);
//        contentValues.put("total", total);
//        contentValues.put("paymentstatus", paymentstatus);
//        db.update("checkoutorderlist", contentValues, "id = ? ", new String[]{Integer.toString(id)});
//    }
//
//
//    public void insertbalance (int id, String username, String password, String total, String pay, String balance)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("id", id);
//        contentValues.put("username", username);
//        contentValues.put("password", password);
//        contentValues.put("total", total);
//        contentValues.put("pay", pay);
//        contentValues.put("balance", balance);
//        db.insert("balance", null, contentValues);
//    }
//
//    public void updatebalance (int id, String username, String password, String total, String pay, String balance)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("username", username);
//        contentValues.put("password", password);
//        contentValues.put("total", total);
//        contentValues.put("pay", pay);
//        contentValues.put("balance", balance);
//        db.update("balance", contentValues, "id = ? ", new String[]{Integer.toString(id)});
//    }
//
//    public void updatecategory  (int id,String category_id,String category,int quantity)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("category_id", category_id);
//        contentValues.put("category", category);
//        contentValues.put("quantity", quantity);
//        db.update("categories", contentValues, "id = ? ", new String[]{Integer.toString(id)});
//       // return true;
//    }
//
//    public void updatequantitycategory  (int id,int quantity)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("quantity", quantity);
//        db.update("categories", contentValues, "id = ? ", new String[]{Integer.toString(id)});
//        // return true;
//    }
//
//    public void updatesubsubcategory  (int id, String product_id, String subcat_id, String name, String description, String size, String numberofbages, String pcase, String numberofcase, String price, String vat,int quantity)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("product_id", product_id);
//        contentValues.put("subcat_id", subcat_id);
//        contentValues.put("name", name);
//        contentValues.put("description", description);
//        contentValues.put("size", size);
//        contentValues.put("numberofbages", numberofbages);
//        contentValues.put("pcase", pcase);
//        contentValues.put("numberofcase", numberofcase);
//        contentValues.put("price", price);
//        contentValues.put("vat", vat);
//        contentValues.put("quantity", quantity);
//        db.update("subsubcategories1", contentValues, "id = ? ", new String[]{Integer.toString(id)});
//        //return true;
//    }
//
//    public void updatequantitysubsubcategory  (int id,int quantity)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("quantity", quantity);
//        db.update("subsubcategories1", contentValues, "id = ? ", new String[]{Integer.toString(id)});
//        //return true;
//    }


    //    public ArrayList<get_detail> getAllUser()
//    {
//        ArrayList<get_detail> array_list = new ArrayList<get_detail>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        Cursor res =  db.rawQuery( "select * from login", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            array_list.add(new get_detail(res.getInt(0),
//                            res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9),res.getString(10),res.getString(11),res.getString(12),res.getString(13),res.getString(14))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//


    public ArrayList<get_set_groups> getAllJoinGroup()
    {
        ArrayList<get_set_groups> array_list = new ArrayList<get_set_groups>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        String id="false";
        Cursor res =  db.rawQuery( "select * from joingroups", null );
        Log.e("count",res.getCount()+"");
        res.moveToFirst();

        while(res.isAfterLast() == false){

            array_list.add(new get_set_groups(res.getInt(0),
                    res.getString(1),res.getString(2),
                    res.getString(3),res.getString(4),
                    res.getString(5),res.getString(6),
                    res.getString(7),res.getString(8),
                    res.getString(9),res.getString(10),
                    res.getString(11),res.getString(12),
                    res.getString(13),res.getString(14),
                    res.getString(15),res.getString(16),
                    res.getString(17),res.getString(18),
                    res.getString(19),res.getString(20)));
Log.e("alltotal",res.getInt(0)+" , "+
        res.getString(1)+" , "+res.getString(2)+" , "+
        res.getString(3)+" , "+res.getString(4)+" , "+
        res.getString(5)+" , "+res.getString(6)+" , "+
        res.getString(7)+" , "+res.getString(8)+" , "+
        res.getString(9)+" , "+res.getString(10)+" , "+
        res.getString(11)+" , "+res.getString(12)+" , "+
        res.getString(13)+" , "+res.getString(14)+" , "+
        res.getString(15)+" , "+res.getString(16)+" , "+
        res.getString(17)+" , "+res.getString(18)+" , "+
        res.getString(19)+" , "+res.getString(20));
            Log.e("profile_name_select",res.getString(19));

            res.moveToNext();
        }

        return array_list;
    }


    public ArrayList<get_set_groups> getAllUserGroup(String username)
    {
        ArrayList<get_set_groups> array_list = new ArrayList<get_set_groups>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        String id="false";
        Cursor res =  db.rawQuery( "select * from groups where profile_name='"+username+"'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            array_list.add(new get_set_groups(res.getInt(0),
                            res.getString(1),res.getString(2),
                            res.getString(3),res.getString(4),
                            res.getString(5),res.getString(6),
                            res.getString(7),res.getString(8),
                            res.getString(9),res.getString(10),
                            res.getString(11),res.getString(12),
                            res.getString(13),res.getString(14),
                            res.getString(15),res.getString(16),
                            res.getString(17),res.getString(18),
                    res.getString(19),res.getString(20))
            );
            res.moveToNext();
        }

        return array_list;
    }





//    public ArrayList<get_detail> getAllUser(String username)
//    {
//        ArrayList<get_detail> array_list = new ArrayList<get_detail>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        Cursor res =  db.rawQuery( "select * from login where username='"+username+"'", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            array_list.add(new get_detail(res.getInt(0),
//                            res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9),res.getString(10),res.getString(11),res.getString(12),res.getString(13),res.getString(14))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//
//    public ArrayList<get_cat> getAllCategory()
//    {
//        ArrayList<get_cat> array_list = new ArrayList<get_cat>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        Cursor res =  db.rawQuery( "select * from categories", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            array_list.add(new get_cat(res.getInt(0),
//                            res.getString(1),res.getString(2),res.getInt(3))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//
    public ArrayList<get_subcate> getSelectedAllSubCategory(String cat_id)
    {
        ArrayList<get_subcate> array_list = new ArrayList<get_subcate>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        String id="false";
        Cursor res =  db.rawQuery( "select * from subcategory where cat_id='"+cat_id+"'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            array_list.add(new get_subcate(res.getInt(0),
                            res.getString(1),res.getString(2))
            );
            res.moveToNext();
        }

        return array_list;
    }


    public ArrayList<get_city> getSelectedAllCity(String cat_id)
    {
        ArrayList<get_city> array_list = new ArrayList<get_city>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        String id="false";
        Cursor res =  db.rawQuery( "select * from city where stateid='"+cat_id+"'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            array_list.add(new get_city(res.getInt(0),
                            res.getString(1),res.getString(2))
            );
            res.moveToNext();
        }

        return array_list;
    }


//
//    public ArrayList<get_subcat> getAllSubCategory()
//    {
//        ArrayList<get_subcat> array_list = new ArrayList<get_subcat>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        Cursor res =  db.rawQuery( "select * from subcategories", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            array_list.add(new get_subcat(res.getInt(0),
//                            res.getString(1),res.getString(2))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//
//    public ArrayList<get_subcat> getSelectSubCategory(String cat_id)
//    {
//        ArrayList<get_subcat> array_list = new ArrayList<get_subcat>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
////        Log.e("cat_id", cat_id);
//        Cursor res =  db.rawQuery( "select * from subcategories where subcat_id='"+cat_id+"'", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            array_list.add(new get_subcat(res.getInt(0),
//                            res.getString(1),res.getString(2))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//
//
//
//
//    public ArrayList<get_subsubcat> getAllSubSubCategory()
//    {
//        ArrayList<get_subsubcat> array_list = new ArrayList<get_subsubcat>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        Cursor res =  db.rawQuery( "select * from subsubcategories1", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            Log.e("data",res.getString(2));
//
//            array_list.add(new get_subsubcat(res.getInt(0),
//                            res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9),res.getString(10),res.getInt(11),res.getString(12))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//
//    public ArrayList<get_subsubcat> getAllAccessSubCategory(String scat_id)
//    {
//        ArrayList<get_subsubcat> array_list = new ArrayList<get_subsubcat>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        Log.e("subsubcate","select * from subsubcategories1 where subcat_id='"+scat_id+"'");
//        Cursor res =  db.rawQuery( "select * from subsubcategories1 where subcat_id='"+scat_id+"'", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            Log.e("data",res.getString(2));
//
//            array_list.add(new get_subsubcat(res.getInt(0),
//                            res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8),res.getString(9),res.getString(10),res.getInt(11),res.getString(12))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//
//    public ArrayList<get_subsubcat> getSelectSubSubCategory(String scat_id)
//    {
//
//        ArrayList<get_subsubcat> array_list = new ArrayList<get_subsubcat>();
//        try
//
//        {
//            //hp = new HashMap();
//            SQLiteDatabase db = this.getReadableDatabase();
//            String id = "false";
//
//            scat_id=scat_id.trim();
//
//            Log.e("subsubcate","select * from subsubcategories1 where subcat_id='"+scat_id+"'");
//            Cursor res = db.rawQuery("select * from subsubcategories1 where subcat_id='"+scat_id+"'", null);
//
//            Log.e("records",res.getCount()+"");
//            res.moveToFirst();
//
//            while (res.isAfterLast() == false) {
//
//                array_list.add(new get_subsubcat(res.getInt(0),
//                                res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6), res.getString(7), res.getString(8), res.getString(9), res.getString(10),res.getInt(11),res.getString(12))
//                );
//
//
//                Log.e("subsubcategores11111", res.getString(3));
//                res.moveToNext();
//
//
//            }
//
//        }
//
//        catch(Exception e){
//
//
//            Log.e("e","e",e);
//        }
//
//        return array_list;
//    }
//
//    public ArrayList<data> getAllOrder()
//    {
//        ArrayList<data> array_list = new ArrayList<data>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        Cursor res =  db.rawQuery( "select * from orderlist", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            array_list.add(new data(res.getInt(0),
//                            res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//
//    public ArrayList<data> getUserAllOrder(String username,String pass)
//    {
//        ArrayList<data> array_list = new ArrayList<data>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        Cursor res =  db.rawQuery( "select * from orderlist where username='"+username+"' AND password='"+pass+"'", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            array_list.add(new data(res.getInt(0),
//                            res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//
//
//
//    public ArrayList<get_checkout> getAllCheckout()
//    {
//        ArrayList<get_checkout> array_list = new ArrayList<get_checkout>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        Cursor res =  db.rawQuery( "select * from checkoutorderlist", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            array_list.add(new get_checkout(res.getInt(0),
//                            res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//
//    public ArrayList<get_checkout> getAllCheckout(String username,String pass)
//    {
//        ArrayList<get_checkout> array_list = new ArrayList<get_checkout>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        Cursor res =  db.rawQuery( "select * from checkoutorderlist where username='"+username+"' AND password='"+pass+"'", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            array_list.add(new get_checkout(res.getInt(0),
//                            res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5),res.getString(6),res.getString(7),res.getString(8))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//    public ArrayList<get_userbal> getAllUserbal()
//    {
//        ArrayList<get_userbal> array_list = new ArrayList<get_userbal>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        Cursor res =  db.rawQuery( "select * from checkoutorderlist", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            array_list.add(new get_userbal(res.getInt(0),
//                            res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//
//    public ArrayList<get_userbal> getAllUserbal(String username,String pass)
//    {
//        ArrayList<get_userbal> array_list = new ArrayList<get_userbal>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        Cursor res =  db.rawQuery( "select * from balance where username='"+username+"' AND password='"+pass+"'", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            array_list.add(new get_userbal(res.getInt(0),
//                            res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//
//    public ArrayList<get_cat> getAllDrinkCategory()
//    {
//        ArrayList<get_cat> array_list = new ArrayList<get_cat>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        String scat_id="Drinks";
//        Log.e("subsubcate","select * from categories where category_id='"+scat_id+"'");
//        Cursor res =  db.rawQuery( "select * from categories where category_id='"+scat_id+"'", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            array_list.add(new get_cat(res.getInt(0),
//                            res.getString(1),res.getString(2),res.getInt(3))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//
//    public ArrayList<get_cat> getAllSnacksCategory()
//    {
//        ArrayList<get_cat> array_list = new ArrayList<get_cat>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        String scat_id="Snacks";
//        Log.e("subsubcate","select * from categories where category_id='"+scat_id+"'");
//        Cursor res =  db.rawQuery( "select * from categories where category_id='"+scat_id+"'", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            array_list.add(new get_cat(res.getInt(0),
//                            res.getString(1),res.getString(2),res.getInt(3))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//
//    public ArrayList<get_cat> getAllAccessoriesCategory()
//    {
//        ArrayList<get_cat> array_list = new ArrayList<get_cat>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String id="false";
//        String scat_id="Accessories";
//        Log.e("subsubcate","select * from categories where category_id='"+scat_id+"'");
//        Cursor res =  db.rawQuery( "select * from categories where category_id='"+scat_id+"'", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//
//            array_list.add(new get_cat(res.getInt(0),
//                            res.getString(1),res.getString(2),res.getInt(3))
//            );
//            res.moveToNext();
//        }
//
//        return array_list;
//    }
//
//
//
    // Deleting single contact
    public void deleteGroup(String group_name) {
        SQLiteDatabase db = this.getWritableDatabase();
       // db.delete("delete from orderlist where id= "+'"id"', null);
        Log.e("delete from groups where group_name =",group_name+"kkkkk");
        db.rawQuery("delete from joingroups where group_name ='"+group_name+"'",null);
        Log.e("delete successfull","delete");
       db.delete("joingroups", "group_name = ?", new String[]{group_name});
        db.close();


    }


}
