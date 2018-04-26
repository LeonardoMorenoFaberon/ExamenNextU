package leomoreno.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.CallbackManager;

public class LoginActivity extends AppCompatActivity {


    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create(); //  esta clase callbackManager la usamos para detectar las acciones o eventos de este boton loginButton

    }
}
