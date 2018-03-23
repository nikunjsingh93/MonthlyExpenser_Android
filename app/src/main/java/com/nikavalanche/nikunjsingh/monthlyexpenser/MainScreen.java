package com.nikavalanche.nikunjsingh.monthlyexpenser;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class MainScreen extends AppCompatActivity implements
        View.OnClickListener {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private DatabaseReference databaseReference;

    private EditText editAddMEsp;

    private EditText howMuch;

    private Button addEspBtn;

    private Button showPrev;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        databaseReference = FirebaseDatabase.getInstance().getReference();



        editAddMEsp = (EditText) findViewById(R.id.addExpenseText);

        howMuch = (EditText) findViewById(R.id.howMuchText);



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


    }



    private void addExpense() {

        String addexp = editAddMEsp.getText().toString().trim();

        String howMuchTemp = howMuch.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();


        DatabaseReference postsRef = databaseReference.child(user.getUid()).child("AllExpenses");


        postsRef.push().setValue(new InputDetailsPojo(addexp, howMuchTemp));


        Toast.makeText(this,"Information Added", Toast.LENGTH_SHORT).show();


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
