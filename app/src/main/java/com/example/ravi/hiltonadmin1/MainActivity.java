package com.example.ravi.hiltonadmin1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /***function to add Food******/
    public void AddFood(View view)
    {
        Intent i=new Intent(this,AddFood.class);
        startActivity(i);
    }
    /***Signout User**/
    public void SignOut(View v)
    {
        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        Intent i=new Intent(this,SignIn.class);
        startActivity(i);
        finish(); //to destroy this activity
    }
}
