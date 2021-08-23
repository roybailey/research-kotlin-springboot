/*
 * This file is generated by jOOQ.
 */
package me.roybailey.codegen.jooq.database.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TempCities implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String  name;
    private Integer population;

    public TempCities() {}

    public TempCities(TempCities value) {
        this.id = value.id;
        this.name = value.name;
        this.population = value.population;
    }

    public TempCities(
        Integer id,
        String  name,
        Integer population
    ) {
        this.id = id;
        this.name = name;
        this.population = population;
    }

    /**
     * Getter for <code>public.temp_cities.id</code>.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Setter for <code>public.temp_cities.id</code>.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter for <code>public.temp_cities.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter for <code>public.temp_cities.name</code>.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for <code>public.temp_cities.population</code>.
     */
    public Integer getPopulation() {
        return this.population;
    }

    /**
     * Setter for <code>public.temp_cities.population</code>.
     */
    public void setPopulation(Integer population) {
        this.population = population;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TempCities (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(population);

        sb.append(")");
        return sb.toString();
    }
}
