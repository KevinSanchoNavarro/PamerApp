package pe.edu.pamer.aula.pamer.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pe.edu.pamer.aula.pamer.R;
import pe.edu.pamer.aula.pamer.activities.NavDrawerActivity;
import pe.edu.pamer.aula.pamer.adapters.EvaluacionesAdapter;
import pe.edu.pamer.aula.pamer.models.Evaluacion;
import pe.edu.pamer.aula.pamer.network.HostingPamerServices;

/**
 * Created by KEVIN on 29/08/2017.
 */

public class TabIVBimestreFragment extends Fragment {
    public int periodo = 4;
    RecyclerView evaluacionesRecyclerView;
    EvaluacionesAdapter evaluacionesAdapter;
    RecyclerView.LayoutManager evaluacionesLayoutManager;
    List<Evaluacion> evaluaciones;
    private static String TAG = "Pamer";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_ivbimestre,container,false);

        evaluaciones = new ArrayList<>();
        evaluacionesRecyclerView = (RecyclerView) view.findViewById(R.id.etisRecyclerView4);
        evaluacionesAdapter = new EvaluacionesAdapter(evaluaciones);
        evaluacionesLayoutManager = new LinearLayoutManager(getContext());

        evaluacionesRecyclerView.setAdapter(evaluacionesAdapter);
        evaluacionesRecyclerView.setLayoutManager(evaluacionesLayoutManager);
        updateEvaluaciones();
        return view;
    }

    private void updateEvaluaciones() {

        AndroidNetworking
                .get(HostingPamerServices.UNIVERSAL_URL)
                .setTag(TAG)
                .setPriority(Priority.LOW)
                .addQueryParameter("tag", "Etisreporte")
                .addQueryParameter("varUsuario", NavDrawerActivity.login_email_usuario)
                .addQueryParameter("varcodalumno", NavDrawerActivity.codigo_usuario)
                .addQueryParameter("varperiodo", String.valueOf(periodo))
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response == null) return;
                        try {
                            if(response.getBoolean("success") == false) {
                                Log.d(TAG, response.getString("message"));
                                return;
                            }
                            evaluaciones = Evaluacion.build(response.getJSONArray("data"));
                            Log.d(TAG, "Found Evaluaciones: " + String.valueOf(evaluaciones.size()));
                            evaluacionesAdapter.setEvaluaciones(evaluaciones);
                            evaluacionesAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, anError.getMessage());

                    }
                });



    }



}
