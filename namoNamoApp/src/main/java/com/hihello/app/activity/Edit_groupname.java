package com.hihello.app.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.hihello.app.R;

/**
 * Created by rohan on 5/7/2016.
 */
public class Edit_groupname extends AppCompatActivity
{
    SharedPreferences pref;
    SharedPreferences.Editor pref_edit;
    private EditText edit_name;
    String pt="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_name);
        pref=getSharedPreferences("pref",1);
        edit_name=(EditText)findViewById(R.id.edit_name);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftback);
        getSupportActionBar().setTitle("Edit Group Information");

        Intent in=getIntent();
        pt=in.getStringExtra("parameter");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.status_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.post:
                String name = edit_name.getText().toString();
                if (name.trim().length() == 0) {
                    Toast.makeText(Edit_groupname.this,"Text should not be blank.",Toast.LENGTH_SHORT).show();
                    break;
                }
                else
                {
                    pref_edit=pref.edit();

                    if(pt.equals("adminimg"))
                        pref_edit.putString("admin",name);
                    else if(pt.equals("groupimg"))
                        pref_edit.putString("group",name);
                    else if(pt.equals("aboutimg"))
                        pref_edit.putString("about",name);
                    else if(pt.equals("alternateimg"))
                        pref_edit.putString("alternatephone",name);
                    else if(pt.equals("maincateimg"))
                        pref_edit.putString("main",name);
                    else if(pt.equals("subcatimg"))
                        pref_edit.putString("sub",name);
                    else if(pt.equals("emailimg"))
                        pref_edit.putString("email",name);
                    else if(pt.equals("websiteimg"))
                        pref_edit.putString("website",name);
                    else if(pt.equals("stateimg"))
                        pref_edit.putString("state",name);
                    else if(pt.equals("cityimg"))
                        pref_edit.putString("city",name);
                    else if(pt.equals("addressimg"))
                        pref_edit.putString("address",name);
                    else if(pt.equals("titleimg"))
                        pref_edit.putString("title",name);

                    pref_edit.commit();
                    Intent i=new Intent(Edit_groupname.this,Create_group.class);
                    i.putExtra("class","edit_groupname");
                    startActivity(i);
                    finish();
                }
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
    @Override
    public void onBackPressed() {
        Intent i=new Intent(Edit_groupname.this,Create_group.class);
        startActivity(i);
        finish();
    }
}
