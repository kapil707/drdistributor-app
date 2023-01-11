package com.drdistributor.dr;

import java.util.ArrayList;

public class My_notification_details_get_or_set {
    private String item_id,item_title,item_message,item_date_time,item_image,item_image2,
            item_fun_type,item_fun_name,item_fun_id,item_fun_id2;
    public My_notification_details_get_or_set() {
    }

    public My_notification_details_get_or_set(String item_id, String item_title, String item_message, String item_date_time, String item_image,String item_image2,String item_fun_type,String item_fun_name,String item_fun_id,String item_fun_id2,
                                              ArrayList<String> genre) {
        this.item_id = item_id;
        this.item_title = item_title;
        this.item_message = item_message;
        this.item_date_time = item_date_time;
        this.item_image = item_image;
        this.item_image2 = item_image2;
        this.item_fun_type = item_fun_type;
        this.item_fun_name = item_fun_name;
        this.item_fun_id = item_fun_id;
        this.item_fun_id2 = item_fun_id2;
    }

    public String item_id() {
        return item_id;
    }
    public void item_id(String item_id) {
        this.item_id = item_id;
    }

    public String item_title() {
        return item_title;
    }
    public void item_title(String item_title) {
        this.item_title = item_title;
    }

    public String item_message() {
        return item_message;
    }
    public void item_message(String item_message) {
        this.item_message = item_message;
    }

    public String item_date_time() {
        return item_date_time;
    }
    public void item_date_time(String item_date_time) {
        this.item_date_time = item_date_time;
    }

    public String item_image() {
        return item_image;
    }
    public void item_image(String item_image) {
        this.item_image = item_image;
    }

    public String item_image2() {
        return item_image2;
    }
    public void item_image2(String item_image2) {
        this.item_image2 = item_image2;
    }

    public String item_fun_type() {
        return item_fun_type;
    }
    public void item_fun_type(String item_fun_type) {
        this.item_fun_type = item_fun_type;
    }

    public String item_fun_name() {
        return item_fun_name;
    }
    public void item_fun_name(String item_fun_name) {
        this.item_fun_name = item_fun_name;
    }

    public String item_fun_id() {
        return item_fun_id;
    }
    public void item_fun_id(String item_fun_id) {
        this.item_fun_id = item_fun_id;
    }

    public String item_fun_id2() {
        return item_fun_id2;
    }
    public void item_fun_id2(String item_fun_id2) {
        this.item_fun_id2 = item_fun_id2;
    }
}