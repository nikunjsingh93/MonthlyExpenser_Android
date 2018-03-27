package com.nikavalanche.nikunjsingh.monthlyexpenser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Settings extends AppCompatActivity implements
        View.OnClickListener {


    private Button goTo;

    private Button viewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        goTo = (Button) findViewById(R.id.gotochangelimitBtn);

        goTo.setOnClickListener(this);



        viewAll = (Button) findViewById(R.id.viewallbtn);

        viewAll.setOnClickListener(this);



    }



    public void onClick(View v) {
        int i = v.getId();
       if (v == goTo) {
           gotoFunc();
        } else if (v == viewAll) {
           viewAllDataFunc();
       }
    }



    private void gotoFunc() {



        Intent myIntent = new Intent(Settings.this, ChangeLimit.class);
        Settings.this.startActivity(myIntent);


    }



    private void viewAllDataFunc() {


        Bundle bundle = getIntent().getExtras();
        ArrayList<String> array = bundle.getStringArrayList("array");



        Intent myIntent = new Intent(Settings.this, ViewAll.class);

        myIntent.putExtra("array", array);
        Settings.this.startActivity(myIntent);



    }




}
