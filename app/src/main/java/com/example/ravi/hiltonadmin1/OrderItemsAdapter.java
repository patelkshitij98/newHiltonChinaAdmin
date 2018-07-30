package com.example.ravi.hiltonadmin1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Items> OrderItems;
    private LayoutInflater Inflator;

    public OrderItemsAdapter(Context context, ArrayList<Items> orderItems) {
        this.context = context;
        this.OrderItems = orderItems;
        Inflator = LayoutInflater.from(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = Inflator.inflate(R.layout.item_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Items i = OrderItems.get(position);
        holder.tItemName.setText(i.getItemName());
        holder.tItemPrice.setText(i.getItemPrice());
        holder.tItemDesc.setText(i.getItemCategory());
        holder.tItemNumber.setText(i.getItemNumber());
        Picasso.with(context).load(i.getImageUrl()).placeholder(R.drawable.ravi).into(holder.iItemImage);



    }

    @Override
    public int getItemCount() {
        return OrderItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tItemName;
        private TextView tItemPrice;
        private TextView tItemDesc;
        private TextView tItemNumber;
        private ImageView iItemImage;
        public ViewHolder(View itemView) {
            super(itemView);
            tItemName = itemView.findViewById(R.id.tItemName);
            tItemNumber = itemView.findViewById(R.id.tItemNumber);
            tItemPrice = itemView.findViewById(R.id.tItemPrice);
            tItemDesc = itemView.findViewById(R.id.tItemDescription);
            iItemImage = itemView.findViewById(R.id.iItemImage);

        }
    }
}
