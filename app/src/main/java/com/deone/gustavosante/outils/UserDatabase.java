package com.deone.gustavosante.outils;

import static com.deone.gustavosante.outils.Constants.DATABASE;
import static com.deone.gustavosante.outils.Constants.USERS;
import static com.deone.gustavosante.outils.Tools.ajouterUserImage;
import static com.deone.gustavosante.outils.Tools.prepareUserData;
import static com.deone.gustavosante.outils.Tools.showProgressDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.deone.gustavosante.MainActivity;
import com.deone.gustavosante.R;
import com.deone.gustavosante.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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
    public static void creerUnCompte(Activity activity, Uri imageUri, User user, String password){
        ProgressDialog progressDialog = showProgressDialog(activity,
                ""+activity.getString(R.string.app_name),
                ""+activity.getString(R.string.pd_create_user));
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(user.getUemail(), password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        FirebaseUser fUser = mAuth.getCurrentUser();
                        if (fUser != null)
                            if (imageUri != null)
                                ajouterUserImage(activity, user, "uavatar",
                                        imageUri, ""+fUser.getUid());
                            else
                                ajouterUser(activity, user, ""+fUser.getUid());
                        else
                            Toast.makeText(
                                    activity,
                                    ""+activity.getString(R.string.no_user),
                                    Toast.LENGTH_LONG
                            ).show();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(
                                activity,
                                ""+activity.getString(R.string.create_account_error),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }

    public static void ajouterUser(Activity activity, User user, String myuid){
        ProgressDialog progressDialog = showProgressDialog(
                activity,
                ""+activity.getString(R.string.app_name),
                ""+activity.getString(R.string.pd_save_user));
        String timestamp = String.valueOf(System.currentTimeMillis());
        HashMap<String, String> hashMap = prepareUserData(
                myuid, ""+user.getUnoms(), ""+user.getUcover(), ""+user.getUavatar(),
                ""+user.getUtelephone(), ""+user.getUemail(),""+user.getUcni(),
                ""+user.getUdelivrance(), ""+user.getUcodepostal(),
                ""+user.getUville(),""+user.getUadresse(), ""+user.getUpays(), timestamp
        );
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(""+DATABASE);
        reference.child(USERS).child(myuid).setValue(hashMap)
                .addOnSuccessListener(unused -> {
                    progressDialog.dismiss();
                    Toast.makeText(
                            activity,
                            ""+activity.getString(R.string.save_user_info_ok),
                            Toast.LENGTH_SHORT
                    ).show();
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(
                    activity,
                    ""+e.getMessage(),
                    Toast.LENGTH_SHORT
            ).show();
        });
    }
}
