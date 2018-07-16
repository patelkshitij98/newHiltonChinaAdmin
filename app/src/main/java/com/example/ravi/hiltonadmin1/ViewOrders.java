package com.example.ravi.hiltonadmin1;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class    ViewOrders extends AppCompatActivity {
    private RecyclerView rViewOrders;
    private ArrayList<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        rViewOrders = findViewById(R.id.rViewOrders);
        orders = new ArrayList<>();
        orders.clear();


        FirebaseDatabase.getInstance().getReference("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshotOrder) {

                int count_order =0;
                for (DataSnapshot childSnapshot : dataSnapshotOrder.getChildren())
                {
                    count_order++;
                    final String OrderId = childSnapshot.getKey();
                    final String Paid = childSnapshot.child("Paid").getValue(String.class);
                    final String Progress = childSnapshot.child("Progress").getValue(String.class);
                    final String paymentType = childSnapshot.child("PaymentType").getValue(String.class);
                    String Userid = childSnapshot.child("UserId").getValue(String.class);

                    final int finalCount_order = count_order;
                    FirebaseDatabase.getInstance().getReference("UserData/"+Userid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String Address = dataSnapshot.child("Address").getValue(String.class);
                            String Username = dataSnapshot.child("Username").getValue(String.class);
                            String PhoneNumber = dataSnapshot.child("Phone").getValue(String.class);
                            String registrationToken = dataSnapshot.child("FirebaseToken").getValue(String.class);
                            Order order = new Order(OrderId,Username,Address,Paid, PhoneNumber,paymentType,Progress,registrationToken);
                            orders.add(order);

                            if(finalCount_order == dataSnapshotOrder.getChildrenCount())
                            {
                                rViewOrders.setAdapter(new ViewOrderAdapter(ViewOrders.this,orders));
                                rViewOrders.setLayoutManager(new LinearLayoutManager(ViewOrders.this));
                                rViewOrders.addItemDecoration(new DividerItemDecoration(ViewOrders.this,DividerItemDecoration.VERTICAL));

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
