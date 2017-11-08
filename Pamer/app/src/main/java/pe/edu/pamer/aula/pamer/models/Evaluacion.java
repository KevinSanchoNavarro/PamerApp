package pe.edu.pamer.aula.pamer.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KEVIN on 28/09/2017.
 */

public class Evaluacion {
    private int codigo = -1;
    private String nombre = "";
    private String fecha = "";
    private double nota = -1;
    private int buenas = -1;
    private int malas = -1;
    private int blanco = -1;
    private int swa = -1;

    public Evaluacion() {
    }

    public Evaluacion(int codigo, String nombre, String fecha, float nota, int buenas, int malas, int blanco, int swa) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fecha = fecha;
        this.nota = nota;
        this.buenas = buenas;
        this.malas = malas;
        this.blanco = blanco;
        this.swa = swa;
    }

    public int getCodigo() {
        return codigo;
    }

    public Evaluacion setCodigo(int codigo) {
        this.codigo = codigo;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Evaluacion setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public String getFecha() {
        return fecha;
    }

    public Evaluacion setFecha(String fecha) {
        this.fecha = fecha;
        return this;
    }

    public double getNota() {
        return nota;
    }

    public Evaluacion setNota(double nota) {
        this.nota = nota;
        return this;
    }

    public int getBuenas() {
        return buenas;
    }

    public Evaluacion setBuenas(int buenas) {
        this.buenas = buenas;
        return this;
    }

    public int getMalas() {
        return malas;
    }

    public Evaluacion setMalas(int malas) {
        this.malas = malas;
        return this;
    }

    public int getBlanco() {
        return blanco;
    }

    public Evaluacion setBlanco(int blanco) {
        this.blanco = blanco;
        return this;
    }

    public int getSwa() {
        return swa;
    }

    public Evaluacion setSwa(int swa) {
        this.swa = swa;
        return this;
    }

    public static Evaluacion build(JSONObject jsonEvaluacion) {
        if(jsonEvaluacion == null) return null;
        Evaluacion evaluacion = new Evaluacion();
        try {
            evaluacion.setCodigo(jsonEvaluacion.getInt("cod"))
                    .setNombre(jsonEvaluacion.getString("nom"))
                    .setFecha(jsonEvaluacion.getString("fec"))
                    .setNota(jsonEvaluacion.getDouble("not"))
                    .setBuenas(jsonEvaluacion.getInt("bue"))
                    .setMalas(jsonEvaluacion.getInt("mal"))
                    .setBlanco(jsonEvaluacion.getInt("bla"))
                    .setSwa(jsonEvaluacion.getInt("swa"));
            return evaluacion;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Evaluacion> build(JSONArray jsonEvaluaciones) {
        if(jsonEvaluaciones == null) return null;
        int length = jsonEvaluaciones.length();
        List<Evaluacion> evaluaciones = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                evaluaciones.add(Evaluacion.build(jsonEvaluaciones.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return evaluaciones;
    }


}
