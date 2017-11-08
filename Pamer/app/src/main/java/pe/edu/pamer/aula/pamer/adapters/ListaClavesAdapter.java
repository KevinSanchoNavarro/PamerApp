package pe.edu.pamer.aula.pamer.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pe.edu.pamer.aula.pamer.R;
import pe.edu.pamer.aula.pamer.models.Clave;

/**
 * Created by KEVIN on 6/10/2017.
 */

public class ListaClavesAdapter extends RecyclerView.Adapter<ListaClavesAdapter.ViewHolder> {

    private List<Clave> listaClaves;

    public ListaClavesAdapter() {
    }

    public ListaClavesAdapter(List<Clave> listaClaves) {
        this.setListaClaves(listaClaves);
    }

    @Override
    public ListaClavesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.content_indiv_clave, parent, false));
    }

    @Override
    public void onBindViewHolder(ListaClavesAdapter.ViewHolder holder, int position) {

        holder.textViewIndClavPar.setText(String.valueOf(listaClaves.get(position).getPar()));
        holder.textViewIndClavNro.setText(String.valueOf(listaClaves.get(position).getNro()));
        String curso = listaClaves.get(position).getCur();
        if(curso.length() > 15){
            String parteNombCurso = curso.substring(0, 14);
            holder.textViewIndClavCur.setText(parteNombCurso + " ...");
        } else {
            holder.textViewIndClavCur.setText(curso);
        }

        holder.textViewIndClavClM.setText(listaClaves.get(position).getMar());
        holder.textViewIndClavClC.setText(listaClaves.get(position).getCla());
        holder.textViewIndClavPun.setText(String.valueOf(listaClaves.get(position).getNot()));

    }

    @Override
    public int getItemCount() {
        return listaClaves.size();
    }

    //Accesores-----------------------------------------------

    public List<Clave> getListaClaves() {
        return listaClaves;
    }

    public ListaClavesAdapter setListaClaves(List<Clave> listaClaves) {
        this.listaClaves = listaClaves;
        return this;
    }

    //--------------------------------------------------------

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewIndClavPar;
        TextView textViewIndClavNro;
        TextView textViewIndClavCur;
        TextView textViewIndClavClM;
        TextView textViewIndClavClC;
        TextView textViewIndClavPun;
        ConstraintLayout indivClaveConstraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewIndClavPar = (TextView) itemView.findViewById(R.id.textViewIndivClavePar);
            textViewIndClavNro = (TextView) itemView.findViewById(R.id.textViewIndivClaveNro);
            textViewIndClavCur = (TextView) itemView.findViewById(R.id.textViewIndivClaveCur);
            textViewIndClavClM = (TextView) itemView.findViewById(R.id.textViewIndivClaveClM);
            textViewIndClavClC = (TextView) itemView.findViewById(R.id.textViewIndivClaveClC);
            textViewIndClavPun = (TextView) itemView.findViewById(R.id.textViewIndivClavePun);
            indivClaveConstraintLayout = (ConstraintLayout) itemView.findViewById(R.id.indiv_clave);

        }

    }
}
