package com.deone.gustavosante.connexion;

import static com.deone.gustavosante.outils.UserDatabase.connectUser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.deone.gustavosante.MainActivity;
import com.deone.gustavosante.R;
import com.deone.gustavosante.outils.UserDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConnexionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtvLogin;
    private EditText edtvMotdepasse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        checkUser();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btConnexion){
            String login = edtvLogin.getText().toString().trim();
            String motDePasse = edtvMotdepasse.getText().toString().trim();
            verificationDesChamps(""+login, ""+motDePasse);
        }else if (v.getId() == R.id.tvNouveauCompte){
            startActivity(new Intent(this, NewaccountActivity.class));
            finish();
        }else if (v.getId() == R.id.tvMotDePasseOublie){
            startActivity(new Intent(this, ForgetActivity.class));
            finish();
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
        edtvLogin = findViewById(R.id.edtvLogin);
        edtvMotdepasse = findViewById(R.id.edtvMotdepasse);
        findViewById(R.id.tvNouveauCompte).setOnClickListener(this);
        findViewById(R.id.tvMotDePasseOublie).setOnClickListener(this);
        findViewById(R.id.btConnexion).setOnClickListener(this);
    }

    private void verificationDesChamps(String login, String motDePasse) {
        if (login.isEmpty()){
            Toast.makeText(
                    ConnexionActivity.this,
                    ""+getString(R.string.login_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        if (motDePasse.isEmpty()){
            Toast.makeText(
                    ConnexionActivity.this,
                    ""+getString(R.string.pwd_error),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        connectUser(this,""+login, ""+motDePasse);
    }

}