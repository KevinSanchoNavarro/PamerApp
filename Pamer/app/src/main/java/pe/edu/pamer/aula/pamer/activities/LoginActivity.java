package pe.edu.pamer.aula.pamer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import pe.edu.pamer.aula.pamer.R;
import pe.edu.pamer.aula.pamer.network.FirebaseAuthenticateService;

import static pe.edu.pamer.aula.pamer.network.HostingPamerServices.UNIVERSAL_URL;
//import pe.edu.pamer.aula.pamer.fragments.ConocenosLoginDialogFragment;

public class LoginActivity extends AppCompatActivity {

    private Button buttonSignIn, buttonConocenos;
    private EditText editTextEmail;
    private EditText editTextPass;
    FirebaseAuth auth;
    FirebaseUser user;
    public static String TAG = "Pamer";

    //Popup-Conocenos-----------------------------------------
    public LoginButton btnLoginFacebook;
    public SignInButton btnLoginGo;
    public Button btnCerrar;
    final static int RC_SIGN_IN = 2;
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager callbackManager;
    public int metodoLogueo = 0;
    //--------------------------------------------------

    //Shared Preferences--------------------------------------
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String username = "username";
    public static final String password = "password";
    SharedPreferences sharedpreferences;
    //--------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.argb(255, 41, 169, 223));
        }*/

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        inicializarComponentes();

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String correo = sharedpreferences.getString("username","usernameEnCasoDeNull");

        if(!correo.equals("usernameEnCasoDeNull")){
            Intent i = new Intent(LoginActivity.this,NavDrawerActivity.class);
            startActivity(i);
        }

        inicializarFirebaseCallback();
        eventoClicks();
        //configureGoogleSignIn();


        // #########################  PARA LOGUEO CON CUENTA GOOGLE  #########################
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this, "Email or Password incorrects", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // ##################################################################################

    }

    private void configureGoogleSignIn(){

    }

    private void inicializarFirebaseCallback() {
        auth = FirebaseAuthenticateService.getFirebaseAuth();
        user = FirebaseAuthenticateService.getFirebaseUser();
        callbackManager = CallbackManager.Factory.create();
    }

    private void alert(String s) {
        Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
    }

    private void signInGo() {
        mGoogleApiClient.clearDefaultAccountAndReconnect();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(metodoLogueo == 1){
            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
        if(metodoLogueo == 2){
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    Toast.makeText(LoginActivity.this, "Auth went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("enter_mode","conocenos");
                            editor.putString("username", "usuarioGo");
                            editor.commit();
                            Intent i = new Intent(LoginActivity.this, NavDrawerActivity.class);
                            String enterMode = "conocenos";
                            i.putExtra("enter_mode",enterMode);
                            startActivity(i);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //Método de encriptación SHA1---------------------------------------------------------------
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String SHA1(String textToConvert) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = textToConvert.getBytes("UTF-8");
        md.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
    //------------------------------------------------------------------------------------------


    private void eventoClicks() {
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate(editTextEmail, editTextPass)) {
                    String tag = "login";
                    final String email = editTextEmail.getText().toString().trim();   //CorreoSinEncriptar
                    final String pass = editTextPass.getText().toString().trim();     //PasswordSinEncriptar
                    String varStatic = "f3bd7fd075e3d803d9c4b4db36816dec518fbc70";   //Variable estática utilizada en el encriptado
                    String userSHA1 = "", claveSHA1 = "", userBase64 = "", claveBase64 = "";

                    try {
                        //Encriptando SHA1
                        userSHA1 = SHA1(varStatic + email);
                        claveSHA1 = SHA1(varStatic + pass);

                        //Encriptando Base64
                        byte[] data = email.getBytes("UTF-8");
                        userBase64 = Base64.encodeToString(data, Base64.DEFAULT);
                        byte[] data2 = pass.getBytes("UTF-8");
                        claveBase64 = Base64.encodeToString(data2, Base64.DEFAULT);

                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String varApp = "O";
                    String varNavName = "";
                    String varNavVer = "";
                    String varNavVendor = "";
                    String varNacPais = "";
                    String varNacRegion = "";
                    String varNacCiudad = "";
                    String decGpsLat = "";
                    String decGpsLng = "";
                    String varOSLang = "";
                    String varOSName = "";
                    String varOSProcess = "";
                    String varOSPlatform = "";
                    String varIPPublica = "";
                    String varIPServidor = "";
                    String varMacAddress = "";
                    String intSwCookie = "";
                    String intSwMovil = "";

                    //Envío el request GET
                    AndroidNetworking.get(UNIVERSAL_URL)
                            .setTag(TAG)
                            .setPriority(Priority.LOW)
                            .addQueryParameter("tag", "login")
                            .addQueryParameter("varUsuarioEnc", userSHA1)
                            .addQueryParameter("varContrasenia", claveSHA1)
                            .addQueryParameter("varAPP", varApp)
                            .addQueryParameter("varUsuario", userBase64)
                            .addQueryParameter("varContraseniaes", claveBase64)
                            .addQueryParameter("varNavname", varNavName)
                            .addQueryParameter("varNavver", varNavVer)
                            .addQueryParameter("varNavvendor", varNavVendor)
                            .addQueryParameter("varNacpais", varNacPais)
                            .addQueryParameter("varNacregion", varNacRegion)
                            .addQueryParameter("varNacciudad", varNacCiudad)
                            .addQueryParameter("decGpslat", decGpsLat)
                            .addQueryParameter("decGpslng", decGpsLng)
                            .addQueryParameter("varOslang", varOSLang)
                            .addQueryParameter("varOsname",varOSName)
                            .addQueryParameter("varOsproccess", varOSProcess)
                            .addQueryParameter("varOsplatform", varOSPlatform)
                            .addQueryParameter("varIppublica", varIPPublica)
                            .addQueryParameter("varIpservidor", varIPServidor)
                            .addQueryParameter("varMacaddress", varMacAddress)
                            .addQueryParameter("intSwcookie", intSwCookie)
                            .addQueryParameter("intSwmovil", intSwMovil)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {

                                        if((response.getString("message")).equals("NO OK")) {
                                            Log.d(TAG, response.getString("message"));
                                            alert("Correo y/o password incorrectos");
                                            return;
                                        }

                                        SharedPreferences.Editor editor = sharedpreferences.edit();

                                        editor.putString(username, email);
                                        editor.putString(password, pass);
                                        editor.putString("enter_mode","login");
                                        editor.putString("user_id",response.getJSONObject("data").getString("id"));
                                        editor.putString("user_codigo",response.getJSONObject("data").getString("codigo"));
                                        editor.putString("user_correo",response.getJSONObject("data").getString("correo"));
                                        editor.putString("user_nombre",response.getJSONObject("data").getString("nombre"));
                                        editor.putString("user_apellido",response.getJSONObject("data").getString("apellido"));
                                        editor.putString("user_dni",response.getJSONObject("data").getString("dni"));
                                        editor.putString("user_salon",response.getJSONObject("data").getString("salon"));
                                        editor.putString("user_colegio",response.getJSONObject("data").getString("colegio"));
                                        editor.putString("user_grado",response.getJSONObject("data").getString("grado"));
                                        editor.putString("user_codGrado",response.getJSONObject("data").getString("codgrado"));
                                        editor.putString("user_idSalon",String.valueOf(response.getJSONObject("data").getInt("idsalon")));
                                        editor.putString("user_idPerfil",response.getJSONObject("data").getString("idPerfil"));
                                        editor.putString("user_idPerfiles",response.getJSONObject("data").getString("idperfiles"));
                                        editor.putString("user_tutor",response.getJSONObject("data").getString("tutor"));
                                        editor.putString("user_imgCache",response.getJSONObject("data").getString("imgcache"));

                                        editor.commit();

                                        Intent i = new Intent(LoginActivity.this, NavDrawerActivity.class);
                                        String enterMode = "login";
                                        i.putExtra("enter_mode",enterMode);
                                        startActivity(i);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.d(TAG, anError.getLocalizedMessage());
                                    alert("No hay respuesta del hosting");
                                }
                            });

                    //login(email, pass);
                    //editTextPass.setText("");

                }
            }
        });

        buttonConocenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.popup_conocenos,null);
                builder.setView(dialogView);

                //inicializarComponentesPopUp();
                btnLoginFacebook = (LoginButton) dialogView.findViewById(R.id.btnLoginFacebook);
                btnLoginFacebook.setReadPermissions("email", "public_profile");
                btnLoginGo = (SignInButton) dialogView.findViewById(R.id.btnLoginGo);
                btnCerrar = (Button) dialogView.findViewById(R.id.btnCancelar);

                final AlertDialog dialog = builder.create();

                //eventoClicksPopUp();
                btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        metodoLogueo = 1;
                        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                firebaseAuthWithFb(loginResult.getAccessToken());
                            }

                            @Override
                            public void onCancel() {
                                alert("Operacion Cancelada");
                            }

                            @Override
                            public void onError(FacebookException error) {
                                alert("Error al intentar ingresar con Facebook");
                            }
                        });
                    }
                });

                btnLoginGo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //auth.removeAuthStateListener();
                        metodoLogueo = 2;
                        signInGo();
                    }
                });

                btnCerrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.show();

            }
        });

    }

    private void firebaseAuthWithFb(AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        //AuthCredential credential1 = FacebookAuthProvider.
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("enter_mode","conocenos");
                            editor.putString("username", "usuarioFb");
                            editor.commit();

                            Intent i = new Intent(LoginActivity.this, NavDrawerActivity.class);
                            String enterMode = "conocenos";
                            i.putExtra("enter_mode",enterMode);
                            startActivity(i);
                        } else {
                            alert("Error de authenticacion con Firebase!");
                        }

                    }
                });

    }

    private void login(String email, String pass) {
        auth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent i = new Intent(LoginActivity.this, NavDrawerActivity.class);
                            String enterMode = "login";
                            i.putExtra("enter_mode",enterMode);
                            startActivity(i);
                        }else{
                            alert("Correo o Password incorrectos");
                        }
                    }
                });
    }

    private void inicializarComponentes() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail); editTextEmail.setText("");
        editTextPass = (EditText) findViewById(R.id.editTextPass);   editTextPass.setText("");
        buttonSignIn = (Button) findViewById(R.id.buttonSignin);
        buttonConocenos = (Button) findViewById(R.id.buttonConocenos);
    }

    private boolean validate(EditText et_email, EditText et_password) {

        boolean valid = true;
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("enter a valid email address");
            valid = false;
        } else {
            et_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 15) {
            et_password.setError("between 4 and 15 alphanumeric characters");
            valid = false;
        } else {
            et_password.setError(null);
        }

        return valid;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextPass = (EditText) findViewById(R.id.editTextPass);   editTextPass.setText("");
    }
}
