package pe.edu.pamer.aula.pamer.activities;

import android.app.Dialog;
import android.os.Process;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import pe.edu.pamer.aula.pamer.PamerApp;
import pe.edu.pamer.aula.pamer.R;
import pe.edu.pamer.aula.pamer.network.FirebaseAuthenticateService;
import pe.edu.pamer.aula.pamer.fragments.ContactanosFragment;
import pe.edu.pamer.aula.pamer.fragments.EtisFragment;
import pe.edu.pamer.aula.pamer.fragments.HistoriasExitoFragment;
import pe.edu.pamer.aula.pamer.fragments.HomeFragment;
import pe.edu.pamer.aula.pamer.fragments.QuienesSomosFragment;
import pe.edu.pamer.aula.pamer.fragments.UbicanosFragment;

public class NavDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView textViewCorreo, textViewNombre;
    public static String correo_usuario="", nombre_usuario="", apellido_usuario="", codigo_usuario="", login_email_usuario="", login_pass_usuario="", id_perfil_usuario="";
    public String login_way="";
    public NavigationView navigationView;
    public static String TAG = "Pamer";
    public static String modeAlert = "";
    SharedPreferences sharedpreferences;
    Button btnSiLogout, btnNoLogout, btnOkCerrarApp, btnCancelCerrarApp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_nav_drawer);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.argb(255, 41, 169, 223));
        }*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        login_way = sharedpreferences.getString("enter_mode","EnterModeEnCasoDeNull");

        if(login_way.equals("login")) {
            correo_usuario = sharedpreferences.getString("user_correo","UserCorreoEnCasoDeNull");
            nombre_usuario = sharedpreferences.getString("user_nombre","UserNombreEnCasoDeNull");
            apellido_usuario = sharedpreferences.getString("user_apellido","UserApellidoEnCasoDeNull");
            codigo_usuario = sharedpreferences.getString("user_codigo","UserCodigoEnCasoDeNull");
            login_email_usuario = sharedpreferences.getString("username","UsernameEnCasoDeNull");
            login_pass_usuario = sharedpreferences.getString("password","PasswordEnCasoDeNull");
            id_perfil_usuario = sharedpreferences.getString("user_idPerfil","UserIdPerfilEnCasoDeNull");
        }

        decideItemsMenuLateral(navigationView, login_way);

        if(login_way.equals("login")){
            HomeFragment hf = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor_nav_drawer, hf).commit();
            inicializarDatosUsuario(navigationView);
        }

        if(login_way.equals("conocenos")){
            /*QuienesSomosFragment qsf = new QuienesSomosFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor_nav_drawer, qsf).commit();*/
            HomeFragment hf = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor_nav_drawer, hf).commit();

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        //user = FirebaseAuthenticateService.getFirebaseUser();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(NavDrawerActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.popup_salir_aplicacion,null);
            builder.setView(dialogView);

            //inicializarComponentesPopUp();
            btnOkCerrarApp = (Button) dialogView.findViewById(R.id.btn_ok_cerrar_app);
            btnCancelCerrarApp = (Button) dialogView.findViewById(R.id.btn_cancel_cerrar_app);

            final AlertDialog dialog = builder.create();

            //Eventos Clic
            btnOkCerrarApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //moveTaskToBack(true);
                    //dialog.cancel();
                    //Process.killProcess(Process.myPid());
                    //System.exit(0);
                    finishAffinity();

                }
            });

            btnCancelCerrarApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            dialog.show();

            //---------------------------------------------------------------------------
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_drawer_options_menu, menu);
        //Recuperamos el dato pasado por el activity anterior
        sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        login_way = sharedpreferences.getString("enter_mode","EnterModeEnCasoDeNull");
        //Decidimos Items de OptionsMenu
        if(login_way.equals("login")){
            /*MenuItem optionItemConocenos1 = menu.findItem(R.id.optionItemConocenos1);  optionItemConocenos1.setVisible(false);
            MenuItem optionItemConocenos2 = menu.findItem(R.id.optionItemConocenos2);  optionItemConocenos2.setVisible(false);
            MenuItem optionItemConocenos3 = menu.findItem(R.id.optionItemConocenos3);  optionItemConocenos3.setVisible(false);*/
        }
        if(login_way.equals("conocenos")){
           /* MenuItem optionItemLogin1 = menu.findItem(R.id.optionItemLogin1);  optionItemLogin1.setVisible(false);
            MenuItem optionItemLogin2 = menu.findItem(R.id.optionItemLogin2);  optionItemLogin2.setVisible(false);
            MenuItem optionItemLogin3 = menu.findItem(R.id.optionItemLogin3);  optionItemLogin3.setVisible(false);*/
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.optionItemLogin1) {
            return true;
        } else if (id == R.id.optionItemLogin2) {
            return true;
        } else if (id == R.id.optionItemLogin3) {
            return true;
        } else if (id == R.id.optionItemConocenos1) {
            return true;
        } else if (id == R.id.optionItemConocenos2) {
            return true;
        } else if (id == R.id.optionItemConocenos3) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home_item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor_nav_drawer, new HomeFragment()).commit();
        } else if (id == R.id.etis) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor_nav_drawer, new EtisFragment()).commit();
        } else if (id == R.id.logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(NavDrawerActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.popup_cerrar_cesion,null);
            builder.setView(dialogView);

            //inicializarComponentesPopUp();
            btnSiLogout = (Button) dialogView.findViewById(R.id.btn_si_logout);
            btnNoLogout = (Button) dialogView.findViewById(R.id.btn_no_logout);

            final AlertDialog dialog = builder.create();

            //Eventos Clic
            btnSiLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cerrarSesion();
                    finish();
                }
            });
            btnNoLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            dialog.show();

            //---------------------------------------------------------------------------

        } else if (id == R.id.quienes_somos) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor_nav_drawer, new QuienesSomosFragment()).commit();
        } else if (id == R.id.historias_exito) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor_nav_drawer, new HistoriasExitoFragment()).commit();
        } else if (id == R.id.ubicanos) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor_nav_drawer, new UbicanosFragment()).commit();
        } else if (id == R.id.contactanos) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contenedor_nav_drawer, new ContactanosFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);

        login_way = sharedpreferences.getString("enter_mode","EnterModeEnCasoDeNull");

        if (login_way.equals("login")) {
            Menu menu = navigationView.getMenu();
            MenuItem itemHome = menu.findItem(R.id.home_item);
            MenuItem itemEtis = menu.findItem(R.id.etis);
            if (itemEtis.isChecked()) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contenedor_nav_drawer, new EtisFragment()).commit();
            }
        }
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void inicializarDatosUsuario(NavigationView navigationView) {
        View hview = navigationView.getHeaderView(0);
        TextView nombreMenuLat = (TextView) hview.findViewById(R.id.tvNombreUser);    nombreMenuLat.setText(nombre_usuario+" "+apellido_usuario);
        TextView correoMenuLat = (TextView) hview.findViewById(R.id.tvCorreoUser);    correoMenuLat.setText(correo_usuario);
    }

    private void alert(String s) {
        Toast.makeText(NavDrawerActivity.this,s,Toast.LENGTH_SHORT).show();
    }

    public void decideItemsMenuLateral(NavigationView navigationView, String login_way){
        Menu menu = navigationView.getMenu();
        if(login_way.equals("login")){
            MenuItem itemQuienesSomos = menu.findItem(R.id.quienes_somos);  itemQuienesSomos.setVisible(false);
            MenuItem itemHistoriasExito = menu.findItem(R.id.historias_exito);  itemHistoriasExito.setVisible(false);
            MenuItem itemUbicanos = menu.findItem(R.id.ubicanos);  itemUbicanos.setVisible(false);
            MenuItem itemContactanos = menu.findItem(R.id.contactanos);  itemContactanos.setVisible(false);
            MenuItem itemHome = menu.findItem(R.id.home_item);   itemHome.setChecked(true);


        }
        if(login_way.equals("conocenos")){
            MenuItem itemQuienesSomos = menu.findItem(R.id.quienes_somos);  itemQuienesSomos.setVisible(false);
            MenuItem itemHistoriasExito = menu.findItem(R.id.historias_exito);  itemHistoriasExito.setVisible(false);
            MenuItem itemUbicanos = menu.findItem(R.id.ubicanos);  itemUbicanos.setVisible(false);
            MenuItem itemContactanos = menu.findItem(R.id.contactanos);  itemContactanos.setVisible(false);
            MenuItem itemEtis = menu.findItem(R.id.etis);  itemEtis.setVisible(false);
            MenuItem itemHome = menu.findItem(R.id.home_item);   itemHome.setChecked(true);

        }
    }

    public void cerrarSesion(){
        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    /*public static class FrameDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            if(modeAlert.equals("logout")) {
                builder.setMessage(R.string.logout_dialog_message)
                        .setPositiveButton(R.string.logout_dialog_si, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.clear();
                                editor.commit();
                            }
                        })
                        .setNegativeButton(R.string.logout_dialog_no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.cancel();
                            }
                        });
            }
            if(modeAlert.equals("backpressed")){
                builder.setMessage(R.string.backpressed_dialog_message)
                        .setPositiveButton(R.string.backpressed_dialog_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setNegativeButton(R.string.backpressed_dialog_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.cancel();
                            }
                        });
            }
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }*/


}
