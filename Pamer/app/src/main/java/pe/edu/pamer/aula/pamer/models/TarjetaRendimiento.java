package pe.edu.pamer.aula.pamer.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KEVIN on 3/10/2017.
 */

public class TarjetaRendimiento {

    private int opcion = -1;
    private String curso = "";
    private double nota = -1;
    private int buenas = -1;
    private int malas = -1;
    private int blanco = -1;

    public TarjetaRendimiento() {

    }

    public TarjetaRendimiento(int opcion, String curso, double nota, int buenas, int malas, int blanco) {
        this.setOpcion(opcion);
        this.setCurso(curso);
        this.setNota(nota);
        this.setBuenas(buenas);
        this.setMalas(malas);
        this.setBlanco(blanco);
    }


    public int getOpcion() {
        return opcion;
    }

    public TarjetaRendimiento setOpcion(int opcion) {
        this.opcion = opcion;
        return this;
    }

    public String getCurso() {
        return curso;
    }

    public TarjetaRendimiento setCurso(String curso) {
        this.curso = curso;
        return this;
    }

    public double getNota() {
        return nota;
    }

    public TarjetaRendimiento setNota(double nota) {
        this.nota = nota;
        return this;
    }

    public int getBuenas() {
        return buenas;
    }

    public TarjetaRendimiento setBuenas(int buenas) {
        this.buenas = buenas;
        return this;
    }

    public int getMalas() {
        return malas;
    }

    public TarjetaRendimiento setMalas(int malas) {
        this.malas = malas;
        return this;
    }

    public int getBlanco() {
        return blanco;
    }

    public TarjetaRendimiento setBlanco(int blanco) {
        this.blanco = blanco;
        return this;
    }

    public static TarjetaRendimiento build(JSONObject jsonFilaTarjetRend) {
        if(jsonFilaTarjetRend == null) return null;
        TarjetaRendimiento filaTarjetaRend = new TarjetaRendimiento();
        try {
            filaTarjetaRend.setOpcion(jsonFilaTarjetRend.getInt("opc"))
                    .setCurso(jsonFilaTarjetRend.getString("cur"))
                    .setNota(jsonFilaTarjetRend.getDouble("not"))
                    .setBuenas(Integer.parseInt(jsonFilaTarjetRend.getString("bue")))
                    .setMalas(Integer.parseInt(jsonFilaTarjetRend.getString("mal")))
                    .setBlanco(jsonFilaTarjetRend.getInt("bla"));
            return filaTarjetaRend;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<TarjetaRendimiento> build(JSONArray jsonListaTarjetRend) {
        if(jsonListaTarjetRend == null) return null;
        int length = jsonListaTarjetRend.length();
        List<TarjetaRendimiento> listaTarjetaRend = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                listaTarjetaRend.add(TarjetaRendimiento.build(jsonListaTarjetRend.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return listaTarjetaRend;
    }
}
