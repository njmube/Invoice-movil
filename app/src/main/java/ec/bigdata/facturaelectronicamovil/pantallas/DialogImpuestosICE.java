package ec.bigdata.facturaelectronicamovil.pantallas;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterTarifasImpuesto;
import ec.bigdata.facturaelectronicamovil.interfaces.InterfaceDialogImpuestosICE;
import ec.bigdata.facturaelectronicamovil.modelo.TarifasImpuesto;
import ec.bigdata.facturaelectronicamovil.utilidades.Calculos;
import ec.bigdata.facturaelectronicamovil.utilidades.ClaseGlobalUsuario;

/**
 * Created by DavidLeonardo on 8/6/2016.
 */
public class DialogImpuestosICE extends DialogFragment implements Validator.ValidationListener {

    private List<TarifasImpuesto> tarifasImpuestoList;

    @NotEmpty(message = "La base imponible es obligatoria.")
    private EditText editTextBaseImponibleICE;

    @NotEmpty(message = "El porcentaje es obligatorio.")
    private EditText editTextPorcentaje;

    private TextView textViewtValor;

    private Spinner spinnerImpuestosICE;

    private TarifasImpuesto tarifasImpuesto;

    private Validator validator;

    private InterfaceDialogImpuestosICE interfaceDialogImpuestosICE;

    private ImpuestoComprobanteElectronico impuestoComprobanteElectronico;

    private Context context;

    private ClaseGlobalUsuario claseGlobalUsuario;

    public DialogImpuestosICE() {
    }

    static DialogImpuestosICE newInstance(ArrayList<TarifasImpuesto> tarifasImpuestoList, InterfaceDialogImpuestosICE interfaceDialogImpuestosICE) {

        DialogImpuestosICE dialogImpuestosICE = new DialogImpuestosICE();
        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putSerializable("tarifasImpuestoList", tarifasImpuestoList);
        args.putSerializable("interfaceDialogImpuestosICE", interfaceDialogImpuestosICE);
        dialogImpuestosICE.setArguments(args);
        return dialogImpuestosICE;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tarifasImpuestoList = (ArrayList<TarifasImpuesto>) getArguments().getSerializable("tarifasImpuestoList");
        interfaceDialogImpuestosICE = (InterfaceDialogImpuestosICE) getArguments().getSerializable("interfaceDialogImpuestosICE");
        context = getActivity().getApplicationContext();
        claseGlobalUsuario = (ClaseGlobalUsuario) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearDialogImpuestoICE();
    }

    public AlertDialog crearDialogImpuestoICE() {

        impuestoComprobanteElectronico = new ImpuestoComprobanteElectronico();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.dialog_impuesto_ice, null);

        builder.setView(v);

        editTextBaseImponibleICE = (EditText) v.findViewById(R.id.edit_text_base_imponible_ice);

        editTextPorcentaje = (EditText) v.findViewById(R.id.edit_text_porcentaje_ice);

        textViewtValor = (TextView) v.findViewById(R.id.text_view_valor_ice);

        spinnerImpuestosICE = (Spinner) v.findViewById(R.id.spinner_tarifas_impuestos_ice);

        validator = new Validator(this);
        validator.setValidationListener(this);

        ArrayAdapterTarifasImpuesto arrayAdapterImpuesto = new ArrayAdapterTarifasImpuesto(getContext(), tarifasImpuestoList);
        spinnerImpuestosICE.setAdapter(arrayAdapterImpuesto);
        spinnerImpuestosICE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tarifasImpuesto = (TarifasImpuesto) parent.getItemAtPosition(position);

                if (!editTextBaseImponibleICE.getText().toString().trim().equals("")) {
                    String valor = Calculos.calcularValorImpuestoICE(editTextBaseImponibleICE.getText().toString(), tarifasImpuesto.getPorcentajeTarifaImpuesto());
                    textViewtValor.setText(valor);
                }
                editTextPorcentaje.setText(tarifasImpuesto.getPorcentajeTarifaImpuesto());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        editTextBaseImponibleICE.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tarifasImpuesto != null) {
                    String valor = Calculos.calcularValorImpuestoICE(editTextBaseImponibleICE.getText().toString(), tarifasImpuesto.getPorcentajeTarifaImpuesto());
                    textViewtValor.setText(valor);
                }
            }
        });
        editTextPorcentaje.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (tarifasImpuesto != null) {
                    String valor = Calculos.calcularValorImpuestoICE(editTextBaseImponibleICE.getText().toString(), editTextPorcentaje.getText().toString());
                    textViewtValor.setText(valor);
                }
            }
        });

        Button aceptar = (Button) v.findViewById(R.id.button_aceptar);

        Button cancelar = (Button) v.findViewById(R.id.button_cancelar);

        aceptar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validator.validate();

                    }
                }
        );

        cancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dismiss();
                    }
                }
        );

        return builder.create();
    }

    @Override
    public void onValidationSucceeded() {

        if (tarifasImpuesto != null) {
            impuestoComprobanteElectronico.setCodigo(claseGlobalUsuario.getImpuestos().get("ICE"));
            impuestoComprobanteElectronico.setCodigoPorcentaje(tarifasImpuesto.getCodigoTarifaImpuesto());
            impuestoComprobanteElectronico.setTarifa(editTextPorcentaje.getText().toString());
            impuestoComprobanteElectronico.setBaseImponible(editTextBaseImponibleICE.getText().toString());
            impuestoComprobanteElectronico.setValor(textViewtValor.getText().toString());
            interfaceDialogImpuestosICE.enviarTarifaImpuesto(impuestoComprobanteElectronico);
        }
        dismiss();


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
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
