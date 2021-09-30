/*
 * This file is generated by jOOQ.
 */
package me.roybailey.codegen.jooq.database.tables.pojos;


import java.io.Serializable;
import java.time.OffsetDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class VTempCodegenSample implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer        id;
    private String         title;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String         description;
    private String         periodfrom;
    private String         periodupto;
    private Double         price;
    private Double         discount;

    public VTempCodegenSample() {}

    public VTempCodegenSample(VTempCodegenSample value) {
        this.id = value.id;
        this.title = value.title;
        this.createdAt = value.createdAt;
        this.updatedAt = value.updatedAt;
        this.description = value.description;
        this.periodfrom = value.periodfrom;
        this.periodupto = value.periodupto;
        this.price = value.price;
        this.discount = value.discount;
    }

    public VTempCodegenSample(
        Integer        id,
        String         title,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        String         description,
        String         periodfrom,
        String         periodupto,
        Double         price,
        Double         discount
    ) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.description = description;
        this.periodfrom = periodfrom;
        this.periodupto = periodupto;
        this.price = price;
        this.discount = discount;
    }

    /**
     * Getter for <code>public.v_temp_codegen_sample.id</code>.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Setter for <code>public.v_temp_codegen_sample.id</code>.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter for <code>public.v_temp_codegen_sample.title</code>.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Setter for <code>public.v_temp_codegen_sample.title</code>.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for <code>public.v_temp_codegen_sample.created_at</code>.
     */
    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>public.v_temp_codegen_sample.created_at</code>.
     */
    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Getter for <code>public.v_temp_codegen_sample.updated_at</code>.
     */
    public OffsetDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>public.v_temp_codegen_sample.updated_at</code>.
     */
    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Getter for <code>public.v_temp_codegen_sample.description</code>.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Setter for <code>public.v_temp_codegen_sample.description</code>.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for <code>public.v_temp_codegen_sample.periodfrom</code>.
     */
    public String getPeriodfrom() {
        return this.periodfrom;
    }

    /**
     * Setter for <code>public.v_temp_codegen_sample.periodfrom</code>.
     */
    public void setPeriodfrom(String periodfrom) {
        this.periodfrom = periodfrom;
    }

    /**
     * Getter for <code>public.v_temp_codegen_sample.periodupto</code>.
     */
    public String getPeriodupto() {
        return this.periodupto;
    }

    /**
     * Setter for <code>public.v_temp_codegen_sample.periodupto</code>.
     */
    public void setPeriodupto(String periodupto) {
        this.periodupto = periodupto;
    }

    /**
     * Getter for <code>public.v_temp_codegen_sample.price</code>.
     */
    public Double getPrice() {
        return this.price;
    }

    /**
     * Setter for <code>public.v_temp_codegen_sample.price</code>.
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Getter for <code>public.v_temp_codegen_sample.discount</code>.
     */
    public Double getDiscount() {
        return this.discount;
    }

    /**
     * Setter for <code>public.v_temp_codegen_sample.discount</code>.
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("VTempCodegenSample (");

        sb.append(id);
        sb.append(", ").append(title);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(updatedAt);
        sb.append(", ").append(description);
        sb.append(", ").append(periodfrom);
        sb.append(", ").append(periodupto);
        sb.append(", ").append(price);
        sb.append(", ").append(discount);

        sb.append(")");
        return sb.toString();
    }
}
