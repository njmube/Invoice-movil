package ec.bigdata.facturaelectronicamovil.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestComprobanteElectronico;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Inicio extends AppCompatActivity {

    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

    }

    public void irInicioSesionEmpresa(View view) {
        Intent intent = new Intent(this, LoginEmpresaActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);

    }

    public void enviar(View view) {

        byte[] targetArray = new byte[0];
        RequestBody description = null;
        MultipartBody.Part body = null;
        try {
            FileOutputStream fOut = openFileOutput("file.txt", Context.MODE_PRIVATE);
            String str = "1904201607179086431600120010360000094491790864315\n" +
                    "1904201607179086431600120010360000094481790864311\n" +
                    "1904201607179001093700120010560000267440726627111\n" +
                    "HOLA DAVID CRESPÍN VERA";
            fOut.write(str.getBytes());
            fOut.close();

            File file = new File(getFilesDir() + "/" + "file.txt");
            if (file.exists()) {
                Toast.makeText(this, "El archivo existe y pesa" + file.length(), Toast.LENGTH_SHORT).show();
            }


            /*targetArray = new byte[inputStream.available()];
            inputStream.read(targetArray);
            s=new String(targetArray);
            uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://" +getResources().getResourcePackageName(R.raw.factura_con_ice) + "/" + getResources().getResourceTypeName(R.raw.factura_con_ice) + "/" + getResources().getResourceEntryName(R.raw.factura_con_ice) );
            File file=new File(uri.toString());

            BufferedReader lector=new BufferedReader(new InputStreamReader(inputStream));

            String texto = lector.readLine();
            StringBuilder sb=new StringBuilder();
            while(texto!=null)
            {sb.append(texto);

                texto=lector.readLine();
            }
            inputStream.close();
            lector.close();

        Toast.makeText(this,sb.toString(),Toast.LENGTH_SHORT).show();*/

            // create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            body =
                    MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            // add another part within the multipart request
            String descriptionString = "A/B/C/D/E/F/G/H/I";
            description =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), descriptionString);

        } catch (IOException e) {
            e.printStackTrace();
        }


        ClienteRestComprobanteElectronico.ServicioComprobanteElectronico servicioArchivo = ClienteRestComprobanteElectronico.getServicioComprobanteElectronico();
      /*  Call<ResponseBody> responseBodyCall=servicioArchivo.archivo(description,body);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        Toast.makeText(getApplicationContext(),""+response.body().string(),Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{

                        Log.i("", response.body().toString());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("",t.getMessage());
            }
        });*/

    }

    public void irInicioSesionPersona(View view) {
        Intent intent = new Intent(this, LoginUsuarioActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);

    }

}
