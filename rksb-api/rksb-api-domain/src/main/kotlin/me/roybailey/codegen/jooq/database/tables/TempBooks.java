/*
 * This file is generated by jOOQ.
 */
package me.roybailey.codegen.jooq.database.tables;


import me.roybailey.codegen.jooq.database.Keys;
import me.roybailey.codegen.jooq.database.Public;
import me.roybailey.codegen.jooq.database.tables.records.TempBooksRecord;

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
public class TempBooks extends TableImpl<TempBooksRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.temp_books</code>
     */
    public static final TempBooks TEMP_BOOKS = new TempBooks();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TempBooksRecord> getRecordType() {
        return TempBooksRecord.class;
    }

    /**
     * The column <code>public.temp_books.id</code>.
     */
    public final TableField<TempBooksRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.temp_books.title</code>.
     */
    public final TableField<TempBooksRecord, String> TITLE = createField(DSL.name("title"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.temp_books.publicationdate</code>.
     */
    public final TableField<TempBooksRecord, Integer> PUBLICATIONDATE = createField(DSL.name("publicationdate"), SQLDataType.INTEGER, this, "");

    private TempBooks(Name alias, Table<TempBooksRecord> aliased) {
        this(alias, aliased, null);
    }

    private TempBooks(Name alias, Table<TempBooksRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.temp_books</code> table reference
     */
    public TempBooks(String alias) {
        this(DSL.name(alias), TEMP_BOOKS);
    }

    /**
     * Create an aliased <code>public.temp_books</code> table reference
     */
    public TempBooks(Name alias) {
        this(alias, TEMP_BOOKS);
    }

    /**
     * Create a <code>public.temp_books</code> table reference
     */
    public TempBooks() {
        this(DSL.name("temp_books"), null);
    }

    public <O extends Record> TempBooks(Table<O> child, ForeignKey<O, TempBooksRecord> key) {
        super(child, key, TEMP_BOOKS);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<TempBooksRecord, Integer> getIdentity() {
        return (Identity<TempBooksRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<TempBooksRecord> getPrimaryKey() {
        return Keys.TEMP_BOOKS_PKEY;
    }

    @Override
    public TempBooks as(String alias) {
        return new TempBooks(DSL.name(alias), this);
    }

    @Override
    public TempBooks as(Name alias) {
        return new TempBooks(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public TempBooks rename(String name) {
        return new TempBooks(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public TempBooks rename(Name name) {
        return new TempBooks(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Integer, String, Integer> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}