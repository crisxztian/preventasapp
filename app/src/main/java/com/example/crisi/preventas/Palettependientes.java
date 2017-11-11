package com.example.crisi.preventas;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by crisi on 22/10/2017.
 */

public class Palettependientes extends RecyclerView.Adapter<Palettependientes.PaletteViewHolder> {
    private List<String[]> data;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    public Palettependientes(@NonNull List<String[]> data, @NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.data = data;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }
    @Override
    public PaletteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowpendientes, parent, false);
        return new PaletteViewHolder(row);
    }

    @Override
    public void onBindViewHolder(PaletteViewHolder holder, int position) {
        if(data != null) {
            String[] formpendiente = data.get(position);
            holder.getCliente().setText(formpendiente[0]);
            holder.getDireccion().setText(formpendiente[1]);
            holder.getTotal().setText("$"+formpendiente[2]);
        }
    }

    @Override
    public int getItemCount() {
        int count=0;
        if(data != null){
            count = data.size();
        }
        return count;
    }

    class PaletteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView cliente;
        private TextView direccion;
        private TextView total;

        public TextView getCliente() {
            return cliente;
        }

        public TextView getDireccion() {
            return direccion;
        }

        public TextView getTotal() {
            return total;
        }

        public PaletteViewHolder(View itemView) {
            super(itemView);
            cliente = (TextView) itemView.findViewById(R.id.clienteView);
            direccion = (TextView) itemView.findViewById(R.id.direcciondview);
            total = (TextView) itemView.findViewById(R.id.totalpendiente);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            recyclerViewOnItemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
