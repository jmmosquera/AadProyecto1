package jmm.iezv.aadejercicio1b;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class screen_log extends AppCompatActivity {

    public static String moodleUser, moodlePass, existe;
    private Button btSave;
    private EditText etName, etPass;
    private WebAppInterface wai;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_log);
        etName = findViewById(R.id.et_name);
        etPass = findViewById(R.id.et_pass);
        btSave = findViewById(R.id.bt_savelogin);
        cargar();
        btSave.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                moodleUser = etName.getText().toString();
                moodlePass = etPass.getText().toString();
                guardaDatos(moodleUser, moodlePass);
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.user_saved), Toast.LENGTH_LONG).show();
            }
        });

        if(existe.equals("1")){
            etName.setBackgroundColor(getResources().getColor(R.color.successLogin));
            etPass.setBackgroundColor(getResources().getColor(R.color.successLogin));
        }
        if(existe.equals("0")){
            etName.setBackgroundColor(getResources().getColor(R.color.errorLogin));
            etPass.setBackgroundColor(getResources().getColor(R.color.errorLogin));
        }

        //Este método debería permitir guardar la información acerca de si existe o no el usuario.
        //Por desgracia, es imposible guardar variables de la interfaz wai en SharedPreferences
        //Conservo este código por si fuera posible implementarlo en futuras versiones
        /*String existeCache = Integer.toString(wai.getExiste());
        if(!existeCache.equals("")){
            guardaExiste(Integer.parseInt(existeCache));
        }*/

    }

    private void cargar(){
        etName.setText(traerUsuario());
        etPass.setText(traerClave());
        //Debido a que no hay forma de almacenar la información acerca de su validez,
        //esta variable siempre será nula y nunca se aplicarán los colores a los campos.
        existe = traerExiste();
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

    private String traerUsuario(){
        SharedPreferences pref = getSharedPreferences(getString(R.string.archivoSharedPreferences),Context.MODE_PRIVATE);
        String v = pref.getString(getString(R.string.usuario),"");
        return v;
    }

    private String traerClave(){
        SharedPreferences pref = getSharedPreferences(getString(R.string.archivoSharedPreferences),Context.MODE_PRIVATE);
        String v = pref.getString(getString(R.string.clave),"");
        return v;
    }

    private String traerExiste(){
        SharedPreferences pref = getSharedPreferences(getString(R.string.archivoSharedPreferences),Context.MODE_PRIVATE);
        String v = pref.getString(getString(R.string.existe),"");
        return v;
    }
}
