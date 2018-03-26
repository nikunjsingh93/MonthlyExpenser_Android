package com.nikavalanche.nikunjsingh.monthlyexpenser;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

    private ArrayList<InputDetailsPojo> list = new ArrayList<>();


    private FirebaseAuth mAuth;

    private InputDetailsPojo post;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);




        mAuth = FirebaseAuth.getInstance();


        final FirebaseUser user = mAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();


        DatabaseReference dbList = databaseReference.child(user.getUid()).child("AllExpenses");




        listItemsView = (ListView) findViewById(R.id.listItemsView);


        final ArrayAdapter <String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,userArrayList);

        listItemsView.setAdapter(arrayAdapter);





        dbList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                post = dataSnapshot.getValue(InputDetailsPojo.class);



                post.setKey(dataSnapshot.getKey());






                userArrayList.add(post.getReason()+ "\n" + post.getDateTime()+"\n"+ post.getMoney()+"$");

                list.add(post);

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





        listItemsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(ListItems.this);
                adb.setTitle("Delete!");
                adb.setMessage("Are you sure you want to delete ?");
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);

                final InputDetailsPojo model = list.get(positionToRemove);

                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        userArrayList.remove(positionToRemove);

                        databaseReference.child(user.getUid()).child("AllExpenses").child(model.key).removeValue();


                        arrayAdapter.notifyDataSetChanged();
                    }});
                adb.show();
            }
        });



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
