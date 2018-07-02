package com.example.ravi.hiltonadmin1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;


    public ViewOrderAdapter(Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.order_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tOrderId;
        private TextView tUsername;
        private TextView tAddress;
        private TextView tPaid;
        private Button bAccepted;
        private Button bDelivered;
        private Button bDispute;
        private ImageView iStatus;
        private ImageView iPayment;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
