package com.deone.gustavosante.connexion;

import static com.deone.gustavosante.outils.Constants.CAMERA_REQUEST_CODE;
import static com.deone.gustavosante.outils.Constants.IMAGE_PICK_CAMERA_CODE;
import static com.deone.gustavosante.outils.Constants.IMAGE_PICK_GALLERY_CODE;
import static com.deone.gustavosante.outils.Constants.STORAGE_REQUEST_CODE;
import static com.deone.gustavosante.outils.Tools.checkCameraPermissions;
import static com.deone.gustavosante.outils.Tools.checkStoragePermissions;
import static com.deone.gustavosante.outils.Tools.requestCameraPermissions;
import static com.deone.gustavosante.outils.Tools.requestStoragePermissions;
import static com.deone.gustavosante.outils.UserDatabase.creerUnCompte;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.deone.gustavosante.MainActivity;
import com.deone.gustavosante.R;
import com.deone.gustavosante.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NewaccountActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivAppAvatar;
    private EditText edtvNom;
    private EditText edtvTelephone;
    private EditText edtvLogin;
    private EditText edtvMotdepasse;
    private EditText edtvConfMotdepasse;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newaccount);
        checkUser();
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.newacc_titre))
                .setMessage(getString(R.string.newacc_message))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    startActivity(new Intent(this, ConnexionActivity.class));
                    finish();
                }).create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted){
                        pickFromCamera();
                    }else {
                        Toast.makeText(
                               this,
                                ""+getString(R.string.enable_camera_storage_permissions),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted){
                        pickFromGallery();
                    }else {
                        Toast.makeText(
                                this,
                                ""+getString(R.string.enable_storage_permissions),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
            break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                assert data != null;
                imageUri = data.getData();
                ivAppAvatar.setImageURI(imageUri);
            }else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                ivAppAvatar.setImageURI(imageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btCreateAccount){
            String nom = edtvNom.getText().toString().trim();
            String telephone = edtvTelephone.getText().toString().trim();
            String login = edtvLogin.getText().toString().trim();
            String motDePasse = edtvMotdepasse.getText().toString().trim();
            String confMotDePasse = edtvConfMotdepasse.getText().toString().trim();
            verificationDesChamps(""+nom, ""+telephone, ""+login, ""+motDePasse, ""+confMotDePasse);
        }else if (v.getId() == R.id.ivAppAvatar){
            showImageDialog();
        }
    }

    private void checkUser() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else {
            initViews();
        }
    }

    private void initViews() {
        edtvNom = findViewById(R.id.edtvNom);
        edtvTelephone = findViewById(R.id.edtvTelephone);
        edtvLogin = findViewById(R.id.edtvLogin);
        edtvMotdepasse = findViewById(R.id.edtvMotdepasse);
        edtvConfMotdepasse = findViewById(R.id.edtvConfMotdepasse);
        ivAppAvatar = findViewById(R.id.ivAppAvatar);
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ivAppAvatar.setOnClickListener(this);
        findViewById(R.id.btCreateAccount).setOnClickListener(this);
    }

    private void verificationDesChamps(String nom, String telephone, String login, String motDePasse, String confMotDePasse) {
        if (nom.isEmpty()){
            Toast.makeText(
                    NewaccountActivity.this,
                    ""+getString(R.string.nom_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        if (telephone.isEmpty()){
            Toast.makeText(
                    NewaccountActivity.this,
                    ""+getString(R.string.phone_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (login.isEmpty()){
            Toast.makeText(
                    NewaccountActivity.this,
                    ""+getString(R.string.login_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        if (motDePasse.isEmpty()){
            Toast.makeText(
                    NewaccountActivity.this,
                    ""+getString(R.string.pwd_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (confMotDePasse.equals(motDePasse)){
            Toast.makeText(
                    NewaccountActivity.this,
                    ""+getString(R.string.confirm_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        User user = new User("", ""+nom,
                "", "",
                ""+telephone, ""+login,
                "", "",
                "", "",
                "", "", "");
        creerUnCompte(this, imageUri, user, motDePasse);
    }

    private void showImageDialog() {
        String[] options = {getString(R.string.camera), getString(R.string.gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.selectionner_une_image));
        builder.setItems(options, (dialog, which) -> {
            switch(which){
                case 0 :
                    if (!checkCameraPermissions(this)){
                        requestCameraPermissions(this, cameraPermissions);
                    }else{
                        pickFromCamera();
                    }
                    break;
                case 1 :
                    if (!checkStoragePermissions(this)){
                        requestStoragePermissions(this, storagePermissions);
                    }else{
                        pickFromGallery();
                    }
                    break;
                default:
            }
        });
        builder.create().show();
    }

    private void pickFromCamera() {

    }

    private void pickFromGallery() {

    }

}