package com.drdistributor.dr;

import java.util.ArrayList;

public class Medicine_category_get_or_set {
    private String item_code,item_image,item_name,item_packing,item_expiry,item_company,item_quantity,item_stock,
            item_ptr,item_mrp,item_price,item_scheme,item_margin,item_featured;
    public Medicine_category_get_or_set() {
    }

    public Medicine_category_get_or_set(String item_code, String item_image, String item_name, String item_packing, String item_expiry, String item_company, String item_quantity, String item_stock, String item_ptr, String item_mrp, String item_price, String item_scheme, String item_margin, String item_featured,
                                        ArrayList<String> genre) {
        this.item_code = item_code;
        this.item_image = item_image;
        this.item_name = item_name;
        this.item_packing = item_packing;
        this.item_expiry = item_expiry;
        this.item_company = item_company;
        this.item_quantity = item_quantity;
        this.item_stock = item_stock;
        this.item_ptr = item_ptr;
        this.item_mrp = item_mrp;
        this.item_price = item_price;
        this.item_scheme = item_scheme;
        this.item_margin = item_margin;
        this.item_featured = item_featured;
    }

    public String item_code() {
        return item_code;
    }
    public void item_code(String item_code) {
        this.item_code = item_code;
    }

    public String item_image() {
        return item_image;
    }
    public void item_image(String item_image) {
        this.item_image = item_image;
    }

    public String item_name() {
        return item_name;
    }
    public void item_name(String item_name) {
        this.item_name = item_name;
    }

    public String item_packing() {
        return item_packing;
    }
    public void item_packing(String item_packing) {
        this.item_packing = item_packing;
    }

    public String item_expiry() {
        return item_expiry;
    }
    public void item_expiry(String item_expiry) {
        this.item_expiry = item_expiry;
    }

    public String item_company() {
        return item_company;
    }
    public void item_company(String item_company) {
        this.item_company = item_company;
    }

    public String item_quantity() {
        return item_quantity;
    }
    public void item_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String item_stock() {
        return item_stock;
    }
    public void item_stock(String item_stock) {
        this.item_stock = item_stock;
    }

    public String item_ptr() {
        return item_ptr;
    }
    public void item_ptr(String item_ptr) {
        this.item_ptr = item_ptr;
    }

    public String item_mrp() {
        return item_mrp;
    }
    public void item_mrp(String item_mrp) {
        this.item_mrp = item_mrp;
    }

    public String item_price() {
        return item_price;
    }
    public void item_price(String item_price) {
        this.item_price = item_price;
    }

    public String item_scheme() {
        return item_scheme;
    }
    public void item_scheme(String item_scheme) {
        this.item_scheme = item_scheme;
    }

    public String item_margin() {
        return item_margin;
    }
    public void item_margin(String item_margin) {
        this.item_margin = item_margin;
    }

    public String item_featured() {
        return item_featured;
    }
    public void item_featured(String item_featured) {
        this.item_featured = item_featured;
    }
}