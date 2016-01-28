package br.com.appdev.sqlitecidades;

public class City {

    private long id;
    private String city;
    private int people;
    private String uf;

    public City(String city, int people, String uf) {
        this.city = city;
        this.people = people;
        this.uf = uf;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
