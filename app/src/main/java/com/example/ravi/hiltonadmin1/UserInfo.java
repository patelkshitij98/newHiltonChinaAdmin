package com.example.ravi.hiltonadmin1;

import android.content.Intent;
import android.icu.lang.UCharacterEnums;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class UserInfo extends AppCompatActivity {

    private final String PHONE="Phone";
    private final String USERNAME="UserName";
    private final String EMAIL="Email";
    private EditText eUsername;
    private EditText eEmailAddress;
    private Button SignUp;
    private DatabaseReference AdminDataRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);


        eUsername=(EditText)findViewById(R.id.eUsername);
        eEmailAddress=(EditText)findViewById(R.id.eEmailAddress);
        SignUp=(Button)findViewById(R.id.bSignUp);
        AdminDataRef= FirebaseDatabase.getInstance().getReference("AdminData");

    }

    public void SignUp(View view)
    {
        Intent intent=getIntent();
        Bundle extras=intent.getExtras();
        String String_UserId=extras.getString("UserId");
        String String_Phone=extras.getString("PhoneNumber");
        String String_Username=eUsername.getText().toString().trim();
        String String_Email=eEmailAddress.getText().toString().trim();

        if(TextUtils.isEmpty(String_Username))
        {
            eUsername.setError("Username cannot be Empty");
        }

        else if(TextUtils.isEmpty(String_Email))
        {
            eEmailAddress.setError("Email cannot be Empty");
        }

        else
        {

            AdminDataRef.child(String_UserId).child(USERNAME).setValue(String_Username);
            AdminDataRef.child(String_UserId).child(EMAIL).setValue(String_Email);
            AdminDataRef.child(String_UserId).child(PHONE).setValue(String_Phone);
            FirebaseDatabase.getInstance().getReference("AdminData").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FirebaseToken").setValue(FirebaseInstanceId.getInstance().getToken());
            subscribeToOrdersTopic();


            finish();



        }
    }

    public void subscribeToOrdersTopic()
    {
        FirebaseMessaging.getInstance().subscribeToTopic("Orders");
    }
}
