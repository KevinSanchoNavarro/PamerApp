package pe.edu.pamer.aula.pamer;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;

import pe.edu.pamer.aula.pamer.models.Evaluacion;
import pe.edu.pamer.aula.pamer.network.HostingPamerServices;

/**
 * Created by KEVIN on 24/09/2017.
 */

public class PamerApp extends Application {
    private static PamerApp instance;
    private HostingPamerServices hostingPamerServices;

    public PamerApp() {
        super();
        instance = this;
    }

    public static PamerApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        hostingPamerServices = new HostingPamerServices();
    }

    public PamerApp setCurrrentEvaluacion(Evaluacion evaluacion){
        hostingPamerServices.setCurrentEvaluacion(evaluacion);
        return this;
    }

    public Evaluacion getCurrentEvaluacion(){
        return hostingPamerServices.getCurrentEvaluacion();
    }




}
