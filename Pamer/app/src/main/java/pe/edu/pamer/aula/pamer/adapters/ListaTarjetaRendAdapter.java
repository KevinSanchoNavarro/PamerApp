package pe.edu.pamer.aula.pamer.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.pamer.aula.pamer.R;
import pe.edu.pamer.aula.pamer.models.TarjetaRendimiento;

/**
 * Created by KEVIN on 3/10/2017.
 */

public class ListaTarjetaRendAdapter extends RecyclerView.Adapter<ListaTarjetaRendAdapter.ViewHolder>{
    private List<TarjetaRendimiento> listaTarjetaRend;

    public ListaTarjetaRendAdapter() {
    }

    public ListaTarjetaRendAdapter(List<TarjetaRendimiento> listaTarjetaRend) {
        this.listaTarjetaRend = listaTarjetaRend;
    }

    @Override
    public ListaTarjetaRendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.content_individual_tarjrendim, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textViewOpc.setText(String.valueOf(listaTarjetaRend.get(position).getOpcion()));
        String curso = listaTarjetaRend.get(position).getCurso();
        if(curso.length() > 15){
            String nombreCursoParte = curso.substring(0,14);
            holder.textViewCurso.setText(nombreCursoParte + " " + "...");
        }else{
            holder.textViewCurso.setText(curso);
        }

        holder.textViewNotaTR.setText(String.valueOf(listaTarjetaRend.get(position).getNota()));
        holder.textViewBueTR.setText(String.valueOf(listaTarjetaRend.get(position).getBuenas()));
        holder.textViewMalTR.setText(String.valueOf(listaTarjetaRend.get(position).getMalas()));
        holder.textViewBlanTR.setText(String.valueOf(listaTarjetaRend.get(position).getBlanco()));

    }

    @Override
    public int getItemCount() {
        return listaTarjetaRend.size();
    }

    //Accesores---------------------------------------------
    public List<TarjetaRendimiento> getListaTarjetaRend() {
        return listaTarjetaRend;
    }

    public ListaTarjetaRendAdapter setListaTarjetaRend(List<TarjetaRendimiento> listaTarjetaRend) {
        this.listaTarjetaRend = listaTarjetaRend;
        return this;
    }
    //------------------------------------------------------

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOpc;
        TextView textViewCurso;
        TextView textViewNotaTR;
        TextView textViewBueTR;
        TextView textViewMalTR;
        TextView textViewBlanTR;
        ConstraintLayout indivTRConstraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewOpc = (TextView) itemView.findViewById(R.id.textViewOpc);
            textViewCurso = (TextView) itemView.findViewById(R.id.textViewCurso);
            textViewNotaTR = (TextView) itemView.findViewById(R.id.textViewNotaTR);
            textViewBueTR = (TextView) itemView.findViewById(R.id.textViewBueTR);
            textViewMalTR = (TextView) itemView.findViewById(R.id.textViewMalTR);
            textViewBlanTR = (TextView) itemView.findViewById(R.id.textViewBlanTR);
            indivTRConstraintLayout = (ConstraintLayout) itemView.findViewById(R.id.individual_tarj_rendim);

        }
    }


}
