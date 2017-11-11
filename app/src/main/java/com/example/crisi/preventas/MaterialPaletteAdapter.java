package com.example.crisi.preventas;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class MaterialPaletteAdapter extends RecyclerView.Adapter<MaterialPaletteAdapter.PaletteViewHolder> {
    private List<String[]> data;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    public MaterialPaletteAdapter(@NonNull List<String[]> data,@NonNull RecyclerViewOnItemClickListener recyclerViewOnItemClickListener) {
        this.data = data;
        this.recyclerViewOnItemClickListener = recyclerViewOnItemClickListener;
    }

    @Override
    public PaletteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new PaletteViewHolder(row);
    }

    @Override
    public void onBindViewHolder(PaletteViewHolder holder, int position) {
        if(data != null) {
            String[] formpreventa = data.get(position);
            holder.getDescripcion().setText(formpreventa[0]);
            holder.getCantidad().setText(formpreventa[1]);
            holder.getTotal().setText(formpreventa[2]);
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
        private TextView descripcion;
        private TextView cantidad;
        private TextView total;

        public TextView getDescripcion() {
            return descripcion;
        }

        public TextView getCantidad() {
            return cantidad;
        }

        public TextView getTotal() {
            return total;
        }

        public PaletteViewHolder(View itemView) {
            super(itemView);
            descripcion = (TextView) itemView.findViewById(R.id.descripcionView);
            cantidad = (TextView) itemView.findViewById(R.id.cantidadview);
            total = (TextView) itemView.findViewById(R.id.totalView);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            recyclerViewOnItemClickListener.onClick(v, getAdapterPosition());
        }
    }

}

