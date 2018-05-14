package com.example.ravi.hiltonadmin1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        View view = inflator.inflate(R.layout.categories_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tItemTitle.setText(arrayList.get(position).getItemName());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder
    {
        LinearLayout lItemRow;
        TextView tItemTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            lItemRow = itemView.findViewById(R.id.lCatRow);
            tItemTitle = itemView.findViewById(R.id.tCaRowTitle);

        }
    }
}
