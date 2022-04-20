package com.example.taxibookinguserapplication.util;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.equalinfotech.coffee.util.DialogActionInterface;
import com.example.taxibookinguserapplication.R;


public  class CustomAlertDialog {
    Dialog dialog;

   public CustomAlertDialog(Activity activity, String msg, String btnName, final DialogActionInterface dialogActionInterface){
        dialog = new Dialog(activity);
       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
       dialog.setCancelable(false);
       dialog.setContentView(R.layout.alert_dialog);
       dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

       TextView text = (TextView) dialog.findViewById(R.id.text_layout);

       Button btn_dialog =  dialog.findViewById(R.id.btn_dialog);

       if (btnName!=null)
           btn_dialog.setText(btnName);

       if (msg!=null)
       text.setText(msg);


       btn_dialog.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               if (dialog.isShowing())
               {
                   if (dialogActionInterface != null) {
                       dialogActionInterface.onDialogAction();
                   }

                   dialog.dismiss();
               }
           }
       });



   }


   public void showCustomAlertDialog()
   {
       if (dialog!=null)
           dialog.show();
   }



}