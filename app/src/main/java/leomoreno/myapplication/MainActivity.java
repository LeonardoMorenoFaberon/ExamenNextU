package leomoreno.myapplication;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
//    https://www.youtube.com/watch?v=cZBj8MQTonI

    //Agregar el adView para banner-----inicio
    private AdView mAdView;
    //Agregar el adView para banner-----fin

    //Intersticial adView
    InterstitialAd mInterstitialAd;
    //Intersticial adView


    CallbackManager callbackManager;
    LoginButton loginButton;

    TextView txtEmail, txtBirthday, txtFriends;
    Button buttonVerificando;
    ProfilePictureView profilePictureView;

    ImageView avatarImage;

    //BANNER____INICIO
    AdView adView;  //publicidad tipo Banner
    InterstitialAd interstitialAd; //publicidad pantalla completa.
    Button botonBanner;
    //BANNER____FIN



    //____________________________________________________________________________
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    //____________________________________________________________________________

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        initializeControls();
        loginWithFB();

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                verificandoSesion2();
            }
        };

    } //onCreate(...)_____________________________________________________________

    private void loginWithFB() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                txtEmail.setText("Login Success:\n" + loginResult.getAccessToken());
                txtEmail.append("" + "\n" + callbackManager.toString());


            }

            @Override
            public void onCancel() {
                txtEmail.setText("Login Canceled");

            }

            @Override
            public void onError(FacebookException error) {

                txtEmail.setText("Login Error: " + error.getMessage());
            }
        });


    }


    public void verificandoSesion2() {
        if (isLoggedIn()) {
            txtFriends.setText("Actualmente tienes una sesion inciada");

        }
        if (!isLoggedIn()) {
            txtFriends.setText("Ya no Hay Sesion activa");
        }
    }

    public void verificandoSesion(View view) {
        if (isLoggedIn()) {
            txtFriends.setText("Actualmente tienes una sesion inciada");
            String url = Profile.getCurrentProfile().getProfilePictureUri(250, 250).toString();

        }
        if (!isLoggedIn()) {
            txtFriends.setText("Ya no Hay Sesion activa");
            avatarImage.setImageResource(R.mipmap.ic_launcher_round);
        }
    }


    //________________________________________________________________
    private void initializeControls() {

        callbackManager = CallbackManager.Factory.create();
        txtBirthday = (TextView) findViewById(R.id.txtBirthday);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtFriends = (TextView) findViewById(R.id.txtFriends);
        loginButton = (LoginButton) findViewById(R.id.loginButton);
        buttonVerificando = (Button) findViewById(R.id.buttonVerificando);
        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePictureView);
        avatarImage = (ImageView) findViewById(R.id.avatarImage);


        //agregando el Banner en el app______________inicio
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //agregando el Banner en el app______________fin


        //Interstitial Ad____________________________inicio
        mInterstitialAd = new InterstitialAd(this);
//        replace adUnitId with your ad id from admob website
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
                                          @Override
                                          public void onAdClosed() {
                                              super.onAdClosed();
                                              finish();
                                          }

                                      }
        );
        //Interstitial Ad____________________________fin
    }
    //________________________________________________________________
    public void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn´t loaded yet.");
            finish();
        }
    }
    //________________________________________________________________
    @Override
    public void onBackPressed() {
        //show ad of app exit.
        showInterstitial();
    }
    //________________________________________________________________

    /**
     * Comprueba si el usuario ha iniciado sesión en Facebook y el
     * token de acceso está activo
     *
     * @return
     */

    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return (accessToken != null) && (!accessToken.isExpired());
    }

    //____________________________________________________________________________
    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager()
                    .getPackageInfo("leomoreno.myapplication", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    //____________________________________________________________________________
//    Jorge Caro
//7:27 pm
//    dele click donde dice vcs en android studio
//
//    entre las opciones le aparece git
//
//    le da en share in github
//
//    le pedira su usuario, contraseña, nombre del repositorio
//
//7:28 pm
//    y despues le pedira que haga commit
//
//    y push
//
//    commit es subir los cambios en local
//
//    osea, quedan en su pc solamente
//
//    push es mandarlos al remoto
//
//    osea al repositorio en internet
//    tiene mas poder de lo que imagina
//
//7:29 pm
//    pero ahora es facil
//
//    porque es sencillo cuando se trabaja solo
//
//    su poder real se ve cuando se trabaja en grupo
    //https://www.xvideos.com/video11653627/busty_french_redhead_babe_deep_anal_fucked_with_cum_on_ass_for_her_casting_couch

}