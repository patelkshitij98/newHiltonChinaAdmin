package com.example.ravi.hiltonadmin1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    private LayoutInflater inflator;
    private Context context;
    private ArrayList<Items> arrayList;

    public ItemListAdapter(Context context, ArrayList<Items> arrayList) {
        inflator = LayoutInflater.from(context);
        this.context = context;
        this.arrayList = arrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflator.inflate(R.layout.category_item_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Items  i = arrayList.get(position);
        holder.tItemTitle.setText(arrayList.get(position).getItemName());
        holder.lItemRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,ItemInfo.class);
                i.putExtra("Item",arrayList.get(position));
                context.startActivity(i);
            }
        });

        final boolean avail = i.isAvail();


                if(avail)
                {
                    holder.bItemOption.setBackgroundColor(Color.GREEN);
                    holder.bItemOption.setText("Available");
                }
                else
                {
                    holder.bItemOption.setBackgroundColor(Color.RED);
                    holder.bItemOption.setText("Unavailable");
                }


        holder.bItemOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(avail)
                {
                    FirebaseDatabase.getInstance().getReference("ItemData/"+i.getItemCategory()+"/"+i.getItemId()+"/Avail").setValue(false);
                    i.setAvail(false);

                }
                else
                {
                    FirebaseDatabase.getInstance().getReference("ItemData/"+i.getItemCategory()+"/"+i.getItemId()+"/Avail").setValue(true);
                    i.setAvail(true);

                }
                notifyItemChanged(position,i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder
    {
        LinearLayout lItemRow;
        TextView tItemTitle;
        Button bItemOption;
        public ViewHolder(View itemView) {
            super(itemView);
            lItemRow = itemView.findViewById(R.id.lCatRow);
            tItemTitle = itemView.findViewById(R.id.tCaRowTitle);
            bItemOption = itemView.findViewById(R.id.bItemOption);

        }
    }
}
