package com.example.taxibookinguserapplication.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public static void toast_Short(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static void toast_Long(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
