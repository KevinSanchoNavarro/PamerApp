package pe.edu.pamer.aula.pamer.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pe.edu.pamer.aula.pamer.network.FirebaseAuthenticateService;
import pe.edu.pamer.aula.pamer.R;

public class MainPanelActivity extends AppCompatActivity {

    private TextView textViewEmail, textViewID;
    private Button buttonLogout;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel);

        inicializarComponentes();
        eventosClicks();

    }

    private void eventosClicks() {
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuthenticateService.logOut();
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuthenticateService.getFirebaseAuth();
        user = FirebaseAuthenticateService.getFirebaseUser();
        verificaUser();
    }

    private void verificaUser() {
        if (user == null){
            finish();
        }else{
            textViewEmail.setText("Email: "+user.getEmail());
            textViewID.setText("ID: "+user.getUid());
        }
    }

    private void inicializarComponentes() {
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewID = (TextView) findViewById(R.id.textViewID);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
    }








}
