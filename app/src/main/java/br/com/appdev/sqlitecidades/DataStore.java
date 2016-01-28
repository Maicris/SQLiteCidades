package br.com.appdev.sqlitecidades;

import android.content.Context;

import java.util.List;

public class DataStore {

    private static DataStore instance = null;
    private CityDatabase database;
    private List<City> cities;
    private Context context;

    protected DataStore() {
    }

    public static DataStore sharedInstance() {
        if (instance == null)
            instance = new DataStore();
        return instance;
    }

    public void setContext(Context context) {
        this.context = context;
        database = new CityDatabase(context);
        cities = database.getAll();
    }

    public List<City> getCities() {
        return cities;
    }

    public City getCity(int position) {
        return cities.get(position);
    }

    public void addCity(City city) {
        cities.add(city);
        database.insertCity(city);
    }

    public void editCity(City city, int position) {
        cities.set(position, city);
        database.updateCity(city);
    }

    public void removeCity(int position) {
        City city = cities.get(position);
        database.removeCity(city);
        cities.remove(position);
    }
}
