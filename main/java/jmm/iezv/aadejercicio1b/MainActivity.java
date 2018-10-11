package jmm.iezv.aadejercicio1b;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static jmm.iezv.aadejercicio1b.R.string.getsharedpreferences;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MITAG";
    private Button btMoodle, toLogin;
    private WebView webMoodle;
    private EditText etUserName, etPassword;
    private WebAppInterface wai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btMoodle = findViewById(R.id.btModle);
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        webMoodle = findViewById(R.id.web_moodle);
        webMoodle.getSettings().setJavaScriptEnabled(true);

        etUserName.setText(traerUsuario());
        etPassword.setText(traerClave());

        String existe = traerExiste();
        marcarColores(existe);
        eventHandler();
    }

    private void eventHandler(){
        btMoodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(MainActivity.this);
                visitarMoodle();
            }
        });
    }

    private void guardaDatos(String usuario, String clave){
        SharedPreferences pref = this.getSharedPreferences(getString(R.string.archivoSharedPreferences),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.usuario), usuario);
        editor.putString(getString(R.string.clave), clave);
        editor.commit();
    }

    private void guardaExiste(int ex){
        String existe = Integer.toString(ex);
        SharedPreferences pref = this.getSharedPreferences(getString(R.string.archivoSharedPreferences),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.existe), existe);
        editor.commit();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    //Esta función marcará en verde o rojo la pantalla de login según si la contraseña es o no válida.
    private void marcarColores(String existe){
        if(existe.equals("1")){
            etUserName.setBackgroundColor(getResources().getColor(R.color.successLogin));
            etPassword.setBackgroundColor(getResources().getColor(R.color.successLogin));
        }
        if(existe.equals("0")){
            etUserName.setBackgroundColor(getResources().getColor(R.color.errorLogin));
            etPassword.setBackgroundColor(getResources().getColor(R.color.errorLogin));
        }
    }

    //Accede a un nombre de usuario almacenado en SharedPreferences
    private String traerUsuario(){
        SharedPreferences pref = getSharedPreferences(getString(R.string.archivoSharedPreferences),Context.MODE_PRIVATE);
        String v = pref.getString(getString(R.string.usuario),"");
        return v;
    }

    //Accede a una clave almacenada en SharedPreferences
    private String traerClave(){
        SharedPreferences pref = getSharedPreferences(getString(R.string.archivoSharedPreferences),Context.MODE_PRIVATE);
        String v = pref.getString(getString(R.string.clave),"");
        return v;
    }

    //Permite comprobar si una cuenta de usuario existe
    private String traerExiste(){
        SharedPreferences pref = getSharedPreferences(getString(R.string.archivoSharedPreferences),Context.MODE_PRIVATE);
        String v = pref.getString(getString(R.string.existe),"");
        return v;
    }

    //Permite abrir moodle desde un webview
    private void visitarMoodle(){
        guardaDatos(etUserName.getText().toString(), etPassword.getText().toString());
        wai = new WebAppInterface();
        webMoodle.addJavascriptInterface(wai, "android");

        //En caso de no haber ningún usuario almacenado, la aplicación te llevará a la portada de la web.
        if(traerUsuario().equals("")&&traerClave().equals("")){
            webMoodle.loadUrl("http://www.juntadeandalucia.es/averroes/centros-tic/18700098/moodle2/");
            webMoodle.setWebViewClient(new WebViewClient());
            //En caso de haber algún usuario almacenado y ser válido, la aplicación tratará de loguearse automáticamente.
        }else{
            //Datos de conexión de la práctica.
            //USUARIO MOODLE: aad CONTRASEÑA: 1234
            webMoodle.loadUrl("http://www.juntadeandalucia.es/averroes/centros-tic/18700098/moodle2/login/index.php");
            webMoodle.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    String javaScript = " var boton = document.getElementById('loginbtn');\n" +
                            " var nombre = document.getElementById('username');\n" +
                            " var pass = document.getElementById('password');\n" +
                            //loginerrormessage es un campo que aparece únicamente si los datos de conexión son incorrectos.
                            " var error = document.getElementById('loginerrormessage');\n" +
                            //int5 es un campo que aparece únicamente si los datos de conexión son correctos.
                            " var inst5 = document.getElementById('inst5');\n" +
                            " if(inst5!=null){\n"+
                            "   android.setExiste(1);\n"+
                            " }\n"+
                            " if(error!=null){\n"+
                            //En caso de detectar un error, el script se preparará para guardar los datos pero no tratará de iniciar sesión automáticamente.
                            "   android.setExiste(0);\n"+
                            "   boton.addEventListener('click', function(e){\n " +
                            //Utilizo e.prevent default para modificar el comportamiento por defecto del formulario.
                            //En caso de no utilizarlo, la página se logueará antes de llegar a enviar los datos a la interfaz.
                            "       e.preventDefault();\n"+
                            "       var usuario = nombre.value;\n" +
                            "       var clave = pass.value;\n" +
                            "       android.sendData(usuario,clave);\n" +
                            "       document.getElementById('login').submit();\n" +
                            "   });" +
                            " }\n"+
                            " if(error==null){\n"+
                            "   boton.addEventListener('click', function(e){\n " +
                            "       e.preventDefault();\n"+
                            "       nombre.value = '"+traerUsuario()+"';\n"+
                            "       pass.value = '"+traerClave()+"';\n" +
                            "       var usuario = nombre.value;\n" +
                            "       var clave = pass.value;\n" +
                            "       android.sendData(usuario,clave);\n" +
                            "       document.getElementById('login').submit();\n" +
                            "   });" +
                            "   boton.click();"+
                            " }";
                    if(url.equals("http://www.juntadeandalucia.es/averroes/centros-tic/18700098/moodle2/")){
                        guardaExiste(1);
                        marcarColores("1");
                    }else if(url.equals("http://www.juntadeandalucia.es/averroes/centros-tic/18700098/moodle2/login/index.php")){
                        marcarColores("0");
                        guardaExiste(0);
                    }
                    webMoodle.loadUrl("javascript: "+ javaScript);
                }
            });
        }
    }


}
