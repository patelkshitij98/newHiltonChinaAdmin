package com.example.ravi.hiltonadmin1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DeliveryFragment extends DialogFragment {

    public DeliveryFragment() {

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delivery Details");
        builder.setView(R.layout.accept_order);
        builder.setPositiveButton("Done", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        Bundle bundle = getArguments();
        final String OrderId = bundle.getString("OrderId");
        final String UserId = bundle.getString("UserId");
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positive =  ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText  eUsername = ((Dialog)dialog).findViewById(R.id.edelUsername);
                        final EditText eContact = ((Dialog) dialog).findViewById(R.id.edelContact);
                        if(eUsername.getText().toString().isEmpty())
                        {
                            eUsername.setError("Cannot be empty");

                        }
                        else if( eContact.toString().isEmpty())
                        {
                            eContact.setError("Cannot be empty");
                        }
                        else
                        {

                           //Push data to Firebase Database
                            FirebaseDatabase.getInstance().getReference("UserData/"+UserId+"/Orders/"+OrderId).child("Delivery").child("DeliveryBoy").setValue(eUsername.getText().toString());
                            FirebaseDatabase.getInstance().getReference("UserData/"+UserId+"/Orders/"+OrderId).child("Delivery").child("DeliverContact").setValue(eContact.getText().toString());
                            FirebaseDatabase.getInstance().getReference("Orders/"+OrderId+"/Progress").setValue("Accepted");

                            dialog.dismiss();



                        }
                    }
                });
            }
        });

        return dialog;
    }
}
