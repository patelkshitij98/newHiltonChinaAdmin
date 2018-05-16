package com.example.ravi.hiltonadmin1;

import android.content.ClipData;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ItemInfo extends AppCompatActivity {

    public ImageButton ibFoodImage;
    public Items item;
    public EditText eItemName;
    public EditText eItemDesc;
    public EditText eItemPrice;
    private Uri uri=null;
    public Button bSave;
    private boolean ImageChanged=false;
    private static final int GALLREQ=1;
    private StorageReference storageReference=null;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        item = getIntent().getParcelableExtra("Item");

        /***setting initial layout ***/
        ibFoodImage=findViewById(R.id.ibFoodImage);
        eItemName= findViewById(R.id.eItemName);
        eItemDesc = findViewById(R.id.eItemDescription);
        eItemPrice = findViewById(R.id.eItemPrice);

        bSave = findViewById(R.id.bAddFood);
        Picasso.with(this).load(item.getImageUrl()).placeholder(R.drawable.ravi).into(ibFoodImage);
        eItemName.setText(item.getItemName());
        eItemDesc.setText(item.getItemDescription());
        eItemPrice.setText(item.getItemPrice());
        bSave.setText("Save");

        databaseReference = FirebaseDatabase.getInstance().getReference("ItemData").child(item.getItemCategory()).child(item.getItemId());
        storageReference= FirebaseStorage.getInstance().getReference("Item");




        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String String_Name=eItemName.getText().toString().trim();
                final String String_Description=eItemDesc.getText().toString().trim();
                final String String_Price=eItemPrice.getText().toString().trim();



                if(TextUtils.isEmpty(String_Name))
                {
                    eItemName.setError("Cannot be empty");
                }

                else if(TextUtils.isEmpty(String_Description))
                {
                    eItemDesc.setError("Cannot be empty");
                }

                else if(TextUtils.isEmpty(String_Price))
                {
                    eItemPrice.setError("Cannot be empty");
                }

                else if(!ImageChanged)
                {
                    ibFoodImage.setEnabled(false);
                    eItemName.setEnabled(false);
                    bSave.setEnabled(false);
                    eItemDesc.setEnabled(false);
                    eItemPrice.setEnabled(false);

                    final DatabaseReference newPost=databaseReference;
                    newPost.child("Name").setValue(String_Name);
                    newPost.child("Desc").setValue(String_Description);
                    newPost.child("Price").setValue(String_Price);
                    Toast.makeText(ItemInfo.this,"Item Changed Succesfully",Toast.LENGTH_LONG).show();
                    finish();

                }

                else
                {
                    ibFoodImage.setEnabled(false);
                    eItemName.setEnabled(false);
                    bSave.setEnabled(false);
                    eItemDesc.setEnabled(false);
                    eItemPrice.setEnabled(false);


                    StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(item.getImageUrl());
                    photoRef.delete();



                    if(ibFoodImage.getDrawable()==null)
                    {
                        final DatabaseReference newPost=databaseReference;
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

                                final DatabaseReference newPost=databaseReference;
                                newPost.child("Name").setValue(String_Name);
                                newPost.child("Desc").setValue(String_Description);
                                newPost.child("Price").setValue(String_Price);
                                newPost.child("Image").setValue(downloadurl.toString());


                            }
                        });
                    }

                    Toast.makeText(ItemInfo.this,"Item Changed Succesfully",Toast.LENGTH_LONG).show();

                    finish();




                }
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
            ImageChanged=true;

        }
    }
}
