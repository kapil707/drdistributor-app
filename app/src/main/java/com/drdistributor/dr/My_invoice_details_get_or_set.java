package com.drdistributor.dr;

import java.util.ArrayList;

public class My_invoice_details_get_or_set {
    private String item_code,item_quantity,item_image,item_name,
            item_packing,item_expiry,item_company,item_scheme,
            item_price,item_quantity_price,item_date_time,item_modalnumber,
            item_description1,item_delete_title;
    public My_invoice_details_get_or_set() {
    }

    public My_invoice_details_get_or_set(String item_code, String item_quantity, String item_image, String item_name, String item_packing, String item_expiry, String item_company, String item_scheme, String item_price, String item_quantity_price, String item_date_time, String item_modalnumber,String item_description1,String item_delete_title,
                                         ArrayList<String> genre) {
        this.item_code = item_code;
        this.item_image = item_image;
        this.item_name = item_name;
        this.item_packing = item_packing;
        this.item_expiry = item_expiry;
        this.item_company = item_company;
        this.item_scheme = item_scheme;
        this.item_price = item_price;
        this.item_quantity = item_quantity;
        this.item_quantity_price = item_quantity_price;
        this.item_date_time = item_date_time;
        this.item_modalnumber = item_modalnumber;
        this.item_description1 = item_description1;
        this.item_delete_title = item_delete_title;
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

    public String item_scheme() {
        return item_scheme;
    }
    public void item_scheme(String item_scheme) {
        this.item_scheme = item_scheme;
    }

    public String item_price() {
        return item_price;
    }
    public void item_price(String item_price) {
        this.item_price = item_price;
    }

    public String item_quantity() {
        return item_quantity;
    }
    public void item_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String item_quantity_price() {
        return item_quantity_price;
    }
    public void item_quantity_price(String item_quantity_price) {
        this.item_quantity_price = item_quantity_price;
    }

    public String item_date_time() {
        return item_date_time;
    }
    public void item_date_time(String item_date_time) {
        this.item_date_time = item_date_time;
    }

    public String item_modalnumber() {
        return item_modalnumber;
    }
    public void item_modalnumber(String item_modalnumber) {
        this.item_modalnumber = item_modalnumber;
    }

    public String item_description1() {
        return item_description1;
    }
    public void item_description1(String item_description1) {
        this.item_description1 = item_description1;
    }

    public String item_delete_title() {
        return item_delete_title;
    }
    public void item_delete_title(String item_delete_title) {
        this.item_delete_title = item_delete_title;
    }
}