package ec.bigdata.facturaelectronicamovil.adaptador;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.Producto;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestProducto;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DavidLeonardo on 6/5/2016.
 */
public class ArrayAdapterProducto extends ArrayAdapter<Producto> {

    private Context context;
    private int resource;
    private int textViewResourceId;
    private List<Producto> sugerencias;
    private List<Producto> productosTemporales;
    private ClaseGlobalUsuario claseGlobalUsuario;
    private ClienteRestProducto.ServicioProducto servicioProducto;

    public ArrayAdapterProducto(Context context, int resource, int textViewResourceId, List<Producto> productos) {
        super(context, resource, textViewResourceId);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        productosTemporales = new ArrayList<Producto>(productos);
        sugerencias = new ArrayList<Producto>();
        claseGlobalUsuario = (ClaseGlobalUsuario) this.context.getApplicationContext();
        servicioProducto = ClienteRestProducto.getServicioProducto();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.fila_auto_complete, parent, false);
        }
        Producto producto = productosTemporales.get(position);
        if (producto != null) {
            TextView lblName = (TextView) view.findViewById(R.id.text_view_filtrado);
            if (lblName != null) {
                lblName.setText(producto.getCodigoPrincipalProducto() + "-" + producto.getDescripcionProducto());
                lblName.setTextColor(ContextCompat.getColor(context, android.R.color.black));
            }
        }

        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((ec.bigdata.facturaelectronicamovil.modelo.Producto) resultValue).getDescripcionProducto();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                sugerencias.clear();
                for (Producto producto : productosTemporales) {
                    if (producto.getCodigoPrincipalProducto().contains(constraint.toString()) ||
                            producto.getDescripcionProducto().toUpperCase().contains(constraint.toString().toUpperCase())) {
                        sugerencias.add(producto);
                    }
                }
                /*AsyncTaskCargaProductos asyncTaskCargaProductos=new AsyncTaskCargaProductos();
                try {List<ec.bigdata.facturaelectronicamovil.modelo.Producto> productosFiltrados=asyncTaskCargaProductos.execute(constraint.toString().toUpperCase()).get();
                    if(productosFiltrados!=null && !productosFiltrados.isEmpty()){
                        sugerencias.addAll(productosFiltrados);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }*/
                FilterResults filterResults = new FilterResults();

                filterResults.values = sugerencias;
                filterResults.count = sugerencias.size();

                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<ec.bigdata.facturaelectronicamovil.modelo.Producto> filterList = (ArrayList<ec.bigdata.facturaelectronicamovil.modelo.Producto>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ec.bigdata.facturaelectronicamovil.modelo.Producto producto : filterList) {

                    add(producto);
                    notifyDataSetChanged();
                }
            }
        }
    };

    class AsyncTaskCargaProductos extends AsyncTask<String, String, List<ec.bigdata.facturaelectronicamovil.modelo.Producto>> {

        private ProgressDialog progressDialog;
        private List<ec.bigdata.facturaelectronicamovil.modelo.Producto> productosConsultados;

        @Override
        protected List<ec.bigdata.facturaelectronicamovil.modelo.Producto> doInBackground(String... params) {


            Call<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>> callProducto = servicioProducto.obtenerProductosPorCoincidenciaPorEmpresaAsociado(claseGlobalUsuario.getIdEmpresa(), params[0].toUpperCase());
            callProducto.enqueue(new Callback<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>>() {
                @Override
                public void onResponse(Call<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>> call, Response<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>> response) {
                    if (response.isSuccessful()) {
                        productosConsultados = response.body();
                    }
                }

                @Override
                public void onFailure(Call<List<ec.bigdata.facturaelectronicamovil.modelo.Producto>> call, Throwable t) {
                    call.cancel();
                }
            });

            return productosConsultados;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            productosConsultados = new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<ec.bigdata.facturaelectronicamovil.modelo.Producto> result) {
            super.onPostExecute(result);

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();


        }
    }


}