/*
 * This file is generated by jOOQ.
 */
package me.roybailey.codegen.jooq.database.tables;


import java.time.OffsetDateTime;

import me.roybailey.codegen.jooq.database.Public;
import me.roybailey.codegen.jooq.database.tables.records.VTempCodegenSampleRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row9;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VTempCodegenSample extends TableImpl<VTempCodegenSampleRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.v_temp_codegen_sample</code>
     */
    public static final VTempCodegenSample V_TEMP_CODEGEN_SAMPLE = new VTempCodegenSample();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<VTempCodegenSampleRecord> getRecordType() {
        return VTempCodegenSampleRecord.class;
    }

    /**
     * The column <code>public.v_temp_codegen_sample.id</code>.
     */
    public final TableField<VTempCodegenSampleRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.v_temp_codegen_sample.title</code>.
     */
    public final TableField<VTempCodegenSampleRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.v_temp_codegen_sample.created_at</code>.
     */
    public final TableField<VTempCodegenSampleRecord, OffsetDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    /**
     * The column <code>public.v_temp_codegen_sample.updated_at</code>.
     */
    public final TableField<VTempCodegenSampleRecord, OffsetDateTime> UPDATED_AT = createField(DSL.name("updated_at"), SQLDataType.TIMESTAMPWITHTIMEZONE(6), this, "");

    /**
     * The column <code>public.v_temp_codegen_sample.description</code>.
     */
    public final TableField<VTempCodegenSampleRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>public.v_temp_codegen_sample.periodfrom</code>.
     */
    public final TableField<VTempCodegenSampleRecord, String> PERIODFROM = createField(DSL.name("periodfrom"), SQLDataType.VARCHAR(10), this, "");

    /**
     * The column <code>public.v_temp_codegen_sample.periodupto</code>.
     */
    public final TableField<VTempCodegenSampleRecord, String> PERIODUPTO = createField(DSL.name("periodupto"), SQLDataType.VARCHAR(10), this, "");

    /**
     * The column <code>public.v_temp_codegen_sample.price</code>.
     */
    public final TableField<VTempCodegenSampleRecord, Double> PRICE = createField(DSL.name("price"), SQLDataType.DOUBLE, this, "");

    /**
     * The column <code>public.v_temp_codegen_sample.discount</code>.
     */
    public final TableField<VTempCodegenSampleRecord, Double> DISCOUNT = createField(DSL.name("discount"), SQLDataType.DOUBLE, this, "");

    private VTempCodegenSample(Name alias, Table<VTempCodegenSampleRecord> aliased) {
        this(alias, aliased, null);
    }

    private VTempCodegenSample(Name alias, Table<VTempCodegenSampleRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.view("create view \"v_temp_codegen_sample\" as  SELECT temp_codegen_sample.id,\n    temp_codegen_sample.title,\n    temp_codegen_sample.created_at,\n    temp_codegen_sample.updated_at,\n    temp_codegen_sample.description,\n    temp_codegen_sample.periodfrom,\n    temp_codegen_sample.periodupto,\n    temp_codegen_sample.price,\n    temp_codegen_sample.discount\n   FROM temp_codegen_sample;"));
    }

    /**
     * Create an aliased <code>public.v_temp_codegen_sample</code> table
     * reference
     */
    public VTempCodegenSample(String alias) {
        this(DSL.name(alias), V_TEMP_CODEGEN_SAMPLE);
    }

    /**
     * Create an aliased <code>public.v_temp_codegen_sample</code> table
     * reference
     */
    public VTempCodegenSample(Name alias) {
        this(alias, V_TEMP_CODEGEN_SAMPLE);
    }

    /**
     * Create a <code>public.v_temp_codegen_sample</code> table reference
     */
    public VTempCodegenSample() {
        this(DSL.name("v_temp_codegen_sample"), null);
    }

    public <O extends Record> VTempCodegenSample(Table<O> child, ForeignKey<O, VTempCodegenSampleRecord> key) {
        super(child, key, V_TEMP_CODEGEN_SAMPLE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public VTempCodegenSample as(String alias) {
        return new VTempCodegenSample(DSL.name(alias), this);
    }

    @Override
    public VTempCodegenSample as(Name alias) {
        return new VTempCodegenSample(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public VTempCodegenSample rename(String name) {
        return new VTempCodegenSample(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public VTempCodegenSample rename(Name name) {
        return new VTempCodegenSample(name, null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<Integer, String, OffsetDateTime, OffsetDateTime, String, String, String, Double, Double> fieldsRow() {
        return (Row9) super.fieldsRow();
    }
}