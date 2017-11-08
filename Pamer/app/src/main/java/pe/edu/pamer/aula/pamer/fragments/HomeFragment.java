package pe.edu.pamer.aula.pamer.fragments;


import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import pe.edu.pamer.aula.pamer.R;
import pe.edu.pamer.aula.pamer.activities.NavDrawerActivity;

import static pe.edu.pamer.aula.pamer.R.id.text;
import static pe.edu.pamer.aula.pamer.R.id.textViewDescriptHome;

/**
 * Created by KEVIN on 31/08/2017.
 */

public class HomeFragment extends Fragment {

    TextView textViewDescHome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        ((NavDrawerActivity) getActivity())
                .setActionBarTitle("Home");

        textViewDescHome = (TextView) view.findViewById(R.id.textViewDescriptHome);

        String descripcion = "Iniciamos ofreciendo una educación pre universitaria exclusiva para postulantes a la Católica, gracias el éxito obtenido en los procesos de admisión y la cantidad de ingresantes, conseguimos posicionarnos como una marca líder, permitiéndonos ampliar nuestras líneas de negocio en la preparación para otras universidades, alcanzando excelentes resultados.\n" +
                "\n" +
                "En el 2001 Pamer amplía su oferta e incursiona en colegios, contando con resultados exitosos tanto desde el punto de vista académico como de rentabilidad, formando personas de éxito, capaces de afrontar los desafíos del día a día.";
        textViewDescHome.setText(descripcion);
        //textViewDescHome.setMovementMethod(new ScrollingMovementMethod());




        return view;
    }



}
