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
        final String FireToken = bundle.getString("FirebaseToken");
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positive =  ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText  eUsername = ((Dialog)dialog).findViewById(R.id.edelUsername);
                        EditText eContact = ((Dialog) dialog).findViewById(R.id.edelContact);
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

                            //Intantiate the RequestQueue.
                            RequestQueue queue = Volley.newRequestQueue((Context)getActivity());
                            String url = "https://us-central1-hiltonchina-6351c.cloudfunctions.net/sendOrderAccepted";

                            //Request a string response from the provided URL.
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText((Context) getActivity(), "String Response",Toast.LENGTH_SHORT);
                                    dialog.dismiss();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String,String> params = new HashMap<String,String>();
                                    params.put("FirebaseToken",FireToken);


                                    return params;
                                }
                            };
                            queue.add(stringRequest);



                        }
                    }
                });
            }
        });

        return dialog;
    }
}
