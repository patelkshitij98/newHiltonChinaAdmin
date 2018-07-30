package com.example.ravi.hiltonadmin1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class OrderItems extends AppCompatActivity {
    private ArrayList<Items> OrderItems;
    private RecyclerView rOrderItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);

        Intent i = getIntent();
        OrderItems = i.getParcelableArrayListExtra("OrderItems");
        rOrderItems = findViewById(R.id.rOrderItems);
        rOrderItems.setLayoutManager(new LinearLayoutManager(this));
        rOrderItems.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rOrderItems.setAdapter(new OrderItemsAdapter(this, OrderItems));




    }
}
