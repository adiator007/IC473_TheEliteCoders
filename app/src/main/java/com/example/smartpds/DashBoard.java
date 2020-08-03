package com.example.smartpds;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.smartpds.orderview.DisplayOrdersActivity;
import com.example.smartpds.utils.CheckPermissions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

public class DashBoard extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener {
    Menu menu;
    SliderLayout sliderLayout;
    HashMap<String, String> Hash_file_maps;
    CardView walletCard, shopCard, allOrders, manageBeneficiary;
    String mobile;
    String email, name, profilePic;
    private NotificationManagerCompat notificationManager;
    TextView customerName, customerEmail;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private DatabaseReference mDatabase,statusReference;
    private DatabaseReference mDatabaseKyc;
    private NavigationView mNavigationView;
    private ImageView customerProfilePic;
    private Long walletAmount;
    private static final String CUSTOMER_MOBILE_NUMBER = "customerMobileNumber";
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        drawerLayout = (DrawerLayout) findViewById(R.id.user_drawer_layout);
        navigationView = findViewById(R.id.user_nav_view);
        navigationView.bringToFront();
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        mobile = getIntent().getStringExtra("mobile");
        statusReference = FirebaseDatabase.getInstance().getReference("Customers").child(mobile);
        statusReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("accountStatus").getValue(String.class);
                if (status.equalsIgnoreCase("pending")){
                    try {
                        showErrorDialog(DashBoard.this,"Error");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        if (pref.contains("username") && pref.contains("usertype"))
        {

        }
        else {
            Intent intent = new Intent(DashBoard.this, UserLogin.class);
            startActivity(intent);
        }

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

        mNavigationView = (NavigationView) findViewById(R.id.user_nav_view);
        customerName = mNavigationView.getHeaderView(0).findViewById(R.id.customer_name);
        customerEmail = mNavigationView.getHeaderView(0).findViewById(R.id.customer_email);
        customerProfilePic = mNavigationView.getHeaderView(0).findViewById(R.id.customer_profile_pic);
        notificationManager = NotificationManagerCompat.from(this);

        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username",mobile);
        editor.putString("usertype","customer");
        editor.commit();
        Toast.makeText(this, "Session Started", Toast.LENGTH_SHORT).show();
        final DrawerLayout drawer = findViewById(R.id.user_drawer_layout);
        mDatabase = FirebaseDatabase.getInstance().getReference("Customers").child(mobile);
        mDatabaseKyc = FirebaseDatabase.getInstance().getReference("CustomerKYC").child(mobile);
        ImageView menuIcon = (ImageView) findViewById(R.id.menu_icon);
        ImageView extraMenuIcon = (ImageView) findViewById(R.id.extra_menu_icon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        extraMenuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                        sendOnChannel1(view);

            }
        });


        if (CheckPermissions.checkWriteExternalPermission(getApplicationContext() ,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
            }

        }

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                email = dataSnapshot.child("email").getValue(String.class);
                name = dataSnapshot.child("fname").getValue(String.class) + " " + dataSnapshot.child("lname").getValue(String.class);
                walletAmount = dataSnapshot.child("walletAmmount").getValue(long.class);

                customerEmail.setText(email);
                customerName.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mDatabaseKyc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                profilePic = dataSnapshot.child("profilepic").child("url").getValue(String.class);
                Picasso.with(DashBoard.this).load(profilePic).into(customerProfilePic);
//                Toast.makeText(DashBoard.this, "Profile Pic Loaded=== "+profilePic, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

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

            TextSliderView textSliderView = new TextSliderView(DashBoard.this);
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


        walletCard = findViewById(R.id.bankCard);
        allOrders = findViewById(R.id.allorders);
        manageBeneficiary = findViewById(R.id.manage_beneficiary);
        shopCard = findViewById(R.id.shop_card);
        manageBeneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoard.this, BeneficiaryActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });
        walletCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent showOrders = new Intent(getApplicationContext(), CustomerWalletTransaction.class);
                showOrders.putExtra("mobile", mobile);
                startActivity(showOrders);
            }
        });

        allOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(DashBoard.this, "All Orders", Toast.LENGTH_SHORT).show();
                Intent showOrders = new Intent(getApplicationContext(), DisplayOrdersActivity.class);
                showOrders.putExtra(CUSTOMER_MOBILE_NUMBER, mobile);
                startActivity(showOrders);

            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashBoard.this, "Buy Clicked", Toast.LENGTH_LONG).show();
            }
        });
        shopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckPermissions.checkWriteExternalPermission(getApplicationContext() , android.Manifest.permission.CAMERA)) {

                    Intent intent = new Intent(DashBoard.this, DistributorQRScanner.class);
                    intent.putExtra("customermobile", mobile);
                    startActivity(intent);
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
                    }

                }


            }
        });
    }


    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this, slider.getBundle().get("extra") + " " + "   " + Hash_file_maps.get("Shivkumar"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.user_drawer_layout);
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

//        super.onBackPressed();
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.overflow_menu, menu);

        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action b ar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.logout:
                // Red item was selected
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(DashBoard.this, UserLogin.class);
                startActivity(intent);
                break;
            case R.id.exit:
                // Green item was selected
                return true;
            case R.id.nav_wallet:
                Intent walletIntent = new Intent(getApplicationContext(), CustomerWalletTransaction.class);
                walletIntent.putExtra("mobile", mobile);
                startActivity(walletIntent);
                break;
            case R.id.nav_benificiary:
                Intent benificiaryintent = new Intent(DashBoard.this, BeneficiaryActivity.class);
                benificiaryintent.putExtra("mobile", mobile);
                startActivity(benificiaryintent);
                break;

            case R.id.nav_kyc:
                Intent kyc = new Intent(DashBoard.this, CustomerKycRegister.class);
                kyc.putExtra("mobile", mobile);
                startActivity(kyc);
                break;

            case R.id.nav_orders:
                Toast.makeText(DashBoard.this, "All Orders", Toast.LENGTH_SHORT).show();
                Intent showOrders = new Intent(getApplicationContext(), DisplayOrdersActivity.class);
                showOrders.putExtra(CUSTOMER_MOBILE_NUMBER, mobile);
                startActivity(showOrders);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_wallet) {
            Intent walletIntent = new Intent(getApplicationContext(), CustomerWalletTransaction.class);
            walletIntent.putExtra("mobile", mobile);
            startActivity(walletIntent);
        } else if (id == R.id.nav_benificiary) {
            Intent benificiaryintent = new Intent(DashBoard.this, BeneficiaryActivity.class);
            benificiaryintent.putExtra("mobile", mobile);
            startActivity(benificiaryintent);
        } else if (id == R.id.nav_orders) {

            Toast.makeText(DashBoard.this, "All Orders", Toast.LENGTH_SHORT).show();
            Intent showOrders = new Intent(getApplicationContext(), DisplayOrdersActivity.class);
            showOrders.putExtra(CUSTOMER_MOBILE_NUMBER, mobile);
            startActivity(showOrders);
        } else if (id == R.id.nav_login) {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(DashBoard.this, UserLogin.class);
            startActivity(intent);
        }  else if (id == R.id.nav_kyc) {
            Intent kyc = new Intent(DashBoard.this, CustomerKycRegister.class);
            kyc.putExtra("mobile", mobile);
            startActivity(kyc);
        }
        else if (id == R.id.nav_app_setting) {

        } else if (id == R.id.nav_notification_setting) {

        }

        DrawerLayout drawer = findViewById(R.id.user_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
        text1.setText("You are not approved consumer.");

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
