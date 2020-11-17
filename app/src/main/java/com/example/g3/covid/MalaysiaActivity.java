package com.example.g3.covid;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.g3.R;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

public class MalaysiaActivity extends AppCompatActivity {

    TextView tvCases, tvRecovered, tvCritical, tvActive, tvTodayCases, tvTotalDeaths, tvTodayDeaths;
    SimpleArcLoader simpleArcLoader;
    ScrollView scrollView;
    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.malaysia_covid_data);
        getSupportActionBar().setTitle("Malaysia Covid-19 Overview");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tvCases = findViewById(R.id.tvCasesMalaysia);
        tvRecovered = findViewById(R.id.tvRecoveredMalaysia);
        tvCritical = findViewById(R.id.tvCriticalMalaysia);
        tvActive = findViewById(R.id.tvActiveMalaysia);
        tvTodayCases = findViewById(R.id.tvTodayCasesMalaysia);
        tvTotalDeaths = findViewById(R.id.tvTotalDeathsMalaysia);
        tvTodayDeaths = findViewById(R.id.tvTodayDeathsMalaysia);
        simpleArcLoader = findViewById(R.id.loaderMalaysia);
        scrollView = findViewById(R.id.scrollStatsMalaysia);
        pieChart = findViewById(R.id.piechartMalaysia);
        fetchData();
    }

    private void fetchData() {
        String url = "https://disease.sh/v3/covid-19/countries";
        simpleArcLoader.start();
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String countryName = jsonObject.getString("country");
                                if (countryName.equalsIgnoreCase("Malaysia")) {
                                    String cases = jsonObject.getString("cases");
                                    String todayCases = jsonObject.getString("todayCases");
                                    String deaths = jsonObject.getString("deaths");
                                    String todayDeaths = jsonObject.getString("todayDeaths");
                                    String recovered = jsonObject.getString("recovered");
                                    String active = jsonObject.getString("active");
                                    String critical = jsonObject.getString("critical");
                                    //get malaysia's flag icon
//                                    JSONObject object = jsonObject.getJSONObject("countryInfo");
//                                    String flagUrl = object.getString("flag");
                                    tvCases.setText(cases);
                                    tvRecovered.setText(recovered);
                                    tvCritical.setText(critical);
                                    tvActive.setText(active);
                                    tvTodayCases.setText(todayCases);
                                    tvTotalDeaths.setText(deaths);
                                    tvTodayDeaths.setText(todayDeaths);
                                }
                            }
                            pieChart.addPieSlice(new PieModel("Cases",
                                    Integer.parseInt(tvCases.getText().toString()),
                                    Color.parseColor("#FFA726")));
                            pieChart.addPieSlice(new PieModel("Recoverd",
                                    Integer.parseInt(tvRecovered.getText().toString()),
                                    Color.parseColor("#66BB6A")));
                            pieChart.addPieSlice(new PieModel("Deaths",
                                    Integer.parseInt(tvTotalDeaths.getText().toString()),
                                    Color.parseColor("#EF5350")));
                            pieChart.addPieSlice(new PieModel("Active",
                                    Integer.parseInt(tvActive.getText().toString()),
                                    Color.parseColor("#29B6F6")));
                            pieChart.startAnimation();
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            simpleArcLoader.stop();
                            simpleArcLoader.setVisibility(View.GONE);
                            scrollView.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader.stop();
                simpleArcLoader.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                Toast.makeText(MalaysiaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}




