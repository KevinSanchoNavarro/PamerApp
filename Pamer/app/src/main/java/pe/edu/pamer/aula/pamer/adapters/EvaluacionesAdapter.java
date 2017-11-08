package pe.edu.pamer.aula.pamer.adapters;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.pamer.aula.pamer.PamerApp;
import pe.edu.pamer.aula.pamer.R;
import pe.edu.pamer.aula.pamer.activities.DetalleEtisActivity;
import pe.edu.pamer.aula.pamer.models.Evaluacion;

/**
 * Created by KEVIN on 29/09/2017.
 */

public class EvaluacionesAdapter extends RecyclerView.Adapter<EvaluacionesAdapter.ViewHolder> {
    private List<Evaluacion> evaluaciones;

    public EvaluacionesAdapter() {
    }

    public EvaluacionesAdapter(List<Evaluacion> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }


    @Override
    public EvaluacionesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.content_individual_eti, parent, false));
    }

    @Override
    public void onBindViewHolder(EvaluacionesAdapter.ViewHolder holder, final int position) {
        holder.textViewNota.setText(String.valueOf(evaluaciones.get(position).getNota()));
        holder.textViewFecha.setText(evaluaciones.get(position).getFecha());
        holder.textViewNombre.setText(evaluaciones.get(position).getNombre());
        holder.textViewBue.setText(String.valueOf(evaluaciones.get(position).getBuenas()));
        holder.textViewMal.setText(String.valueOf(evaluaciones.get(position).getMalas()));
        holder.textViewBlan.setText(String.valueOf(evaluaciones.get(position).getBlanco()));
        holder.indivEtiConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PamerApp.getInstance().setCurrrentEvaluacion(evaluaciones.get(position));
                v.getContext().startActivity(new Intent(v.getContext(), DetalleEtisActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return evaluaciones.size();
    }

    //Accesores-----------------------------
    public List<Evaluacion> getEvaluaciones() {
        return evaluaciones;
    }

    public EvaluacionesAdapter setEvaluaciones(List<Evaluacion> evaluaciones) {
        this.evaluaciones = evaluaciones;
        return this;
    }
    //--------------------------------------

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNota;
        TextView textViewFecha;
        TextView textViewNombre;
        TextView textViewBue;
        TextView textViewMal;
        TextView textViewBlan;
        ConstraintLayout indivEtiConstraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNota = (TextView) itemView.findViewById(R.id.textViewNota);
            textViewFecha = (TextView) itemView.findViewById(R.id.textViewFecha);
            textViewNombre = (TextView) itemView.findViewById(R.id.textViewNombre);
            textViewBue = (TextView) itemView.findViewById(R.id.textViewBue);
            textViewMal = (TextView) itemView.findViewById(R.id.textViewMal);
            textViewBlan = (TextView) itemView.findViewById(R.id.textViewBlan);
            indivEtiConstraintLayout = (ConstraintLayout) itemView.findViewById(R.id.individual_eti);

        }


    }






}
