package com.example.smartpds.benificiary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.smartpds.R;
import com.example.smartpds.model.Benificiary;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AddBenificiaryActivity extends AppCompatActivity {


    private static final String UPLOAD_IMAGE_STORAGE_KEY = "benificiaryDocuments";
    ImageButton btClose , btDone;
    LinearLayout photoContainer , dobContainer , adharcardContainer;
    TextView imagename , benificiaryDob , adharcard;
    EditText benificiaryName;
    private ImageView imageview;

    private static final int IMAGE_REQUEST = 1;
    private Uri photoUri = null, adharuri = null;
    private static final int ADHAR_REQUEST=2;

    FirebaseStorage storage ;
    private FirebaseDatabase database;
    DatabaseReference benificiaryReference;

    private static final String USERID = "userid";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_benificiary);
          storage= FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();


        final String customer = getIntent().getStringExtra(USERID);

        benificiaryReference= database.getReference("Customers/" + customer +"/benificiary");

        initcomponent();
        initpicker();



        btDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                progressDialog = new ProgressDialog(AddBenificiaryActivity.this);
                progressDialog.setMessage("Submitting benificiary detail");
                progressDialog.setCancelable(false);
                progressDialog.show();
                submitinformation();

            }
        });


    }

    private void submitinformation() {
        if (photoUri!=null)
        {
            if (adharuri!=null)
            {
                if (!TextUtils.isEmpty(benificiaryName.getText()))
                {
                    if (!TextUtils.isEmpty(benificiaryDob.getText()))
                    {
                        uploadinformation();
                    }else {
                        //benidiciary dob error
                    }
                }
                else {
                    //Benificiary Name Error
                }
            }
            else
            {
                //adhara error dialog
            }
        }
        else
        {
            //photoerror dialog
        }


    }

//    public StorageReference getStorageReference() {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        return storage.getReferenceFromUrl(getApplicationContext().getResources().getString(R.string.storage_link));
//    }

    public UploadTask uploadDocument(Uri uri, String imageTitle) {

        StorageReference riversRef = storage.getReference().child(UPLOAD_IMAGE_STORAGE_KEY + "/" + imageTitle);
        // Create file metadata including the content type
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setCacheControl("max-age=7776000, Expires=7776000, public, must-revalidate")
                .build();

        return riversRef.putFile(uri, metadata);
    }



   public void uploadPhoto(Uri photoUri , OnUploadListner onUploadListner)
   {
       String imagetitle ="PHOTO_"+ benificiaryName.getText().toString()+benificiaryDob.getText().toString()+ ServerValue.TIMESTAMP;
       UploadTask uploadTask = uploadDocument(photoUri ,imagetitle );

       if (uploadTask!=null)
       {
           uploadTask.addOnCompleteListener(task  ->{
               if (task.isSuccessful())
               {
                   Objects.requireNonNull(task.getResult()).getStorage().getDownloadUrl().addOnCompleteListener(task1 -> {
                       if (task1.isSuccessful()) {
                           Uri downloadUrl = task1.getResult();
                           onUploadListner.onComplete(downloadUrl);
                       }
                   });
               }
           });
       }
   }


    public void uploadAdhar(Uri adharuri , OnUploadListner onUploadListner)
    {
        String imagetitle = "ADHAR_"+benificiaryName.getText().toString()+benificiaryDob.getText().toString()+ ServerValue.TIMESTAMP;
        UploadTask uploadTask = uploadDocument(adharuri ,imagetitle );

        if (uploadTask!=null)
        {
            uploadTask.addOnCompleteListener(task  ->{
                if (task.isSuccessful())
                {
                    Objects.requireNonNull(task.getResult()).getStorage().getDownloadUrl().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            Uri downloadUrl = task1.getResult();
                            onUploadListner.onComplete(downloadUrl);
                        }
                    });
                }
            });
        }
    }
    private void uploadinformation() {
        Benificiary benificiary = new Benificiary();
        benificiary.setBname(benificiaryName.getText().toString());
        benificiary.setBdob(benificiaryDob.getText().toString());


      uploadPhoto(photoUri, new OnUploadListner() {
          @Override
          public void onComplete(Uri uploadeduri) {
              benificiary.setPhotouri(uploadeduri.toString());

              uploadAdhar(adharuri, new OnUploadListner() {
                  @Override
                  public void onComplete(Uri uploadeduri) {
                      benificiary.setAdharuri(uploadeduri.toString());
                       uploadBenificiaryObject(benificiary);

                  }

                  @Override
                  public void onFailed() {

                  }
              });

          }

          @Override
          public void onFailed() {

          }
      });

    }

    private void uploadBenificiaryObject(Benificiary benificiary) {
        benificiary.setStatus(false);
        benificiaryReference.push().setValue(benificiary);
        Toast.makeText(AddBenificiaryActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
    }

    private void initpicker() {

        //date picker
        dobContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDatePickerDark((LinearLayout) view);
            }
        });


        //benificiaryPhotoPicler
        photoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMAGE_REQUEST);
            }
        });


        //adaharcardfilepicker
        adharcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, ADHAR_REQUEST);

            }
        });
    }

    private void initcomponent() {
        btClose = findViewById(R.id.bt_close);
        btDone = findViewById(R.id.benificiary_done_imagebtn);

        photoContainer = findViewById(R.id.benificiary_photo_container);
        dobContainer = findViewById(R.id.benificiary_dob_container);
        adharcardContainer = findViewById(R.id.benificiary_adharcard_container);

        imageview = findViewById(R.id.benificiary_image_view);
        imagename = findViewById(R.id.benificiary_image_textview);
        benificiaryDob = findViewById(R.id.benificiary_dob_textview);
        adharcard = findViewById(R.id.benificiary_adharcard_filename_textview);

        benificiaryName = findViewById(R.id.benificiary_name_edittext);

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            photoUri = data.getData();
            Glide.with(getApplicationContext()).load(photoUri).into(imageview);
        }
        if (requestCode == ADHAR_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            adharuri = data.getData();
            adharcard.setText(getFileName(adharuri));
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    private void dialogDatePickerDark(final LinearLayout bt) {
        Calendar cur_calender = Calendar.getInstance();

        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date_ship_millis = calendar.getTimeInMillis();
                       benificiaryDob.setText(getFormattedDateSimple(date_ship_millis));
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark theme
        datePicker.setThemeDark(true);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
//        datePicker.setMinDate(cur_calender);
        datePicker.show(getFragmentManager(), "Datepickerdialog");
    }

    public static String getFormattedDateSimple(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy");
        return newFormat.format(new Date(dateTime));
    }
}
