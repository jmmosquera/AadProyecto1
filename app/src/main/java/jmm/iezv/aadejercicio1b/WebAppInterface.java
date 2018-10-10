package jmm.iezv.aadejercicio1b;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class WebAppInterface extends MainActivity{

    private String usuario, clave;
    private int existe;

    public WebAppInterface() {
    }

    @JavascriptInterface
    public void setExiste(int existe){
        this.existe = existe;
    }

    @JavascriptInterface
    public void sendData(String usuario, String clave){
        setUsuario(usuario);
        setClave(clave);

    }

    @JavascriptInterface
    public void echo(String punto){
        Log.v("DVLTEST", punto);
    }

    public String getUsuario(){
        return usuario;
    }

    public String getClave(){
        return clave;
    }

    public int getExiste(){
        return existe;
    }

    public void setUsuario(String usuario){
        this.usuario =  usuario;
    }

    public void setClave(String clave){
        this.clave = clave;
    }


    //Este método debería permitir almacenar directamente usuario y clave en SharedPreferences
    //Por desgracia, es imposible utilizar SharedPreferences desde esta clase.
    //Conservo este código por si pudiera ser útil de cara a futuras versiones.
    /*@JavascriptInterface
    public void guardaExiste(int ex){
        String existe = Integer.toString(ex);
        SharedPreferences pref = this.getSharedPreferences(getString(R.string.archivoSharedPreferences),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.existe), existe);
        editor.commit();
    }*/

}
