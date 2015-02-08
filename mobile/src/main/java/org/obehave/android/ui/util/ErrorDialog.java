package org.obehave.android.ui.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import org.obehave.android.ui.exceptions.UiException;

final public class ErrorDialog {
        private UiException e;
        private Context context;
        public ErrorDialog(UiException e, Context context) {
            this.e = e;
            this.context = context;
        }

        public void invoke() {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setMessage(e.getMessage());
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }