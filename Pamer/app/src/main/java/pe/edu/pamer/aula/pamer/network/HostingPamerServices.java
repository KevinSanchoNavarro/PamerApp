package pe.edu.pamer.aula.pamer.network;

import pe.edu.pamer.aula.pamer.models.Evaluacion;

/**
 * Created by KEVIN on 26/09/2017.
 */

public class HostingPamerServices {
    public static String UNIVERSAL_URL = "http://aula.pamer.edu.pe/nvpmovil/index.php";
    private Evaluacion currentEvaluacion;


    public Evaluacion getCurrentEvaluacion() {
        return currentEvaluacion;
    }

    public HostingPamerServices setCurrentEvaluacion(Evaluacion currentEvaluacion) {
        this.currentEvaluacion = currentEvaluacion;
        return this;
    }

}
