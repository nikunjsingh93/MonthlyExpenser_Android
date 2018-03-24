package com.nikavalanche.nikunjsingh.monthlyexpenser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ListItems extends AppCompatActivity  implements
        View.OnClickListener  {


    private Button backList;

    private DatabaseReference databaseReference;

    private ListView listItemsView;

    private ArrayList<String> userArrayList = new ArrayList<>();

    private FirebaseAuth mAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);




        mAuth = FirebaseAuth.getInstance();


        FirebaseUser user = mAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();


        DatabaseReference dbList = databaseReference.child(user.getUid()).child("AllExpenses");




        listItemsView = (ListView) findViewById(R.id.listItemsView);


        final ArrayAdapter <String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,userArrayList);

        listItemsView.setAdapter(arrayAdapter);





        dbList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                InputDetailsPojo post = dataSnapshot.getValue(InputDetailsPojo.class);


                userArrayList.add(post.getReason()+ "\n" + post.getDateTime()+"\n"+ post.getMoney()+"$");

                arrayAdapter.notifyDataSetChanged();



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        backList = (Button) findViewById(R.id.backList);

        backList.setOnClickListener(this);



    }




    private void goToMain() {

        Intent myIntent = new Intent(ListItems.this, MainScreen.class);
        ListItems.this.startActivity(myIntent);

    }


    public void onClick(View v) {
        int i = v.getId();
        if (v == backList) {
            goToMain();
        }
    }



}
