package pe.edu.pamer.aula.pamer.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import pe.edu.pamer.aula.pamer.PamerApp;
import pe.edu.pamer.aula.pamer.R;
import pe.edu.pamer.aula.pamer.adapters.ListaClavesAdapter;
import pe.edu.pamer.aula.pamer.adapters.ListaTarjetaRendAdapter;
import pe.edu.pamer.aula.pamer.models.Clave;
import pe.edu.pamer.aula.pamer.models.Evaluacion;
import pe.edu.pamer.aula.pamer.models.TarjetaRendimiento;
import pe.edu.pamer.aula.pamer.network.HostingPamerServices;

public class DetalleEtisActivity extends AppCompatActivity {

    TextView textViewTitDetEtis;
    Button buttonTarjRend, buttonClaves;
    Evaluacion evaluacion;
    private static String TAG = "Pamer";
    private float[] yData = {1f, 2f, 3f};
    private String[] xData = {"Buenas", "Malas", "Blanco"};
    PieChart pieChart;

    //Elementos del popupTarjeta
    RecyclerView listaTRRecyclerView;
    ListaTarjetaRendAdapter listaTRAdapter;
    RecyclerView.LayoutManager listaTRLayoutManager;
    List<TarjetaRendimiento> listaTR;
    Button btnCancelar1;

