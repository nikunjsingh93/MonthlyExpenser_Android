package com.nikavalanche.nikunjsingh.monthlyexpenser;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

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




        findViewById(R.id.sign_out_button).setOnClickListener(this);


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


//       String tp= mLimit.getKey();
//
//
//        MonthlyLimitTextView.setText(tp);



        DatabaseReference dbList = databaseReference.child(user.getUid()).child("AllExpenses");




        mLimit.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                MonthlyEstimatePojo post = dataSnapshot.getValue(MonthlyEstimatePojo.class);

                MonthlyLimitTextView.setText(post.getMonthlyEstimate());

                temp1 = Double.parseDouble(post.getMonthlyEstimate());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        mLimit.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
//
//
//                MonthlyEstimatePojo post = dataSnapshot.getValue(MonthlyEstimatePojo.class);
//
//
//                MonthlyLimitTextView.setText(post.getMonthlyEstimate());
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        dbList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



                InputDetailsPojo post = dataSnapshot.getValue(InputDetailsPojo.class);


                allAmounts.add(Double.parseDouble(post.getMoney()));


                Double sum = 0.0;
                for(Double d : allAmounts) {
                    sum += d;
                }


                amountRem.setText(sum.toString());

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


    }


    private void addExpense() {

        String addexp = editAddMEsp.getText().toString().trim();

        String howMuchTemp = howMuch.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();



        Date todaysDate = new Date();

        DateFormat formattedDate = new SimpleDateFormat("E, MMM dd yyyy, KK:mm a");

        String DateTimeInString = formattedDate.format(todaysDate);



        DatabaseReference postsRef = databaseReference.child(user.getUid()).child("AllExpenses");


        postsRef.push().setValue(new InputDetailsPojo(addexp, howMuchTemp,DateTimeInString));


        Toast.makeText(this,"Information Added", Toast.LENGTH_SHORT).show();


    }


    private void amountRemaining() {







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
        if (i == R.id.sign_out_button) {
            signOut();
        } else if (v == addEspBtn) {
            addExpense();
        } else if (v == showPrev) {
            goToList();
        }
    }




}
