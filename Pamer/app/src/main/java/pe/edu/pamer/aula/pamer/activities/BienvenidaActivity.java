package pe.edu.pamer.aula.pamer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import pe.edu.pamer.aula.pamer.R;

public class BienvenidaActivity extends AppCompatActivity{

    private Button buttonComenzar;
    private TextView textViewMensaje;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bienvenida);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.argb(255, 41, 169, 223));
        }*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        inicializarComponentes();
        sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String correo = sharedpreferences.getString("username","usernameEnCasoDeNull");

        if(!correo.equals("usernameEnCasoDeNull")){
            Intent i = new Intent(BienvenidaActivity.this,LoginActivity.class);
            startActivity(i);
        }

        eventoClicks();

    }

    private void inicializarComponentes() {
        textViewMensaje = (TextView) findViewById(R.id.textViewMensaje);
        buttonComenzar = (Button) findViewById(R.id.buttonComenzar);
    }

    private void eventoClicks() {
        buttonComenzar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BienvenidaActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }

}
