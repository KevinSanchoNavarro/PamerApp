package pe.edu.pamer.aula.pamer.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pe.edu.pamer.aula.pamer.R;

/**
 * Created by KEVIN on 23/08/2017.
 */

public class ContactanosFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactanos,container,false);
        return view;
    }
}
