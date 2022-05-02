package com.deone.gustavosante.outils;

import android.app.Activity;
import android.app.ProgressDialog;

public class Tools {

    public static ProgressDialog showProgressDialog(Activity activity, String titre, String message) {
        ProgressDialog progressDialog= new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(titre);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

}
