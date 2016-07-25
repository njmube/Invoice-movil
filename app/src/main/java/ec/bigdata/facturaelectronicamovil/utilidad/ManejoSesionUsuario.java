package ec.bigdata.facturaelectronicamovil.utilidad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import ec.bigdata.facturaelectronicamovil.login.Inicio;

/**
 * Created by DavidLeonardo on 21/7/2016.
 */
public class ManejoSesionUsuario {

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;

    // Context
    Context context;

    //Nombre de la preferencia compartida
    private static final String SHARED_PREFERENCES_NAME = "PreferenciasApp";

    //Llaves
    private static final String LLAVE_EXISTE_SESION_USUARIO = "SesionUsurarioActiva";

    public static final String LLAVE_NOMBRE_USUARIO = "nombreUsuario";

    public static final String LLAVE_CLAVE = "claveUsuario";

    public ManejoSesionUsuario(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
    }

    //Crea la sesión del usuario
    public void createUserLoginSession(String nombreUsuario, String claveUsuario) {
        //Se pone el estado de la sesión a true
        editor.putBoolean(LLAVE_EXISTE_SESION_USUARIO, true);

        //Almacena el nombre y la clave del usuario
        editor.putString(LLAVE_NOMBRE_USUARIO, nombreUsuario);
        editor.putString(LLAVE_CLAVE, claveUsuario);

        //Guarda los cambios
        editor.commit();
    }

    /**
     * Verifica el estaod de la sesión del usuario si es falso le direcciona a la página de inicio caso contrario no hace nada
     *
     * @return
     */
    public boolean verificarInicioSesion() {
        // Check login status
        if (!this.estaActivaSesionUsuario()) {

            //Direcciona al inicio
            Intent intent = new Intent(context, Inicio.class);

            //Cierra todas las actividades
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Agrega una nueva bandera para empezar una nueva actividad
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);

            return true;
        }
        return false;
    }

    // Check for login
    public boolean estaActivaSesionUsuario() {
        return sharedPreferences.getBoolean(LLAVE_EXISTE_SESION_USUARIO, false);
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> obtenerDetallesSesionUsuario() {


        HashMap<String, String> stringHashMapUsuario = new HashMap<String, String>();


        stringHashMapUsuario.put(LLAVE_NOMBRE_USUARIO, sharedPreferences.getString(LLAVE_NOMBRE_USUARIO, null));


        stringHashMapUsuario.put(LLAVE_CLAVE, sharedPreferences.getString(LLAVE_CLAVE, null));


        return stringHashMapUsuario;
    }
}
