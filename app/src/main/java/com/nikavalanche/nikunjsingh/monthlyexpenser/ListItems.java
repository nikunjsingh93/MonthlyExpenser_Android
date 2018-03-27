package com.nikavalanche.nikunjsingh.monthlyexpenser;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ListItems extends AppCompatActivity  implements
        View.OnClickListener  {


    private Button backList;

    private DatabaseReference databaseReference;

    private ListView listItemsView;

    public ArrayList<String> userArrayList = new ArrayList<>();






    private ArrayList<InputDetailsPojo> list = new ArrayList<>();


    private FirebaseAuth mAuth;

    private InputDetailsPojo post;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);



//        generateNoteOnSD(this, "MEfile","this is the Body");




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

                adb.setPositiveButton("Okay", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        userArrayList.remove(positionToRemove);

                        databaseReference.child(user.getUid()).child("AllExpenses").child(model.key).removeValue();


                        arrayAdapter.notifyDataSetChanged();
                    }});
                adb.show();
            }
        });



//
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.DAY_OF_MONTH, 1);
//        System.out.println(c.getTime());
//
//
//        Date d = new Date();
//
//        if(c.getTime() == d ) {
//
//            for (int i= 0; i <list.size(); i++) {
//
//                InputDetailsPojo model = list.get(i);
//
//                databaseReference.child(user.getUid()).child("AllExpenses").child(model.key).removeValue();
//
//
//            }
//
//
//            databaseReference.child(user.getUid()).child("MonthlyEstimate").child("monthlyEstimate").removeValue();
//
//
//        }
//



    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){


        getMenuInflater().inflate(R.menu.delete,menu);


        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();


        switch (id){

            case R.id.delete_all:




                AlertDialog.Builder adb=new AlertDialog.Builder(ListItems.this);
                adb.setTitle("Delete ALL DATA!");
                adb.setMessage("Are you sure you want to delete ALL DATA? (This Cannot be Undone)");


                adb.setNegativeButton("Cancel", null);



                adb.setPositiveButton("Okay", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {



                        Intent myIntent = new Intent(ListItems.this, InputDetails.class);
                        ListItems.this.startActivity(myIntent);


                        FirebaseUser us = mAuth.getCurrentUser();


                        for (int i= 0; i <list.size(); i++) {

                            InputDetailsPojo model = list.get(i);

                            databaseReference.child(us.getUid()).child("AllExpenses").child(model.key).removeValue();


                        }





                    }});
                adb.show();






                break;


        }


        return true;
    }



//    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
//        try {
//            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
//            if (!root.exists()) {
//                root.mkdirs();
//            }
//            File gpxfile = new File(root, sFileName);
//            FileWriter writer = new FileWriter(gpxfile);
//            writer.append(sBody);
//            writer.flush();
//            writer.close();
//            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//






    private void goToMain() {

        Intent myIntent = new Intent(ListItems.this, MainScreen.class);

//        myIntent.putExtra("array", userArrayList);


        ListItems.this.startActivity(myIntent);




    }


    public void onClick(View v) {
        int i = v.getId();
        if (v == backList) {
            goToMain();
        }
    }



}
