package ec.bigdata.facturaelectronicamovil.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;

/**
 * Created by DavidLeonardo on 9/8/2016.
 */
public class DialogEnvioCorreo extends DialogFragment implements Validator.ValidationListener {


    @NotEmpty(message = "El correo electrónico de destino no puede estar vacío.")
    private EditText editTextCorreoElectronicoPara;

    @NotEmpty(message = "El asunto no puede estar vacío.")
    private EditText editTextAsunto;

    @NotEmpty(message = "El contenido del correo no puede estar vacío.")
    private EditText editTextContenido;

    private String correoElectronicoDe;

    private String correoElectronicoPara;

    private DialogEnvioCorreoComunicacion dialogEnvioCorreoComunicacion;

    private Validator validator;

    public DialogEnvioCorreo() {
    }

    public interface DialogEnvioCorreoComunicacion {
        void enviarCorreo(String correoElectronicoPara, String asunto, String contenido);
    }

    public static DialogEnvioCorreo newInstance(String correoElectronicoPara) {

        DialogEnvioCorreo dialogEnvioCorreo = new DialogEnvioCorreo();
        Bundle args = new Bundle();

        args.putString("correoElectronicoPara", correoElectronicoPara);
        dialogEnvioCorreo.setArguments(args);
        return dialogEnvioCorreo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearDialogEnviarCorreo();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        correoElectronicoPara = getArguments().getString("correoElectronicoPara");
        dialogEnvioCorreoComunicacion = (DialogEnvioCorreoComunicacion) getActivity();

    }

    public AlertDialog crearDialogEnviarCorreo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_envio_correo, null);

        builder.setView(v);

        editTextCorreoElectronicoPara = (EditText) v.findViewById(R.id.edit_text_correo_destino);

        editTextCorreoElectronicoPara.setText(correoElectronicoPara);

        editTextAsunto = (EditText) v.findViewById(R.id.edit_text_asunto);

        editTextContenido = (EditText) v.findViewById(R.id.edit_text_contenido);

        Button aceptar = (Button) v.findViewById(R.id.button_aceptar);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        Button cancelar = (Button) v.findViewById(R.id.button_cancelar);


        cancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dismiss();
                    }
                }
        );

        validator = new Validator(this);
        validator.setValidationListener(this);

        return builder.create();
    }

    @Override
    public void onValidationSucceeded() {
        dialogEnvioCorreoComunicacion.enviarCorreo(editTextCorreoElectronicoPara.getText().toString(), editTextAsunto.getText().toString(), editTextContenido.getText().toString());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                MensajePersonalizado.mostrarToastPersonalizado(getContext(), getActivity().getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, message);
            }
        }
    }
}
