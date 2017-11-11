package com.example.crisi.preventas;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by crisi on 17/10/2017.
 */

public class pendientes extends Fragment {

    conexion conex;
    View viewfrag;
    pedidos ped = new pedidos();
    detallepedido det = new detallepedido();
    productos pro = new productos();
    RecyclerView recypendientes;
    List<String[]> lstpedidos = new ArrayList<>();
    List<String[]> formpendientes = new ArrayList<>();
    List<String> ids = new ArrayList<>();
    TextView counter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewfrag = inflater.inflate(R.layout.fragment_pendientes, container, false);

        conex = new conexion();

        counter = (TextView) viewfrag.findViewById(R.id.contadorpendientes);

        recypendientes = (RecyclerView) viewfrag.findViewById(R.id.recyclerView1);
        recypendientes.setAdapter(new Palettependientes(formpendientes, new RecyclerViewOnItemClickListener() {
            @Override
            public void onClick(View v, final int position) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
                dialogo1.setTitle("Escoger acción");
                dialogo1.setMessage("La preventa fue:");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Entregada", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        new Thread(){
                            @Override
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Connection con = conex.conectar();
                                        ped.setid(Integer.parseInt(ids.get(position)));
                                        Log.i("errores", ids.get(position));
                                        try {
                                            ped.updateestado(con, "Entregado");
                                            formpendientes.remove(position);
                                            recypendientes.getAdapter().notifyItemRemoved(position);
                                            ids.remove(position);
                                            counter.setText(ids.size()+" preventas en lista");
                                            Toast.makeText(getActivity(), "Pedido entregado", Toast.LENGTH_SHORT).show();
                                        } catch (SQLException e) {
                                            Toast.makeText(getActivity(), "No se pudo actualizar el pedido: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                             }
                        }.start();
                    }
                });
                dialogo1.setNegativeButton("Cancelada", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                            new Thread(){
                                @Override
                                public void run() {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Connection con = conex.conectar();
                                            List<String[]> datos;
                                            int id = Integer.parseInt(ids.get(position));
                                            Log.i("errores", ids.get(position));
                                            det.setpedidos_id(id);
                                            ped.setid(id);
                                            try {
                                                datos = det.selectcancelar(con);
                                                for (int i = 0; i < datos.size() ; i++) {
                                                    pro.setid(Integer.parseInt(datos.get(i)[1]));
                                                    pro.setstock(Integer.parseInt(datos.get(i)[0]) + Integer.parseInt(datos.get(i)[2]));
                                                    pro.update(con);
                                                }
                                                ped.updateestado(con, "Cancelado");
                                                formpendientes.remove(position);
                                                recypendientes.getAdapter().notifyItemRemoved(position);
                                                ids.remove(position);
                                                counter.setText(ids.size()+" preventas en lista");
                                                Toast.makeText(getActivity(), "Pedido cancelado", Toast.LENGTH_SHORT).show();
                                            } catch (SQLException e) {
                                                Toast.makeText(getActivity(), "No se pudo actualizar el pedido: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }.start();
                    }
                });
                dialogo1.setNeutralButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogo1.show();
            }
        }));
        recypendientes.setLayoutManager(new LinearLayoutManager(getActivity()));

        recypendientes.addItemDecoration(new DividerItemDecoration(getActivity()));

        dopreventa preventa = new dopreventa();
        preventa.execute("");



        return viewfrag;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Preventas por entregar");
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
                counter.setText(lstpedidos.size()+" preventas en lista");
                for (int i = 0; i < lstpedidos.size() ; i++) {
                    ids.add(lstpedidos.get(i)[0]);
                    formpendientes.add(new String[]{lstpedidos.get(i)[1],lstpedidos.get(i)[2],lstpedidos.get(i)[3]});
                    recypendientes.getAdapter().notifyItemInserted(recypendientes.getAdapter().getItemCount());

                }
                lstpedidos.clear();
                Log.i("errores", String.valueOf(lstpedidos.size()));


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

                    lstpedidos = ped.selectpendientes(con);
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
