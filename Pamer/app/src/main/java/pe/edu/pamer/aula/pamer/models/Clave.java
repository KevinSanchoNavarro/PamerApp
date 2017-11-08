package pe.edu.pamer.aula.pamer.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KEVIN on 5/10/2017.
 */

public class Clave {
    private int opc = -1;
    private int par = -1;
    private int nro = -1;
    private String cur = "";
    private String mar = "";
    private String cla = "";
    private int not = -1;


    public Clave() {
    }


    public Clave(int opc, int par, int nro, String cur, String mar, String cla, int not) {
        this.setOpc(opc);
        this.setPar(par);
        this.setNro(nro);
        this.setCur(cur);
        this.setMar(mar);
        this.setCla(cla);
        this.setNot(not);
    }


    public int getOpc() {
        return opc;
    }

    public Clave setOpc(int opc) {
        this.opc = opc;
        return this;
    }

    public int getPar() {
        return par;
    }

    public Clave setPar(int par) {
        this.par = par;
        return this;
    }

    public int getNro() {
        return nro;
    }

    public Clave setNro(int nro) {
        this.nro = nro;
        return this;
    }

    public String getCur() {
        return cur;
    }

    public Clave setCur(String cur) {
        this.cur = cur;
        return this;
    }

    public String getMar() {
        return mar;
    }

    public Clave setMar(String mar) {
        this.mar = mar;
        return this;
    }

    public String getCla() {
        return cla;
    }

    public Clave setCla(String cla) {
        this.cla = cla;
        return this;
    }

    public int getNot() {
        return not;
    }

    public Clave setNot(int not) {
        this.not = not;
        return this;
    }

    public static Clave build(JSONObject jsonFilaClave) {
        if(jsonFilaClave == null) return null;
        Clave filaClave = new Clave();
        try {
            filaClave.setOpc(jsonFilaClave.getInt("opc"))
                    .setPar(jsonFilaClave.getInt("par"))
                    .setNro(jsonFilaClave.getInt("nro"))
                    .setCur(jsonFilaClave.getString("cur"))
                    .setMar(jsonFilaClave.getString("mar"))
                    .setCla(jsonFilaClave.getString("cla"))
                    .setNot(jsonFilaClave.getInt("not"));
            return filaClave;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Clave> build(JSONArray jsonListaClaves) {
        if(jsonListaClaves == null) return null;
        int length = jsonListaClaves.length();
        List<Clave> listaClaves = new ArrayList<>();
        for(int i = 0; i < length; i++)
            try {
                listaClaves.add(Clave.build(jsonListaClaves.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return listaClaves;
    }

















}
