package com.nikavalanche.nikunjsingh.monthlyexpenser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewAll extends AppCompatActivity {


    private TextView viewAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);


        Bundle bundle = getIntent().getExtras();
        ArrayList<String> array = bundle.getStringArrayList("array");



        viewAll = (TextView) findViewById(R.id.viewAll);



        StringBuilder builder = new StringBuilder();
        for (String details : array) {
            builder.append(details + "\n");
        }

        viewAll.setText(builder.toString());



    }
}
