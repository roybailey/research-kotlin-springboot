/*
 * This file is generated by jOOQ.
 */
package me.roybailey.codegen.jooq.database.tables;


import me.roybailey.codegen.jooq.database.Keys;
import me.roybailey.codegen.jooq.database.Public;
import me.roybailey.codegen.jooq.database.tables.records.TempCitiesRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TempCities extends TableImpl<TempCitiesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.temp_cities</code>
     */
    public static final TempCities TEMP_CITIES = new TempCities();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TempCitiesRecord> getRecordType() {
        return TempCitiesRecord.class;
    }

    /**
     * The column <code>public.temp_cities.id</code>.
     */
    public final TableField<TempCitiesRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.temp_cities.name</code>.
     */
    public final TableField<TempCitiesRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.temp_cities.population</code>.
     */
    public final TableField<TempCitiesRecord, Integer> POPULATION = createField(DSL.name("population"), SQLDataType.INTEGER, this, "");

    private TempCities(Name alias, Table<TempCitiesRecord> aliased) {
        this(alias, aliased, null);
    }

    private TempCities(Name alias, Table<TempCitiesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.temp_cities</code> table reference
     */
    public TempCities(String alias) {
        this(DSL.name(alias), TEMP_CITIES);
    }

    /**
     * Create an aliased <code>public.temp_cities</code> table reference
     */
    public TempCities(Name alias) {
        this(alias, TEMP_CITIES);
    }

    /**
     * Create a <code>public.temp_cities</code> table reference
     */
    public TempCities() {
        this(DSL.name("temp_cities"), null);
    }

    public <O extends Record> TempCities(Table<O> child, ForeignKey<O, TempCitiesRecord> key) {
        super(child, key, TEMP_CITIES);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<TempCitiesRecord, Integer> getIdentity() {
        return (Identity<TempCitiesRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<TempCitiesRecord> getPrimaryKey() {
        return Keys.TEMP_CITIES_PKEY;
    }

    @Override
    public TempCities as(String alias) {
        return new TempCities(DSL.name(alias), this);
    }

    @Override
    public TempCities as(Name alias) {
        return new TempCities(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TempCities rename(String name) {
        return new TempCities(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TempCities rename(Name name) {
        return new TempCities(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Integer, String, Integer> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}