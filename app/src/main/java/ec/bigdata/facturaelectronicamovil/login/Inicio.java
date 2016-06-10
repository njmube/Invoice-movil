package ec.bigdata.facturaelectronicamovil.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ec.bigdata.facturaelectronicamovil.R;

public class Inicio extends AppCompatActivity {


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

    public void irInicioSesionPersona(View view) {
        Intent intent = new Intent(this, LoginUsuarioActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);

    }
}
