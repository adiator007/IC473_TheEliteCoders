package com.example.smartpds;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class DistributorKycRegister extends AppCompatActivity implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 234;

    //view objects
    private Button aadharCardChoose, panCardChoose, shopLicenseChoose, doneButton;
    private Button aadharCardUpload, panCardUpload, shopLicencseUpload;

    //uri to store file
    private Uri filePath;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseProducts;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_kyc_register);
        mobile = getIntent().getStringExtra("mobile");
        shopLicenseChoose = (Button) findViewById(R.id.shopchoose);

        aadharCardChoose = (Button) findViewById(R.id.buttonChooseAadhar);
        panCardChoose = (Button) findViewById(R.id.buttonChoose_pan);
        aadharCardUpload = (Button) findViewById(R.id.buttonUploadadhar);
        panCardUpload = (Button) findViewById(R.id.buttonUploadPan);
        shopLicencseUpload = (Button) findViewById(R.id.shopupload);
        doneButton = (Button) findViewById(R.id.done);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference("DistributorKYC").child(mobile);
        mDatabaseProducts = FirebaseDatabase.getInstance().getReference("DistributorsProducts").child(mobile);

        shopLicenseChoose.setOnClickListener(this);
        aadharCardChoose.setOnClickListener(this);
        panCardChoose.setOnClickListener(this);
        aadharCardUpload.setOnClickListener(this);
        panCardUpload.setOnClickListener(this);
        shopLicencseUpload.setOnClickListener(this);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProducts();
                Intent intent = new Intent(DistributorKycRegister.this, DistributorDashBoard.class);
                intent.putExtra("mobile",mobile);
                startActivity(intent);
            }
        });
    }

    private void updateProducts() {

        String productRecordIDFromServer = mDatabaseProducts.push().getKey();

        // Adding the both name and number values using student details class object using ID.
        mDatabaseProducts.child("rava").child("quantity").setValue("2");
        mDatabaseProducts.child("rava").child("price").setValue(20);
        mDatabaseProducts.child("rava").child("img").setValue("https://meetthetaste.files.wordpress.com/2019/02/img-20190205-wa00072035697822075996542.jpg");
        String productRecordIDFromServer2 = mDatabaseProducts.push().getKey();
        mDatabaseProducts.child("peanuts").child("quantity").setValue("5");
        mDatabaseProducts.child("peanuts").child("price").setValue(10);
        mDatabaseProducts.child("wheat").child("quantity").setValue("5");
        mDatabaseProducts.child("wheat").child("price").setValue(10);
        mDatabaseProducts.child("rice").child("quantity").setValue("8");
        mDatabaseProducts.child("rice").child("price").setValue(9);
        mDatabaseProducts.child("oil").child("quantity").setValue("20");
        mDatabaseProducts.child("oil").child("price").setValue(15);
    }

    private void showFileChooserAadhar() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void showFileChooserPan() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void showFileChooserShopLicense() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == aadharCardChoose) {
            showFileChooserAadhar();
        } else if (view == aadharCardUpload) {
            uploadFileAadhar();
        } else if (view == panCardChoose) {
            showFileChooserPan();
        } else if (view == panCardUpload) {
            uploadFilePan();
        } else if (view == shopLicenseChoose) {
            showFileChooserShopLicense();
        } else if (view == shopLicencseUpload) {
            uploadFileShopLicense();
        }


    }



    private void uploadFileAadhar() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            final StorageReference sRef = storageReference.child("" + mobile + "/" + "aadharcard." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();
                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "AdharCard Uploaded ", Toast.LENGTH_LONG).show();

                            //creating the upload object to store uploaded image details
                            UploadKycDistributor upload = new UploadKycDistributor("" + mobile + " Aadhar Card", downloadUrl.toString());

                            //adding an upload to firebase database
//                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child("aadharcard").setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }

    private void uploadFilePan() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            final StorageReference sRef = storageReference.child("" + mobile + "/" + "pancard." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();
                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "PanCard Uploaded ", Toast.LENGTH_LONG).show();

                            //creating the upload object to store uploaded image details
                            UploadKycDistributor upload = new UploadKycDistributor("" + mobile + " Pan Card", downloadUrl.toString());

                            //adding an upload to firebase database

                            mDatabase.child("pancard").setValue(upload);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }

    private void uploadFileShopLicense() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            final StorageReference sRef = storageReference.child("" + mobile + "/" + "shoplicense." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();
                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "ShopLicence Uploaded ", Toast.LENGTH_LONG).show();

                            //creating the upload object to store uploaded image details
                            UploadKycDistributor upload = new UploadKycDistributor("" + mobile + " Shop License Card", downloadUrl.toString());

                            //adding an upload to firebase database
//                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child("shoplicense").setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                          //  Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
