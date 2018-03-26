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

public class InputDetails extends AppCompatActivity implements
        View.OnClickListener {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private DatabaseReference databaseReference;

    private EditText editTextMonthlyEst;

    private Button continueMontlyEst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_details);






        databaseReference = FirebaseDatabase.getInstance().getReference();

        editTextMonthlyEst = (EditText) findViewById(R.id.editTextMonthlyEst);

        continueMontlyEst = (Button) findViewById(R.id.continueMonthlyButton);

        continueMontlyEst.setOnClickListener(this);



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


    private void saveMonthlyEst() {

        String monthlyEstTxt = editTextMonthlyEst.getText().toString().trim();


        FirebaseUser user = mAuth.getCurrentUser();



        DatabaseReference postsRef = databaseReference.child(user.getUid()).child("MonthlyEstimate");


        if(monthlyEstTxt.equalsIgnoreCase("")) {



            Toast.makeText(this,"Please Enter Detail", Toast.LENGTH_SHORT).show();


        } else {


            postsRef.setValue(new MonthlyEstimatePojo(monthlyEstTxt));


            Toast.makeText(this,"Information Added", Toast.LENGTH_SHORT).show();


            Intent myIntent = new Intent(InputDetails.this, MainScreen.class);
            InputDetails.this.startActivity(myIntent);

        }




    }

    private void updateUI(FirebaseUser user) {

            Intent myIntent = new Intent(InputDetails.this, SignIn.class);
        InputDetails.this.startActivity(myIntent);


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
        } else if (v == continueMontlyEst) {
            saveMonthlyEst();

        }
    }


}
