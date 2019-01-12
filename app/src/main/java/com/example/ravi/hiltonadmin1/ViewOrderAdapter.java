package com.example.ravi.hiltonadmin1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Order> orders;
    private String Order = "Order";


    public ViewOrderAdapter(Context context, ArrayList<Order> orders) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.orders = orders;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.order_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Order order = orders.get(position);
        holder.tOrderId.setText("Order ID: "+order.getOrderId());
        holder.tUsername.setText(order.getUserName());
        holder.tAddress.setText(order.getAddress());
        holder.tPhone.setText(order.getPhone());
        holder.tPaid.setText(order.getPaid());


        /***setting initial conditions for progress of order***/
        if(order.getProgress().equals("Accepted"))
        {
            holder.iStatus.setImageResource(R.drawable.accepted_status);
            holder.bAccepted.setEnabled(false);
        }
        else if(order.getProgress().equals("Delivered"))
        {
            holder.iStatus.setImageResource(R.drawable.delivered_status);
            holder.bAccepted.setEnabled(false);
            holder.bCancel.setEnabled(false);
            holder.bDelivered.setEnabled(false);
        }
        else if(order.getProgress().equals("Canceled"))
        {
            holder.iStatus.setImageResource(R.drawable.dispute_status);
            holder.bAccepted.setEnabled(false);
            holder.bCancel.setEnabled(false);
            holder.bDelivered.setEnabled(false);
        }





        //changing status eventlistner
        FirebaseDatabase.getInstance().getReference("Orders/"+order.getOrderId()+"/Progress").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String progress  = dataSnapshot.getValue(String.class);
                if(progress.equals("Accepted"))
                {
                    holder.iStatus.setImageResource(R.drawable.accepted_status);
                    FirebaseDatabase.getInstance().getReference("UserData/"+order.getUserId()+"/Orders/"+order.getOrderId()+"/Progress").setValue("Accepted");
                    holder.bAccepted.setEnabled(false);

                }
                else if(progress.equals("Delivered"))
                {
                    holder.iStatus.setImageResource(R.drawable.delivered_status);
                    FirebaseDatabase.getInstance().getReference("UserData/"+order.getUserId()+"/Orders/"+order.getOrderId()+"/Progress").setValue("Delivered");
                    holder.bAccepted.setEnabled(false);
                    holder.bCancel.setEnabled(false);
                    holder.bDelivered.setEnabled(false);
                }
                else if(progress.equals("Canceled"))
                {
                    holder.iStatus.setImageResource(R.drawable.dispute_status);
                    FirebaseDatabase.getInstance().getReference("UserData/"+order.getUserId()+"/Orders/"+order.getOrderId()+"/Progress").setValue("Canceled");
                    holder.bAccepted.setEnabled(false);
                    holder.bCancel.setEnabled(false);
                    holder.bDelivered.setEnabled(false);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        holder.bAccepted.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DeliveryFragment deliveryFragment = new DeliveryFragment();
                deliveryFragment.show(((Activity)context).getFragmentManager(),"delivery");
                Bundle bundle = new Bundle();
                bundle.putString("OrderId",order.getOrderId());
                bundle.putString("UserId",order.getUserId());
                deliveryFragment.setArguments(bundle);
                //for order data

            }
        });
        holder.bDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for order data
                FirebaseDatabase.getInstance().getReference("Orders/"+order.getOrderId()+"/Progress").setValue("Delivered");
            }
        });


        holder.bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for cancelling or dispute in order.
                FirebaseDatabase.getInstance().getReference("Orders/"+order.getOrderId()+"/Progress").setValue("Canceled");
            }
        });

        holder.bView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,OrderItems.class);
                i.putParcelableArrayListExtra("OrderItems",order.getItemList());
                ((Activity)context).startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {

        Log.d(Order,"Orders size1= "+orders.size());
        return orders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tOrderId;
        private TextView tUsername;
        private TextView tPhone;
        private TextView tAddress;
        private TextView tPaid;
        private Button bAccepted;
        private Button bDelivered;
        private Button bCancel;
        private ImageView iStatus;
        private Button bView;
        private ImageView iPayment;

        public ViewHolder(View itemView) {
            super(itemView);
            bView = itemView.findViewById(R.id.bView);
            tOrderId = itemView.findViewById(R.id.tOrderId);
            tUsername = itemView.findViewById(R.id.tUsername);
            tPhone = itemView.findViewById(R.id.tPhone);
            tAddress = itemView.findViewById(R.id.tAddress);
            tPaid = itemView.findViewById(R.id.tPaid);
            bAccepted = itemView.findViewById(R.id.bAccept);
            bDelivered = itemView.findViewById(R.id.bDelivered);
            bCancel = itemView.findViewById(R.id.bCancel);
            iPayment = itemView.findViewById(R.id.iPayment);
            iStatus = itemView.findViewById(R.id.iStatus);


        }
    }
}
