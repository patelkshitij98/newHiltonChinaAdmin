package com.example.ravi.hiltonadmin1;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class    ViewOrders extends AppCompatActivity {
    private RecyclerView rViewOrders;
    private ArrayList<Order> orders;
    private long TotalOrders=0;
    private long countOrder=0;
    private String order = "Order";
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
                final int countArr[]=new int[1];
                countArr[0]=0;
                 TotalOrders = dataSnapshotOrder.getChildrenCount();
                Log.d(order,"Initial database call for all orders");
                for (DataSnapshot childSnapshot : dataSnapshotOrder.getChildren()) /******All Orders******/
                {

                    final String OrderId = childSnapshot.getKey();
                    final String Amount = childSnapshot.child("Amount").getValue(String.class);
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
                            final long TotalItems[] = new long[1];
                            TotalItems[0] = dataSnapshot.child("Orders").child(OrderId).child("Items").getChildrenCount();
                            final long countItems[]= new long[1];
                            countItems[0]=0;
                            Log.d(order,"TotalItems= "+TotalItems);
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
                                        Items i = new Items(ItemId,ItemName,ItemCategory,ItemNumber,ItemDescription,ItemPrice,Image,true);
                                        ItemList.add(i);
                                        countItems[0]++;
                                        Log.d(order,"countItems "+countItems[0]+" TotalItems "+TotalItems[0]);
                                        if(countItems[0] == TotalItems[0])
                                        {
                                            Order order = new Order(OrderId,Userid,Username,Address,Paid,Amount,PhoneNumber,paymentType,Progress,registrationToken,ItemList);
                                            orders.add(order);
                                            countOrder++;
                                            countItems[0]=0;
                                        }

                                        Log.d(order,"countOrder "+countOrder+" TotalOrders "+TotalOrders);
                                        if(countOrder == TotalOrders)
                                        {
                                            sortOrders(orders);
                                            Log.d(order,"Order Size= "+Integer.toString(orders.size()));
                                            countOrder=0;
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

    public void sortOrders(ArrayList<Order> orders)
    {
        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return Integer.compare(Integer.parseInt(o2.orderId),Integer.parseInt(o1.orderId));
            }
        });
    }
}
