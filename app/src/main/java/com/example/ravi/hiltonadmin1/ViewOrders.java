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
    private long countItems=0;
    private long countOrder=0;
    private long TotalOrders=0;
    private long TotalItems=0;
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

                 TotalOrders = dataSnapshotOrder.getChildrenCount();
                for (DataSnapshot childSnapshot : dataSnapshotOrder.getChildren()) /************All Orders******/
                {

                    final String OrderId = childSnapshot.getKey();
                    final String Paid = childSnapshot.child("Paid").getValue(String.class);
                    final String Progress = childSnapshot.child("Progress").getValue(String.class);
                    final String paymentType = childSnapshot.child("PaymentType").getValue(String.class);
                    final String Userid = childSnapshot.child("UserId").getValue(String.class);


                    FirebaseDatabase.getInstance().getReference("UserData/"+Userid).addListenerForSingleValueEvent(new ValueEventListener() { /********Retriving UserData for the order********/
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {

                            final String Address = dataSnapshot.child("Address").getValue(String.class);
                            final String Username = dataSnapshot.child("UserName").getValue(String.class);
                            final String PhoneNumber = dataSnapshot.child("Phone").getValue(String.class);
                            final String registrationToken = dataSnapshot.child("FirebaseToken").getValue(String.class);
                            final ArrayList<Items> ItemList = new ArrayList<Items>();
//                            Toast.makeText(ViewOrders.this,"User Data "+OrderId,Toast.LENGTH_SHORT).show();
                             TotalItems = dataSnapshot.child("Orders").child(OrderId).child("Items").getChildrenCount();

                            for(DataSnapshot ItemsSnapshot : dataSnapshot.child("Orders").child(OrderId).child("Items").getChildren()) /*****ALl Items Data in the Order******/
                            {
                                final String ItemId = ItemsSnapshot.getKey();
                                final String ItemNumber = ItemsSnapshot.child("ItemNumber").getValue(String.class);
                                final String ItemCategory = ItemsSnapshot.child("ItemCategory").getValue(String.class);

                                FirebaseDatabase.getInstance().getReference("ItemData/"+ItemCategory+"/"+ItemId).addListenerForSingleValueEvent(new ValueEventListener() { /****getting Item Data*-***/
                                    @Override
                                    public void onDataChange(DataSnapshot ItemInfo) {
                                        String Image = ItemInfo.child("Image").getValue(String.class);
                                        String ItemName = ItemInfo.child("Name").getValue(String.class);
                                        String ItemDescription = ItemInfo.child("Desc").getValue(String.class);
                                        String ItemPrice = ItemInfo.child("Price").getValue(String.class);
                                        Toast.makeText(ViewOrders.this,"Item Data "+ItemId,Toast.LENGTH_SHORT).show();
                                        Items i = new Items(ItemId,ItemName,ItemCategory,ItemNumber,ItemDescription,ItemPrice,Image);
                                        ItemList.add(i);
                                        countItems++;
                                        System.out.println("Count Items : "+countItems);
                                        if(countItems == TotalItems)
                                        {
                                            Order order = new Order(OrderId,Userid,Username,Address,Paid, PhoneNumber,paymentType,Progress,registrationToken,ItemList);
                                            orders.add(order);
                                            countOrder++;
                                            Toast.makeText(ViewOrders.this,"ItemAdded "+OrderId,Toast.LENGTH_SHORT).show();
                                            countItems=0;
                                        }


                                        if(countOrder == TotalOrders)
                                        {
                                            Toast.makeText(ViewOrders.this,"Adapter",Toast.LENGTH_SHORT).show();
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
