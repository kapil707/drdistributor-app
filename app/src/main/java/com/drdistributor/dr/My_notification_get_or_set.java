package com.drdistributor.dr;

import java.util.ArrayList;

public class My_notification_get_or_set {
    private String item_id,item_title,item_message,item_date_time,item_image;
    public My_notification_get_or_set() {
    }

    public My_notification_get_or_set(String item_id, String item_title, String item_message, String item_date_time, String item_image,
                                      ArrayList<String> genre) {
        this.item_id = item_id;
        this.item_title = item_title;
        this.item_message = item_message;
        this.item_date_time = item_date_time;
        this.item_image = item_image;
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
}