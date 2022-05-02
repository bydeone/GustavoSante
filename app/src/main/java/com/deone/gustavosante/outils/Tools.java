package com.deone.gustavosante.outils;

import static com.deone.gustavosante.outils.Constants.CAMERA_REQUEST_CODE;
import static com.deone.gustavosante.outils.Constants.STORAGE_REQUEST_CODE;
import static com.deone.gustavosante.outils.UserDatabase.ajouterUser;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.deone.gustavosante.R;
import com.deone.gustavosante.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class Tools {

    public static ProgressDialog showProgressDialog(Activity activity, String titre, String message) {
        ProgressDialog progressDialog= new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(titre);
        progressDialog.setMessage(message);
        progressDialog.show();
        return progressDialog;
    }

    public static void requestStoragePermissions(Activity activity, String[] storagePermissions) {
        ActivityCompat.requestPermissions(activity, storagePermissions, STORAGE_REQUEST_CODE);
    }

    public static boolean checkStoragePermissions(Context context) {
        boolean result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    public static void requestCameraPermissions(Activity activity, String[] cameraPermissions) {
        ActivityCompat.requestPermissions(activity, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    public static boolean checkCameraPermissions(Context context) {
        boolean result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    public static HashMap<String, String> prepareUserData(String uid, String unoms,
                                                          String ucover, String uavatar,
                                                          String utelephone, String uemail,
                                                          String ucni, String udelivrance,
                                                          String ucodepostal, String uville,
                                                          String uadresse, String upays, String udate) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("unoms", unoms);
        hashMap.put("ucover", ucover);
        hashMap.put("uavatar", uavatar);
        hashMap.put("utelephone", utelephone);
        hashMap.put("uemail", uemail);
        hashMap.put("ucni", ucni);
        hashMap.put("udelivrance", udelivrance);
        hashMap.put("ucodepostal", ucodepostal);
        hashMap.put("uville", uville);
        hashMap.put("uadresse", uadresse);
        hashMap.put("upays", upays);
        hashMap.put("udate", udate);
        return hashMap;
    }

    public static void ajouterUserImage(Activity activity, User user, String field, Uri imageUri, String myuid){
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Users/"+"user_" + myuid + "_" +  field + "_" + timestamp;

        ProgressDialog progressDialog = showProgressDialog(
                activity,
                ""+activity.getString(R.string.app_name),
                ""+activity.getString(R.string.add_image_message));

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful());

            String downloadUri = uriTask.getResult().toString();
            if (uriTask.isSuccessful()){
                progressDialog.dismiss();
                Toast.makeText(
                        activity,
                        ""+activity.getString(R.string.save_image_ok),
                        Toast.LENGTH_SHORT
                ).show();
                user.setUavatar(downloadUri);
                ajouterUser(activity, user, myuid);
            }else
                progressDialog.dismiss();
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(
                    activity,
                    ""+e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        });
    }

}
