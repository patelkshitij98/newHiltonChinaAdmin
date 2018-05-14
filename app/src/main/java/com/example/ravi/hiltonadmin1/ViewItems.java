package com.example.ravi.hiltonadmin1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewItems extends AppCompatActivity {

    private RecyclerView rCategories;
    private ArrayList<String> arrayList =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        rCategories= findViewById(R.id.rCategories);

        arrayList=new ArrayList<String>(); // arraylist for adding all categories
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("ItemData");

        rCategories.setLayoutManager(new LinearLayoutManager(ViewItems.this));
        rCategories.addItemDecoration(new DividerItemDecoration(ViewItems.this,DividerItemDecoration.VERTICAL));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int count =0;
                for(DataSnapshot data:dataSnapshot.getChildren())
                {

                    count++;
                    arrayList.add(data.getKey().toString());


                    /**** setting up recycler view ****/
                        if(count==dataSnapshot.getChildrenCount()) {
                            CategoriesRecyclerAdapter categoriesRecyclerAdapter = new CategoriesRecyclerAdapter(ViewItems.this, arrayList);
                            rCategories.setAdapter(categoriesRecyclerAdapter);
                        }


                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
