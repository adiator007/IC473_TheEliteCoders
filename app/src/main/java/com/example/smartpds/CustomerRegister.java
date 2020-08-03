package com.example.smartpds;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.example.smartpds.model.Customers;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.io.File;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class CustomerRegister extends AppCompatActivity implements Validator.ValidationListener {


    @NotEmpty
    @Length(min = 3, max = 10)
    EditText firstName,lastName;

    @NotEmpty
    @Length(min = 10, max = 10)
    EditText mobileNo;

    @NotEmpty
    @Email
    @Pattern(regex = "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+")
    EditText emailId ;

    @NotEmpty
    @Length(min = 3, max = 100)
    EditText customerAddress ;

    @NotEmpty
    @Length(min = 3, max = 30)
    EditText customerCity ;

    @NotEmpty
    @Length(min = 6, max = 6)
    EditText customerPincode;

    @NotEmpty
    @Length(min = 3, max = 20)
    EditText customerState;


    Button registerButton;
    QRGEncoder qrgEncoder;
    private DatabaseReference mDatabase;
    String url = "https://firebasestorage.googleapis.com/v0/b/crudoperationapp-3b7b0.appspot.com/o/qrbackground%2Fration.jpg?alt=media&token=d813630f-1c13-41a3-a827-ce0b5b13e676";

    Bitmap bitmap;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    static int count=0;
    String mobile;
    String TAG = "GenerateQRCode";
DatabaseReference databaseReference;
Customers customer;
    File file;
    String dirPath, fileName;
String name;
    private StorageReference storageReference;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_register);
        mobile=getIntent().getStringExtra("mobile");
        firstName=findViewById(R.id.fname);
        lastName=findViewById(R.id.lname);
        mobileNo=findViewById(R.id.mobno);
        emailId=findViewById(R.id.email);
        customerAddress=findViewById(R.id.address);
        customerCity=findViewById(R.id.city);
        customerPincode=findViewById(R.id.pincode);
        customerState=findViewById(R.id.state);
        registerButton=findViewById(R.id.register);
        databaseReference= FirebaseDatabase.getInstance().getReference("Customers");
        mDatabase = FirebaseDatabase.getInstance().getReference("KYC").child("CustomerKYC").child(mobile);
        storageReference = FirebaseStorage.getInstance().getReference();
        customer=new Customers();



        validator = new Validator(this);
        validator.setValidationListener(this);

    }
    public void insert(View view){

        validator.validate();
//
//        customer.setFname(firstName.getText().toString().trim());
//        customer.setLname(lastName.getText().toString().trim());
//        customer.setMobile(Long.parseLong(mobileNo.getText().toString().trim()));
//        customer.setEmail(emailId.getText().toString().trim());
//        name=firstName.getText().toString().trim()+lastName.getText().toString().trim();
//        final String mobile=mobileNo.getText().toString().trim();
//        customer.setAddress(customerAddress.getText().toString().trim());
//        customer.setCity(customerCity.getText().toString().trim());
//        customer.setKycDone("no");
//        customer.setWalletAmmount(0);
//        customer.setAccountStatus("pending");
//        customer.setState(customerState.getText().toString().trim());
//        customer.setPincode(Integer.parseInt(customerPincode.getText().toString().trim()));
//
//                databaseReference.child(mobile).setValue(customer);
//                generateDistributorQR();
//                Intent intent=new Intent(CustomerRegister.this,CustomerKycRegister.class);
//                intent.putExtra("mobile",mobile);
//                startActivity(intent);

    }

    private void submit() {

        customer.setFname(firstName.getText().toString().trim());
        customer.setLname(lastName.getText().toString().trim());
        customer.setMobile(Long.parseLong(mobileNo.getText().toString().trim()));
        customer.setEmail(emailId.getText().toString().trim());
        name=firstName.getText().toString().trim()+lastName.getText().toString().trim();
        final String mobile=mobileNo.getText().toString().trim();
        customer.setAddress(customerAddress.getText().toString().trim());
        customer.setCity(customerCity.getText().toString().trim());
        customer.setKycDone("no");
        customer.setWalletAmmount(0);
        customer.setAccountStatus("pending");
        customer.setState(customerState.getText().toString().trim());
        customer.setPincode(Integer.parseInt(customerPincode.getText().toString().trim()));

        databaseReference.child(mobile).setValue(customer);
        generateDistributorQR();
        Intent intent=new Intent(CustomerRegister.this,CustomerKycRegister.class);
        intent.putExtra("mobile",mobile);
        startActivity(intent);

    }


    private void generateDistributorQR() {
        while(count!=1) {
            final StorageReference sRef = storageReference.child("Customers/" + mobileNo.getText() + "/" +"QRCode/"+ name + ".jpg");
            boolean save;
            String result;
        //    Toast.makeText(this, "Image QR Path   " + savePath + name + ".jpg", Toast.LENGTH_SHORT).show();
            try {

                ////////
                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int smallerDimension = width < height ? width : height;
                smallerDimension = smallerDimension * 3 / 4;

                qrgEncoder = new QRGEncoder(
                        mobile, null,
                        QRGContents.Type.TEXT,
                        smallerDimension);
                try {
                    bitmap = qrgEncoder.encodeAsBitmap();
                } catch (WriterException e) {
                    Log.v(TAG, e.toString());
                }

                ///
                AndroidNetworking.initialize(getApplicationContext());
                dirPath = savePath;
                fileName = "ration.jpg";
                file = new File(dirPath, fileName);

                AndroidNetworking.download(url, dirPath, fileName)
                        .build()
                        .startDownload(new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                            //    Toast.makeText(CustomerRegister.this, "DownLoad Complete", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });

                Bitmap background = BitmapFactory.decodeFile(savePath + "ration.jpg");
                Bitmap newbitmap = combineImages(background, bitmap);
                Constants.name = name;
                save = QRGSaver.save(savePath, name, newbitmap, QRGContents.ImageType.IMAGE_JPEG);
                result = save ? "Image Saved" : "Image Not Saved";
             //   Toast.makeText(getApplicationContext(), result + " at " + savePath, Toast.LENGTH_LONG).show();
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Generating QR");
                progressDialog.show();
                File imgFile = new File(savePath + name + ".jpg");
                /////////////////////////////////

                sRef.putFile(Uri.fromFile(imgFile))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                //dismissing the progress dialog
                                progressDialog.dismiss();
                                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!urlTask.isSuccessful()) ;
                                Uri downloadUrl = urlTask.getResult();
                                //displaying success toast
                          //      Toast.makeText(getApplicationContext(), " QR Generated", Toast.LENGTH_LONG).show();

                                //creating the upload object to store uploaded image details
//                                databaseReference.child(mobile).child("qrcodelink").setValue(downloadUrl.toString());
                                //adding an upload to firebase database

                                mDatabase.child("qrlink").setValue(downloadUrl.toString());

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
                                progressDialog.setMessage("Generated QR " + ((int) progress) + "%...");
                            }
                        });
                count++;

                ////////////////////////////////////


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public Bitmap combineImages(Bitmap background, Bitmap foreground) {

        int width = 0, height = 0;
        Bitmap cs;

        width = getWindowManager().getDefaultDisplay().getWidth();
        height = getWindowManager().getDefaultDisplay().getHeight();

        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(cs);
        background = Bitmap.createScaledBitmap(background, width, height, true);
        comboImage.drawBitmap(background, 0, 0, null);
        comboImage.drawBitmap(foreground, 100,foreground.getHeight(), null);

        return cs;
    }
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onValidationSucceeded() {
        registerButton.setEnabled(true);

        submit();

    }



    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