    //Elementos del popupClaves
    RecyclerView listaClavesRecyclerView;
    ListaClavesAdapter listaClavesAdapter;
    RecyclerView.LayoutManager listaClavesLayoutManager;
    List<Clave> listaClaves;
    Button btnCancelar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_etis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detalle_etis);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.argb(255, 41, 169, 223));
        }*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("ETIS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);



        textViewTitDetEtis = (TextView) findViewById(R.id.textViewTitDetEti);
        buttonTarjRend = (Button) findViewById(R.id.imageButtonTarjRend);
        buttonClaves = (Button) findViewById(R.id.imageButtonClaves);

        evaluacion = PamerApp.getInstance().getCurrentEvaluacion();
        textViewTitDetEtis.setText(evaluacion.getNombre());

        double porcBue = evaluacion.getBuenas()/(evaluacion.getBuenas()+evaluacion.getMalas()+evaluacion.getBlanco());
        porcBue = Math.round(porcBue * 100.0) / 100.0;
        double porcMal = evaluacion.getMalas()/(evaluacion.getBuenas()+evaluacion.getMalas()+evaluacion.getBlanco());
        porcMal = Math.round(porcMal * 100.0) / 100.0;
        double porcBlan = evaluacion.getBlanco()/(evaluacion.getBuenas()+evaluacion.getMalas()+evaluacion.getBlanco());
        porcBlan = Math.round(porcBlan * 100.0) / 100.0;

        /*yData[0] = String.valueOf(porcBue)+"%"+
                    "\n Buenas: "+evaluacion.getBuenas();
        yData[1] = String.valueOf(porMal)+"%"+
                    "\n Malas: "+evaluacion.getMalas();
        yData[2] = String.valueOf(porcBlan)+"%"+
                    "\n Blanco: "+evaluacion.getBlanco();*/

        yData[0] = (float) evaluacion.getBuenas();
        yData[1] = (float) evaluacion.getMalas();
        yData[2] = (float) evaluacion.getBlanco();

        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setHoleRadius(35f);
        pieChart.setHoleColor(Color.argb(255,11,70,121));
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setRotationEnabled(false);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setDrawEntryLabels(false);

        addDataSet(evaluacion, porcBue, porcMal, porcBlan);
        eventosClick();




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        //return super.onSupportNavigateUp();
        onBackPressed();
        return true;
    }

    private void eventosClick() {
        buttonTarjRend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleEtisActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.popup_tarj_rend,null);
                builder.setView(dialogView);


                //Inicializando elementos
                listaTR = new ArrayList<>();
                listaTRRecyclerView = (RecyclerView) dialogView.findViewById(R.id.tarjet_rend_recyclerview);
                listaTRAdapter = new ListaTarjetaRendAdapter(listaTR);
                listaTRLayoutManager = new LinearLayoutManager(DetalleEtisActivity.this);

                listaTRRecyclerView.setAdapter(listaTRAdapter);
                listaTRRecyclerView.setLayoutManager(listaTRLayoutManager);
                btnCancelar1 = (Button) dialogView.findViewById(R.id.btn_cancel_TarjR);

                updateListaTR();

                final AlertDialog dialog = builder.create();

                btnCancelar1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.show();

                /*Intent i = new Intent(DetalleEtisActivity.this, TarjetaRendimActivity.class);
                startActivity(i);*/

            }
        });

        buttonClaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleEtisActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.popup_claves,null);
                builder.setView(dialogView);


                //Inicializando elementos
                listaClaves = new ArrayList<>();
                listaClavesRecyclerView = (RecyclerView) dialogView.findViewById(R.id.claves_recyclerview);
                listaClavesAdapter = new ListaClavesAdapter(listaClaves);
                listaClavesLayoutManager = new LinearLayoutManager(DetalleEtisActivity.this);

                listaClavesRecyclerView.setAdapter(listaClavesAdapter);
                listaClavesRecyclerView.setLayoutManager(listaClavesLayoutManager);
                btnCancelar2 = (Button) dialogView.findViewById(R.id.btn_cancel_claves);

                updateListaClaves();

                final AlertDialog dialog = builder.create();

                btnCancelar2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.show();

            }
        });

    }

    private void updateListaClaves() {

        AndroidNetworking
                .get(HostingPamerServices.UNIVERSAL_URL)
                .setTag(TAG)
                .setPriority(Priority.LOW)
                .addQueryParameter("tag", "clavesEtis")
                .addQueryParameter("varUsuario", NavDrawerActivity.login_email_usuario)
                .addQueryParameter("intCodAlumno", NavDrawerActivity.codigo_usuario)
                .addQueryParameter("intExamen", String.valueOf(PamerApp.getInstance().getCurrentEvaluacion().getCodigo()))
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
                            listaClaves = Clave.build(response.getJSONArray("data"));
                            Log.d(TAG, "Found FilasClaves: " + String.valueOf(listaClaves.size()));
                            listaClavesAdapter.setListaClaves(listaClaves);
                            listaClavesAdapter.notifyDataSetChanged();
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


    private void updateListaTR(){
        AndroidNetworking
                .get(HostingPamerServices.UNIVERSAL_URL)
                .setTag(TAG)
                .setPriority(Priority.LOW)
                .addQueryParameter("tag", "tarjetasEtis")
                .addQueryParameter("varUsuario", NavDrawerActivity.login_email_usuario)
                .addQueryParameter("intCodAlumno", NavDrawerActivity.codigo_usuario)
                .addQueryParameter("intExamen", String.valueOf(PamerApp.getInstance().getCurrentEvaluacion().getCodigo()))
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
                            listaTR = TarjetaRendimiento.build(response.getJSONArray("data"));
                            Log.d(TAG, "Found FilasTarjetaRendimiento: " + String.valueOf(listaTR.size()));
                            listaTRAdapter.setListaTarjetaRend(listaTR);
                            listaTRAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "===========ERROR EN WEB SERVICE: ========");
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, anError.getMessage());
                        Log.d(TAG, "===========ERROR EN WEB SERVICE: ========");

                    }
                });

    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            super.onBackPressed();
        return super.onOptionsItemSelected(item);
    }*/


    private void addDataSet(Evaluacion evaluacion, double porcBue, double porcMal, double porcBlan) {
        Log.d(TAG, "addDataSet Started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        yEntrys.add(new PieEntry(yData[0], "Buenas: "+evaluacion.getBuenas()));
        yEntrys.add(new PieEntry(yData[1], "Malas: "+evaluacion.getMalas()));
        yEntrys.add(new PieEntry(yData[2], "Blanco: "+evaluacion.getBlanco()));


        for(int i = 0; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //Create the DataSet
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Resultados");
        pieDataSet.setValueTextSize(15);
        pieDataSet.setValueTextColor(Color.WHITE);

        //Add color to DataSet
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(146, 208, 80));
        colors.add(Color.rgb(192, 0, 0));
        colors.add(Color.rgb(165, 165, 165));

        pieDataSet.setColors(colors);
        pieDataSet.setValueFormatter(new MyValueFormatter());

        //Add legend to chart
        Legend legend = pieChart.getLegend();
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(12);


        //Create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();

    }

    public class MyValueFormatter implements IValueFormatter {

        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            // write your logic here
            return mFormat.format(value) + " %"; // e.g. append a percent-sign
        }
    }

}
