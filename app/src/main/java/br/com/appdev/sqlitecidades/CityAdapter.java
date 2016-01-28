package br.com.appdev.sqlitecidades;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CityAdapter extends BaseAdapter {

    private List<City> cities;
    private Context context;

    public CityAdapter(Context context) {
        this.context = context;
        DataStore.sharedInstance().setContext(context);
        cities = DataStore.sharedInstance().getCities();
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int position) {
        return cities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        City city = cities.get(position);

        View view = LayoutInflater.from(context).inflate(R.layout.adapter_city, parent, false);

        TextView txtCityUF = (TextView) view.findViewById(R.id.txtCityUF);
        TextView txtPeople = (TextView) view.findViewById(R.id.txtPeople);

        txtCityUF.setText(city.getCity() + "/" + city.getUf());
        txtPeople.setText("People: " + city.getPeople());

        return view;
    }
}
