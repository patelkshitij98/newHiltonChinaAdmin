package com.example.ravi.hiltonadmin1;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewOrders extends AppCompatActivity {
    private RecyclerView rViewOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        rViewOrders = findViewById(R.id.rViewOrders);
        rViewOrders.setAdapter(new ViewOrderAdapter(this));
        rViewOrders.setLayoutManager(new LinearLayoutManager(this));
        FirebaseDatabase.getInstance().getReference("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren())
                {
                    String OrderId = childSnapshot.getKey();
                    System.out.println("hello orderId: "+OrderId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
