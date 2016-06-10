package ec.bigdata.facturaelectronicamovil.test;

/**
 * Created by DavidLeonardo on 22/3/2016.
 */
public class Prueba {


    /*
            Usando Gson
          try {
                Request request = params[0];

                Response response = okHttpClient.newCall(request).execute();

                return response.body().string();
            } catch (Exception e) {
                Log.e(TAG, "ERROR CONSULTA WEB SERVICE VALIDACIÓN USUARIO" + e);
                return "";
            }*/


     /*
            Usando Gson
            Gson gson = new GsonBuilder().registerTypeAdapter(UsuarioAcceso.class,new AdaptadorUsuario()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            UsuarioAcceso ua = gson.fromJson(result, UsuarioAcceso.class);
            if (ua != null) {
                if (ua.getEstadoUsuario().equals("1")) {
                    claseGlobalUsuario.setIdUsuario(String.valueOf(ua.getIdUsuario()));
                    claseGlobalUsuario.setIdentificacionUsuario(ua.getIdentificacionUsuario());
                    claseGlobalUsuario.setNombreUsuario(ua.getNombreUsuarioAcceso());
                    claseGlobalUsuario.setClaveUsuario(ua.getClaveUsuarioAcceso());
                    claseGlobalUsuario.setNombres(ua.getNombreUsuario());
                    claseGlobalUsuario.setApellidos(ua.getApellidoUsuario());
                    claseGlobalUsuario.setCorreoPrincipal(ua.getCorreoPrincipalUsuario());
                    if(ua.getCorreoAdicionalUsuario()!=null) {
                        claseGlobalUsuario.setCorreoAdicional(ua.getCorreoAdicionalUsuario());
                    }else{
                        claseGlobalUsuario.setCorreoAdicional("N/A");
                    }
                    claseGlobalUsuario.setTelefonoPrincipal(ua.getTelefonoPrincipalUsuario());
                    if(ua.getTelefonoAdicionalUsuario()!=null){
                        claseGlobalUsuario.setTelefonoAdicional(ua.getTelefonoAdicionalUsuario());
                    }else{
                        claseGlobalUsuario.setTelefonoAdicional("N/A");
                    }
                    claseGlobalUsuario.setIdPerfil(String.valueOf(ua.getPerfil().getIdPerfil()));
                    claseGlobalUsuario.setTipoPerfil(ua.getPerfil().getNombrePerfil());

                    Intent intent = new Intent(getBaseContext(), NavigationDrawer.class);
                    //intent.putExtra("usuario",ua);
                    startActivity(intent);
                    finish();
                    Toast.makeText(LoginUsuarioActivity.this, "Bienvenido: "+ua.getNombreUsuarioAcceso(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginUsuarioActivity.this, "Su cuenta ha sido desactivada..", Toast.LENGTH_SHORT).show();
                    editTextNombreUsuario.requestFocus();
                }
            } else {
                Toast.makeText(LoginUsuarioActivity.this, "El usuario no existe.", Toast.LENGTH_SHORT).show();
                editTextNombreUsuario.requestFocus();

            }*/
}
