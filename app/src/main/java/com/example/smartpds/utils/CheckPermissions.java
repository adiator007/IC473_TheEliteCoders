package com.example.smartpds.utils;

import android.content.Context;
import android.content.pm.PackageManager;

 public class CheckPermissions  {


     public static boolean checkWriteExternalPermission(Context context , String Permission)
    {





        // android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        String permission = Permission;
        int res =context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


}
