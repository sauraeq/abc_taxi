package com.example.taxibookinguserapplication.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseActivity extends AppCompatActivity {

    private ProgressCustomDialog progressCustomDialog;


    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context. getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }




    public void showToastMessage(Context context, String message)
    {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void  showProgressDialog()
    {
        if (progressCustomDialog!=null)
            progressCustomDialog.showCustomDialog();
        else
        {
            progressCustomDialog=new ProgressCustomDialog(this,"Please Wait!");
            progressCustomDialog.showCustomDialog();
        }
    }

    public void  hideProgressDialog()
    {
        if (progressCustomDialog!=null)
            progressCustomDialog.hideCustomDialog();
    }


    public static boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean date(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^ (0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\\\d\\\\d)$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public   String agomethod(String data){
        String dateage = "";
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date past = format.parse(data);
            Date now = new Date();
            long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());
//
//          System.out.println(TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime()) + " milliseconds ago");
//          System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime()) + " minutes ago");
//          System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime()) + " hours ago");
//          System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime()) + " days ago");

            if(seconds<60)
            {
                dateage=seconds+" seconds ago";
                System.out.println(seconds+" seconds ago");
            }
            else if(minutes<60)
            {
                dateage=minutes+" minutes ago";
                System.out.println(minutes+" minutes ago");
            }
            else if(hours<24)
            {
                dateage=hours+" hours ago";
                System.out.println(hours+" hours ago");
            }
            else
            {
                dateage= days+" days ago";
                System.out.println(days+" days ago");
            }
        }
        catch (Exception j){
            j.printStackTrace();
        }
        return dateage;
    }

}


