package com.drdistributor.dr;

import java.util.ArrayList;

public class Chemist_search_get_or_set {
    private String chemist_altercode,chemist_name,chemist_image,user_cart,user_cart_total;
    public Chemist_search_get_or_set() {
    }

    public Chemist_search_get_or_set(String chemist_altercode, String chemist_name, String chemist_image, String user_cart, String user_cart_total,
                                     ArrayList<String> genre) {
        this.chemist_altercode = chemist_altercode;
        this.chemist_name = chemist_name;
        this.chemist_image = chemist_image;
        this.user_cart = user_cart;
        this.user_cart_total = user_cart_total;
    }

    public String chemist_altercode() {
        return chemist_altercode;
    }

    public void chemist_altercode(String chemist_altercode) {
        this.chemist_altercode = chemist_altercode;
    }

    public String chemist_name() {
        return chemist_name;
    }

    public void chemist_name(String chemist_name) {
        this.chemist_name = chemist_name;
    }

    public String chemist_image() {
        return chemist_image;
    }

    public void chemist_image(String chemist_image) {
        this.chemist_image = chemist_image;
    }

    public String user_cart() {
        return user_cart;
    }

    public void user_cart(String user_cart) {
        this.user_cart = user_cart;
    }

    public String user_cart_total() {
        return user_cart_total;
    }

    public void user_cart_total(String user_cart_total) {
        this.user_cart_total = user_cart_total;
    }
}
