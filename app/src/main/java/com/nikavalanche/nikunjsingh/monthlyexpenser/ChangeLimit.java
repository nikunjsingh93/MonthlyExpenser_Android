package com.nikavalanche.nikunjsingh.monthlyexpenser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeLimit extends AppCompatActivity implements
        View.OnClickListener {



    private FirebaseAuth mAuth;

    private DatabaseReference databaseReference;


    private Button changeBtn;

    private EditText changeLimiteditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_limit);


        changeBtn = (Button) findViewById(R.id.changeLimitButton);


        changeBtn.setOnClickListener(this);


        changeLimiteditText = (EditText) findViewById(R.id.changeLimiteditText);



        databaseReference = FirebaseDatabase.getInstance().getReference();



        mAuth = FirebaseAuth.getInstance();




        FirebaseUser user = mAuth.getCurrentUser();


        DatabaseReference mLimit = databaseReference.child(user.getUid()).child("MonthlyEstimate");



        mLimit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                MonthlyEstimatePojo post = dataSnapshot.getValue(MonthlyEstimatePojo.class);


                changeLimiteditText.setText(post.getMonthlyEstimate());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



    private void changeMonthlyFunc() {


        String monthlyEstTxt = changeLimiteditText.getText().toString().trim();




        FirebaseUser user = mAuth.getCurrentUser();



        if(monthlyEstTxt.equalsIgnoreCase("")) {



            Toast.makeText(this,"Please Enter Detail", Toast.LENGTH_SHORT).show();


        } else {


            databaseReference.child(user.getUid()).child("MonthlyEstimate").child("monthlyEstimate").setValue(monthlyEstTxt);



            Toast.makeText(this,"Limit Changed", Toast.LENGTH_SHORT).show();




        }




    }




    public void onClick(View v) {
        int i = v.getId();
        if (v == changeBtn) {
            changeMonthlyFunc();

        }
    }







}
