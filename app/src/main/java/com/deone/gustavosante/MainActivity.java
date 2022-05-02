package com.deone.gustavosante;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.deone.gustavosante.connexion.ConnexionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String myuid;
    private RecyclerView rvPatients;
    private LinearLayoutManager layooutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkUser();
    }

    @Override
    public void onClick(View v) {

    }

    private void checkUser() {
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null){
            myuid = fUser.getUid();
            initViews();
        }else {
            startActivity(new Intent(this, ConnexionActivity.class));
            finish();
        }
    }

    private void initViews() {
        rvPatients = findViewById(R.id.rvPatients);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPatients.setLayoutManager(layoutManager);
    }
}