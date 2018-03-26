package com.nikavalanche.nikunjsingh.monthlyexpenser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity implements
        View.OnClickListener {


    private Button goTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        goTo = (Button) findViewById(R.id.gotochangelimitBtn);

        goTo.setOnClickListener(this);



    }



    public void onClick(View v) {
        int i = v.getId();
       if (v == goTo) {
           gotoFunc();
        }
    }



    private void gotoFunc() {



        Intent myIntent = new Intent(Settings.this, ChangeLimit.class);
        Settings.this.startActivity(myIntent);




    }




}
