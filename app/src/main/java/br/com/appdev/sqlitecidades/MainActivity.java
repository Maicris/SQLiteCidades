package br.com.appdev.sqlitecidades;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private ListView lstCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstCities = (ListView) findViewById(R.id.lstCities);
        CityAdapter cityAdapter = new CityAdapter(this);
        lstCities.setAdapter(cityAdapter);
//        DataStore.sharedInstance().addCity(new City("Porto Alegre", 1400000, "RS"));
//        DataStore.sharedInstance().addCity(new City("Curitiba", 1750000, "PR"));
//        DataStore.sharedInstance().addCity(new City("belo Horizonte", 1450000, "MG"));
    }
}
