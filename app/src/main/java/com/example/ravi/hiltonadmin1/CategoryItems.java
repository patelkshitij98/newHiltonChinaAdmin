package com.example.ravi.hiltonadmin1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class CategoryItems extends AppCompatActivity {
    private String Title;
    private android.support.v7.widget.Toolbar toolbar;
    private RecyclerView rItemList;
    private DatabaseReference databaseReference;
    private ArrayList<Items> arrayList;
    private static ItemListAdapter itemListAdapter;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);
        Bundle extras= getIntent().getExtras();
        if(extras != null)
        {
            Title = extras.getString("Title");

        }
        arrayList = new ArrayList<>();
        rItemList=findViewById(R.id.rItemList);
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(Title);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        databaseReference= FirebaseDatabase.getInstance().getReference("ItemData").child(Title);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count=0;
                arrayList.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {

                    final String ItemCategory = Title;
                    final String ItemId = data.getKey();
                    final String ItemName = data.child("Name").getValue(String.class);
                    final String Desc = data.child("Desc").getValue(String.class);
                    final String ItemPrice = data.child("Price").getValue(String.class);
                    final String ItemNumber ="-1";
                    final String ImageUrl = data.child("Image").getValue(String.class);
                    count++;
                    Items item= new Items(ItemId,ItemName,ItemCategory,ItemNumber,Desc,ItemPrice,ImageUrl);
                    arrayList.add(item);

                    if(count==dataSnapshot.getChildrenCount())
                    {
                         itemListAdapter = new ItemListAdapter(CategoryItems.this,arrayList);
                        rItemList.setAdapter(itemListAdapter);
                        rItemList.addItemDecoration(new DividerItemDecoration(CategoryItems.this,DividerItemDecoration.VERTICAL));
                        rItemList.setLayoutManager(new LinearLayoutManager(CategoryItems.this));
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }


}
