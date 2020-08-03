package com.example.smartpds;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.smartpds.orderview.DisplayOrdersActivity;
import com.example.smartpds.orderview.DisplayOrdersActivityForDistributor;
import com.example.smartpds.utils.CheckPermissions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

public class DistributorDashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    SliderLayout sliderLayout;
    HashMap<String, String> Hash_file_maps;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    // Toolbar toolbar;
    Menu menu;
    String email, name, qrlink;
    long walletAmount=1;
    CardView walletCard, shopCard, showQR, showQuota , showOrders;
    Button qrgenerate;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    ImageView dialog,distributorPicture;
    String mobile;
    TextView distributorName, distributorEmail;
    private DatabaseReference mDatabase,quota,statusReference;
    private DatabaseReference mDatabaseKyc;
    private NavigationView mNavigationView;
    SharedPreferences pref;
    private static final String CUSTOMER_MOBILE_NUMBER = "customerMobileNumber";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distributordashboard);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);

        drawerLayout = (DrawerLayout) findViewById(R.id.distributor_drawer_layout);
        navigationView = findViewById(R.id.distributor_nav_view);
        navigationView.bringToFront();
        mobile = getIntent().getStringExtra("mobile");
        statusReference = FirebaseDatabase.getInstance().getReference("Distributors").child(mobile);
        statusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("accountStatus").getValue(String.class);
                if (status.equalsIgnoreCase("pending")){
                    try {
                        showErrorDialog(DistributorDashBoard.this,"Error");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (
                        this,
                        drawerLayout,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close
                ) {
        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);
        menu.findItem(R.id.nav_profile).setVisible(false);

        /////////////////////////////////
        mNavigationView = (NavigationView) findViewById(R.id.distributor_nav_view);


        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username",mobile);
        editor.putString("usertype","distributor");
        editor.commit();
        Toast.makeText(this, "Session Started", Toast.LENGTH_SHORT).show();
        mDatabase = FirebaseDatabase.getInstance().getReference("Distributors").child(mobile);
        quota = FirebaseDatabase.getInstance().getReference("DistributorsProducts").child(mobile);
        mDatabaseKyc = FirebaseDatabase.getInstance().getReference("KYC").child("DistributorKYC").child(mobile);

        final DrawerLayout drawer = findViewById(R.id.distributor_drawer_layout);
        ImageView menuIcon = (ImageView) findViewById(R.id.menu_icon);
        qrgenerate = (Button) findViewById(R.id.generateqr);
        showQR = findViewById(R.id.showqr);
        showOrders= findViewById(R.id.orders);
        distributorName = mNavigationView.getHeaderView(0).findViewById(R.id.distributor_name);
        distributorEmail = mNavigationView.getHeaderView(0).findViewById(R.id.distributormail);
        distributorPicture=mNavigationView.getHeaderView(0).findViewById(R.id.distributor_pic);
        ImageView extraMenuIcon = (ImageView) findViewById(R.id.extra_menu_icon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });


        qrgenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DistributorDashBoard.this, QRGeneration.class);
                startActivity(intent);
            }
        });
        //Slider Code

        Hash_file_maps = new HashMap<String, String>();
        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        Uri path1 = Uri.parse("android.resource://com.example.smartpds/" + R.drawable.rationone);
        Uri path2 = Uri.parse("android.resource://com.example.smartpds/" + R.drawable.rationtwo);
        Uri path3 = Uri.parse("android.resource://com.example.smartpds/" + R.drawable.rationthree);
        Uri path4 = Uri.parse("android.resource://com.example.smartpds/" + R.drawable.rationfour);
        Uri path5 = Uri.parse("android.resource://com.example.smartpds/" + R.drawable.rationfive);
        Uri path6 = Uri.parse("android.resource://com.example.smartpds/" + R.drawable.rationsix);
        String str1 = path1.toString();
        String str2 = path2.toString();
        String str3 = path3.toString();
        String str4 = path4.toString();
        String str5 = path5.toString();
        String str6 = path6.toString();
        Hash_file_maps.put("One Nation One Ration", str1);
        Hash_file_maps.put("One Nation One Card", str2);
        Hash_file_maps.put("Ration Card India", str3);
        Hash_file_maps.put("एक राष्ट्र एक राशन", str4);
        Hash_file_maps.put("डिजिटल इंडिया का डिजिटल राशन", str5);
        Hash_file_maps.put("Digital Ration Khata", str6);


        for (String name : Hash_file_maps.keySet()) {

            TextSliderView textSliderView = new TextSliderView(DistributorDashBoard.this);
            textSliderView
                    .description(name)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(this);
        mNavigationView.setNavigationItemSelectedListener(this);
        walletCard = findViewById(R.id.bankCard);
//        showQuota = findViewById(R.id.quotashow);
        shopCard = findViewById(R.id.shop_card);
        walletCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DistributorDashBoard.this, DistributorWalletTransaction.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });
        showQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    showDialog(DistributorDashBoard.this, "Your QR Code");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        showOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(DistributorDashBoard.this, "All Orders", Toast.LENGTH_SHORT).show();
                Intent showOrders = new Intent(getApplicationContext(), DisplayOrdersActivityForDistributor.class);
                showOrders.putExtra(CUSTOMER_MOBILE_NUMBER, mobile);
                startActivity(showOrders);

            }
        });

        shopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (CheckPermissions.checkWriteExternalPermission(getApplicationContext() , android.Manifest.permission.CAMERA)) {

                    Intent intent = new Intent(DistributorDashBoard.this, CustomerQRScanner.class);
                    intent.putExtra("distributormobile", mobile);
                    startActivity(intent);
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
                    }

                }
            }
        });
