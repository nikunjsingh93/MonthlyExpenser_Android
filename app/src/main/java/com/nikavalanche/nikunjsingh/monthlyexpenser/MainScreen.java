package com.nikavalanche.nikunjsingh.monthlyexpenser;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainScreen extends AppCompatActivity implements
        View.OnClickListener {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private DatabaseReference databaseReference;

    private EditText editAddMEsp;

    private EditText howMuch;

    private Button addEspBtn;

    private Button showPrev;

    private TextView amountRem;

    private TextView MonthlyLimitTextView;

    private TextView amountRemToSpend;

    private Double temp1;

    private Double temp2;




    private ArrayList<Double> allAmounts = new ArrayList<>();


    private InputDetailsPojo post;


    public ArrayList<String> userArrayList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);






//
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
//



        databaseReference = FirebaseDatabase.getInstance().getReference();





        editAddMEsp = (EditText) findViewById(R.id.addExpenseText);

        howMuch = (EditText) findViewById(R.id.howMuchText);


        amountRem = (TextView) findViewById(R.id.amountRem);

        MonthlyLimitTextView = (TextView) findViewById(R.id.MonthlyLimitText);

        amountRemToSpend = (TextView) findViewById(R.id.amountRemToSpend);



        addEspBtn = (Button) findViewById(R.id.add);

        addEspBtn.setOnClickListener(this);


        showPrev = (Button) findViewById(R.id.showPrev);

        showPrev.setOnClickListener(this);




//        findViewById(R.id.sign_out_button).setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]



        FirebaseUser user = mAuth.getCurrentUser();




        DatabaseReference mLimit = databaseReference.child(user.getUid()).child("MonthlyEstimate");






        mLimit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                MonthlyEstimatePojo post = dataSnapshot.getValue(MonthlyEstimatePojo.class);


                userArrayList.add("Monthly Estimate:" + post.getMonthlyEstimate());


                userArrayList.add("Your Spending List:");


                MonthlyLimitTextView.setText(post.getMonthlyEstimate());

                temp1 = Double.parseDouble(post.getMonthlyEstimate());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







        DatabaseReference dbList = databaseReference.child(user.getUid()).child("AllExpenses");







        dbList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                InputDetailsPojo post = dataSnapshot.getValue(InputDetailsPojo.class);





                userArrayList.add(post.getReason()+ "\n" + post.getDateTime()+"\n"+ post.getMoney()+"$");





                allAmounts.add(Double.parseDouble(post.getMoney()));


                Double sum = 0.0;
                for(Double d : allAmounts) {
                    sum += d;
                }


                amountRem.setText(sum.toString());

                userArrayList.add("Amount Spent:" + sum.toString()+"$");

                temp2 = Double.parseDouble(sum.toString());

                amountRemFunc();



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






    }


    private void amountRemFunc() {

        Double temp3 = temp1 - temp2;

        amountRemToSpend.setText(temp3.toString());

        userArrayList.add("Amount that was remaining to spend:" + temp3.toString()+"$");


    }


    private void addExpense() {

        String addexp = editAddMEsp.getText().toString().trim();

        String howMuchTemp = howMuch.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();



        Date todaysDate = new Date();

        DateFormat formattedDate = new SimpleDateFormat("E, MMM dd yyyy, KK:mm a");

        String DateTimeInString = formattedDate.format(todaysDate);



        DatabaseReference postsRef = databaseReference.child(user.getUid()).child("AllExpenses");

        if(addexp.equalsIgnoreCase("") || howMuchTemp.equalsIgnoreCase("")) {



            Toast.makeText(this,"Please Enter All Details", Toast.LENGTH_SHORT).show();


        } else {


                postsRef.push().setValue(new InputDetailsPojo(addexp, howMuchTemp,DateTimeInString));


                Toast.makeText(this,"Information Added", Toast.LENGTH_SHORT).show();

            editAddMEsp.setText("");
            howMuch.setText("");

        }




    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){


        getMenuInflater().inflate(R.menu.menu_main,menu);


        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();


        switch (id){

            case R.id.action_settings:

                Intent myIntent = new Intent(MainScreen.this, Settings.class);

                myIntent.putExtra("array", userArrayList);
                MainScreen.this.startActivity(myIntent);



                break;

            case R.id.action_signout:



                signOut();




                break;


        }


        return true;
    }





    private void goToList() {

        Intent myIntent = new Intent(MainScreen.this, ListItems.class);
        MainScreen.this.startActivity(myIntent);

    }




    private void updateUI(FirebaseUser user) {

        Intent myIntent = new Intent(MainScreen.this, SignIn.class);
        MainScreen.this.startActivity(myIntent);


    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });

    }


    public void onClick(View v) {
        int i = v.getId();
       if (v == addEspBtn) {
            addExpense();
        } else if (v == showPrev) {
            goToList();
        }
    }




}
