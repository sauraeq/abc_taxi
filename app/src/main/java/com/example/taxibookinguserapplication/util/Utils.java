package com.example.taxibookinguserapplication.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.File;


public class Utils {


    public static Bitmap getBitmapFromImagePath(String path) {
        Bitmap bitmap = null;
        File imgFile = new File(path);
        if (imgFile.exists()) {
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

        }
        return bitmap;
    }
    /**
     * will toast message
     *
     * @param context
     * @param msg
     */
    public static void showLongToastMessage(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * show message In Toast.
     *
     * @param context
     * @param string
     */
    public static void showToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }







}
