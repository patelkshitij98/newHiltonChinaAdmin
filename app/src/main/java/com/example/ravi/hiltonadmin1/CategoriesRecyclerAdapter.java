package com.example.ravi.hiltonadmin1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class CategoriesRecyclerAdapter extends RecyclerView.Adapter<CategoriesRecyclerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflator;
    private ArrayList<String> arrayList;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;


    public CategoriesRecyclerAdapter(Context context, ArrayList<String> arrayList) {
        inflator= LayoutInflater.from(context);
        this.arrayList= arrayList;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.categories_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        databaseReference = FirebaseDatabase.getInstance().getReference("ItemData");
        storage = FirebaseStorage.getInstance();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tCatRowTitle.setText(arrayList.get(position));

        holder.lCatRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                            Intent i = new Intent(context, CategoryItems.class);
                            Bundle bundle =new Bundle();
                            bundle.putString("Title",arrayList.get(position));
                            i.putExtras(bundle);
                            context.startActivity(i);
                            ((Activity)context).finish();

                    }



        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout lCatRow;
        TextView tCatRowTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            lCatRow = itemView.findViewById(R.id.lCatRow);
            tCatRowTitle = itemView.findViewById(R.id.tCaRowTitle);

        }
    }
}
