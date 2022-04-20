package com.example.taxibookinguserapplication.util;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

import com.example.taxibookinguserapplication.R;



public class ProgressCustomDialog {
     Dialog dialog;

    public ProgressCustomDialog(Activity activity, String msg){
         dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView text = (TextView) dialog.findViewById(R.id.text_layout);
        text.setText(msg);

    }



    public void  isCancelable(boolean isCancelable)
    {
        dialog.setCancelable(isCancelable);
    }

    public void showCustomDialog()
    {
        if (dialog!=null)
            dialog.show();
    }


    public void hideCustomDialog()
    {
        if (dialog!=null)
            dialog.dismiss();
    }
}