//        showQuota.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(DistributorDashBoard.this, "Product Quota", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(DistributorDashBoard.this, DistributerQuota.class);
//                intent.putExtra("mobile", mobile);
//                startActivity(intent);
//            }
//        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                email = dataSnapshot.child("email").getValue(String.class);
                name = dataSnapshot.child("shopname").getValue(String.class);
//                walletAmount = dataSnapshot.child("walletAmmount").getValue(long.class);

                distributorEmail.setText(email);
                distributorName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mDatabaseKyc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                qrlink = dataSnapshot.child("qrlink").getValue(String.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.distributor_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }

    public void showDialog(Activity activity, String msg) throws IOException {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.image_layout);
        String name = Constants.name;
        TextView text2;
        ImageView myImage = (ImageView) dialog.findViewById(R.id.a);


        Picasso.with(this).load(qrlink).into(myImage);
//        myImage.setImageBitmap(mIcon_val);
//            myImage.setImageBitmap(myBitmap);

        if (name != "") {
            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText(name.toUpperCase() + " GROCERY SHOP");
        } else {
            text2 = (TextView) dialog.findViewById(R.id.text_dialog);
            text2.setTextSize(15);
            text2.setText("click here to download qr");
        }
        Button dialogButton1 = (Button) dialog.findViewById(R.id.btn1);


        //below code add for Download QR
        Button dialogButton2 = (Button) dialog.findViewById(R.id.btn2);

        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (CheckPermissions.checkWriteExternalPermission(getApplicationContext() ,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    new DownloadQr(qrlink, myImage,mobile , getApplicationContext() ).execute() ;
                    dialog.dismiss();
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
                    }

                }



            }
        });

        ////
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });

        dialog.show();

    }
    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this, ""+slider.getBundle().get("extra") , Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.logout:
                // Red item was selected
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(DistributorDashBoard.this, UserLogin.class);
                startActivity(intent);
                break;
            case R.id.exit:
                // Green item was selected
                return true;
            case R.id.nav_profile:
                Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_wallet:
                Toast.makeText(this, "Wallet Clicked", Toast.LENGTH_SHORT).show();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    //////////////////////////////////////////////After

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_home:
                Toast.makeText(DistributorDashBoard.this, "Home Click", Toast.LENGTH_SHORT).show();

                break;
            case R.id.nav_wallet:

                Intent walletIntent = new Intent(DistributorDashBoard.this, DistributorWalletTransaction.class);
                walletIntent.putExtra("mobile", mobile);
                startActivity(walletIntent);
                break;
            case R.id.nav_Quata:

                Intent quotaIntent = new Intent(DistributorDashBoard.this, DistributorKycRegister.class);
                quotaIntent.putExtra("mobile", mobile);
                startActivity(quotaIntent);

//                Toast.makeText(DistributorDashBoard.this, "Wallet : " + walletAmount, Toast.LENGTH_SHORT).show();


                break;
            case R.id.nav_orders:

                Toast.makeText(DistributorDashBoard.this, "All Orders", Toast.LENGTH_SHORT).show();
                Intent showOrders = new Intent(getApplicationContext(), DisplayOrdersActivity.class);
                showOrders.putExtra(CUSTOMER_MOBILE_NUMBER, mobile);
                startActivity(showOrders);

                break;

                ///nav_logic display as Logout label
            case R.id.nav_login:
                SharedPreferences.Editor editor1 = pref.edit();
                editor1.clear();
                editor1.commit();
                Intent intent1 = new Intent(DistributorDashBoard.this, UserLogin.class);
                startActivity(intent1);
                break;
            case R.id.nav_logout:

                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(DistributorDashBoard.this, UserLogin.class);
                startActivity(intent);

                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void showErrorDialog(Activity activity, String msg) throws IOException {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.insufficientwallet);
//        myImage.setImageBitmap(mIcon_val);
//            myImage.setImageBitmap(myBitmap);


        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView text1 = (TextView) dialog.findViewById(R.id.text1);
        text.setTextSize(25);
        text.setText("Account Status Pending");
        text1.setText("You are not approved Distributor.");

        Button dialogButton1 = (Button) dialog.findViewById(R.id.btn1);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.btn2);
        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finishAffinity();
                System.exit(0);

            }
        });
        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                finishAffinity();
                System.exit(0);
            }
        });
        dialog.show();

    }
}
