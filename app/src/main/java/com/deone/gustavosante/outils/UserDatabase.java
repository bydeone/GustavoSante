package com.deone.gustavosante.outils;

import static com.deone.gustavosante.outils.Tools.showProgressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;

import com.deone.gustavosante.MainActivity;
import com.deone.gustavosante.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserDatabase {
    public static void connectUser(Activity activity, String login, String motDePasse){
        ProgressDialog progressDialog = showProgressDialog(activity,
                ""+activity.getString(R.string.app_name),
                ""+activity.getString(R.string.pd_conn_user));
        FirebaseAuth.getInstance().signInWithEmailAndPassword(login, motDePasse)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(
                                activity,
                                ""+activity.getString(R.string.sign_in_ok,
                                        ""+activity.getString(R.string.app_name)),
                                Toast.LENGTH_LONG
                        ).show();
                        activity.startActivity(new Intent(activity, MainActivity.class));
                        activity.finish();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(
                                activity,
                                ""+activity.getString(R.string.sign_in_error),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
