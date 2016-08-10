package com.suyogcomputech.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.suyogcomputech.sms.R;


/**
 * Created by Amrit Ratha on 1/8/2015.
 */
public class DialogManager {
    private Context _context;

    public DialogManager(Context _context) {
        this._context = _context;
    }

    public void showDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setIcon(R.drawable.error);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ((Activity) _context).finish();
            }
        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showAlertDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setIcon(R.drawable.error);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
