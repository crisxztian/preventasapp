package com.example.crisi.preventas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Created by crisi on 17/10/2017.
 */

public class prevender extends Fragment {

    clientes cli = new clientes();
    productos pro = new productos();
    pedidos ped = new pedidos();
    detallepedido det = new detallepedido();
    conexion conex;
    Spinner spin,spinpro;
    TextView txttotal;
    View dialoglayout, viewfrag;
    EditText canti;
    AlertDialog.Builder builder;
    RecyclerView recyclerView;
    List<String[]> formpreventas = new ArrayList<>();
    List<String[]> listclientes = new ArrayList<>();
    List<String[]> listproductos = new ArrayList<>();
    List<String> precios = new ArrayList<>();
    List<String> ids = new ArrayList<>();
    List<String> stocks = new ArrayList<>();
    List<String> descripciones = new ArrayList<>();
    List<String> clidis = new ArrayList<>();
    List<String> clidoc = new ArrayList<>();
    Calendar cal = new GregorianCalendar();

    String fe = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH)+1)+ "/" + cal.get(Calendar.DATE);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dialoglayout = inflater.inflate(R.layout.dialogventa, null);
        conex = new conexion();
        viewfrag = inflater.inflate(R.layout.fragment_preventa, container, false);
        spin = (Spinner) viewfrag.findViewById(R.id.spinner);
        TextView txtviewtotal = (TextView)  viewfrag.findViewById(R.id.lbltotal);
        txttotal = (TextView) viewfrag.findViewById(R.id.totalventa);

        setHasOptionsMenu(true);
        dopreventa preventa = new dopreventa();
        preventa.execute("");

        spinpro = (Spinner) dialoglayout.findViewById(R.id.productos);
        canti = (EditText) dialoglayout.findViewById(R.id.cantidad);

        builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String produ = spinpro.getSelectedItem().toString();
                int can = Integer.parseInt(canti.getText().toString());
                int pre=0;
                for (int i = 0; i < descripciones.size() ; i++) {
                    if(descripciones.get(i).equals(produ)){
                        if(can < Integer.parseInt(stocks.get(i))){
                            pre=Integer.parseInt(precios.get(i));
                            formpreventas.add(new String[]{produ, String.valueOf(can), String.valueOf(can*pre) });
                            txttotal.setText(String.valueOf(Integer.parseInt(txttotal.getText().toString()) + can*pre));
                            recyclerView.getAdapter().notifyItemInserted(recyclerView.getAdapter().getItemCount());
                            break;
                        }else{
                            Toast.makeText(getActivity(), "La cantidad requerida supera al stock del producto", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        recyclerView = (RecyclerView) viewfrag.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(new MaterialPaletteAdapter(formpreventas, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, final int position) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿Desea borrar este item ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        formpreventas.remove(position);
                        recyclerView.getAdapter().notifyItemRemoved(position);
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();
            }
        }));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        FloatingActionButton fab = (FloatingActionButton) viewfrag.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dialoglayout.getParent() != null){
                    ((ViewGroup)dialoglayout.getParent()).removeView(dialoglayout);
                }
                builder.setView(dialoglayout);
                builder.show();
            }
        });

        return viewfrag;

    }

    public void registrar_pedido(){
        if (formpreventas.size() != 0){
            for (int i = 0; i < clidoc.size(); i++) {
                if (spin.getSelectedItem().toString().equals(clidoc.get(i))) {
                    ped.setclientes_id(Integer.parseInt(clidis.get(i)));
                    break;
                }
            }
            ped.setusuarios_id(getActivity().getIntent().getIntExtra("idusuario", -1));
            int total = 0;
            for (int i = 0; i < formpreventas.size(); i++) {
                total = total + Integer.parseInt(formpreventas.get(i)[2]);
            }
            ped.settotal(total);
            ped.setfecha(fe);
            new Thread() {
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Connection con = conex.conectar();
                            try {
                                ped.insert(con);
                                det.setpedidos_id(ped.selectid(con));
                                for (int i = 0; i < formpreventas.size(); i++) {
                                    for (int j = 0; j < descripciones.size(); j++) {
                                        if (formpreventas.get(i)[0].equals(descripciones.get(j))) {
                                            det.setproductos_id(Integer.parseInt(ids.get(j)));
                                            int can = Integer.parseInt(formpreventas.get(i)[1]);
                                            det.setcantidad(can);
                                            stocks.set(j, String.valueOf(Integer.parseInt(stocks.get(j))-can));
                                            pro.setstock(Integer.parseInt(stocks.get(j)));
                                            pro.setid(Integer.parseInt(ids.get(j)));
                                            pro.update(con);
                                            break;
                                        }
                                    }
                                    det.insert(con);
                                }
                                Toast.makeText(getActivity(), "Preventa realizada satisfactoriamente", Toast.LENGTH_SHORT).show();
                                int borrar = formpreventas.size();
                                for (int i = 0; i < borrar; i++) {
                                    formpreventas.remove(0);
                                    recyclerView.getAdapter().notifyItemRemoved(0);
                                }
                                txttotal.setText("0");
                            } catch (SQLException e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.i("errores", e.getMessage());
                            }
                        }
                    });
                }
            }.start();
        }else{
            Toast.makeText(getActivity(), "No ha ingresado productos a la lista", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Realizar preventa");

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_fin) {
                registrar_pedido();
        }

        return super.onOptionsItemSelected(item);
    }

    public class dopreventa extends AsyncTask<String,String,String> {
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            if (!isSuccess) {
                Toast.makeText(getActivity(), r, Toast.LENGTH_LONG).show();
            } else{

                for (int i = 0; i < listproductos.size() ; i++) {
                    ids.add(listproductos.get(i)[0]);
                    descripciones.add(listproductos.get(i)[1]);
                    precios.add(listproductos.get(i)[2]);
                    stocks.add(listproductos.get(i)[3]);
                }
                for (int i = 0; i < listclientes.size() ; i++) {
                    clidis.add(listclientes.get(i)[0]);
                    clidoc.add(listclientes.get(i)[1]);
                }

                        ArrayAdapter<String> adaptercli = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, clidoc);
                        adaptercli.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin.setAdapter(adaptercli);


                        ArrayAdapter<String> adapterpro = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, descripciones );
                        adapterpro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinpro.setAdapter(adapterpro);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String z="";
            try {
                Connection con = conex.conectar();
                if (con == null) {
                    z = "Error conectando a la base de datos";
                }else {

                            listclientes = cli.selectdoc(con, getActivity());
                            listproductos = pro.selecttodo(con, getActivity());
                            isSuccess = true;

                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage();
            }

            return z;
        }
    }
}
