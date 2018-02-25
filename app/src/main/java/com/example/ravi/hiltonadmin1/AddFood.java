package com.example.ravi.hiltonadmin1;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class AddFood extends AppCompatActivity {

    private ImageButton ibFoodImage;
    private EditText eItemName;
    private EditText eItemDescription;
    private EditText eItemPrice;
    private Button bAddFood;
    private static final int GALLREQ=1;
    private Uri uri=null;
    private StorageReference storageReference=null;
    private DatabaseReference mRef;
    private FirebaseDatabase firebaseDatabase;
    private Spinner sCategory;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        ibFoodImage=(ImageButton)findViewById(R.id.ibFoodImage);

        eItemName=(EditText)findViewById(R.id.eItemName);
        eItemDescription=(EditText)findViewById(R.id.eItemDescription);
        eItemPrice=(EditText)findViewById(R.id.eItemPrice);

        bAddFood=(Button)findViewById(R.id.bAddFood);

        //firebase variables
        storageReference=FirebaseStorage.getInstance().getReference("Item");
        mRef=FirebaseDatabase.getInstance().getReference("ItemData");

        //Read Text from Spinner and create spinner
        sCategory=(Spinner)findViewById(R.id.sCategory);
        final List<String> Categories = new ArrayList<String>();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot categorySnapshot: dataSnapshot.getChildren())
                {
                    Categories.add(categorySnapshot.getKey().toString());
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(AddFood.this, android.R.layout.simple_spinner_item, Categories);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sCategory.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }

    public void FoodImage(View view)
    {
        Intent intent=new Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Selectpictures"),GALLREQ);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLREQ&&resultCode==RESULT_OK)
        {
            uri=data.getData();
            ibFoodImage.setImageURI(uri);
            ViewGroup.LayoutParams params=ibFoodImage.getLayoutParams();
            params.height= ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width= ViewGroup.LayoutParams.WRAP_CONTENT;

        }
    }

    public void AddItem(View view)
    {

        final String String_Name=eItemName.getText().toString().trim();
        final String String_Description=eItemDescription.getText().toString().trim();
        final String String_Price=eItemPrice.getText().toString().trim();



        if(TextUtils.isEmpty(String_Name))
        {
            eItemName.setError("Cannot be empty");
        }

        else if(TextUtils.isEmpty(String_Description))
        {
            eItemDescription.setError("Cannot be empty");
        }

        else if(TextUtils.isEmpty(String_Price))
        {
            eItemPrice.setError("Cannot be empty");
        }



        else
        {
            ibFoodImage.setEnabled(false);
            eItemName.setEnabled(false);
            bAddFood.setEnabled(false);
            eItemDescription.setEnabled(false);
            eItemPrice.setEnabled(false);

            if(ibFoodImage.getDrawable()==null)
            {
                final DatabaseReference newPost=mRef.child(sCategory.getSelectedItem().toString()).push();
                newPost.child("Name").setValue(String_Name);
                newPost.child("Desc").setValue(String_Description);
                newPost.child("Price").setValue(String_Price);
                newPost.child("Image").setValue(null);
            }
            else
            {
                StorageReference filepath = storageReference.child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Uri downloadurl = taskSnapshot.getDownloadUrl();

                        final DatabaseReference newPost=mRef.child(sCategory.getSelectedItem().toString()).push();
                        newPost.child("Name").setValue(String_Name);
                        newPost.child("Desc").setValue(String_Description);
                        newPost.child("Price").setValue(String_Price);
                        newPost.child("Image").setValue(downloadurl.toString());


                    }
                });
            }

            Toast.makeText(AddFood.this,"Item Uploaded Uploaded",Toast.LENGTH_LONG).show();
            finish();




        }
    }
}

