package com.example.ravi.hiltonadmin1;

import android.app.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();
        System.out.println("token refreshed");
        sendTokenToServer(token);
    }

    public void sendTokenToServer(String token){
        FirebaseDatabase.getInstance().getReference("AdminData").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("FirebaseToken").setValue(token);
    }
}
