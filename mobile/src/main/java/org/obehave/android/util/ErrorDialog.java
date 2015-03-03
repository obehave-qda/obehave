package org.obehave.android.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

final public class ErrorDialog {
        private String message;
        private Context context;
        public ErrorDialog(Exception e, Context context) {
            this.message = e.getMessage();
            this.context = context;
        }

        public ErrorDialog(String message, Context context) {
            this.message = message;
            this.context = context;
        }

        public void invoke() {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setMessage(message);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }