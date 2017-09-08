package com.amit.a20170803.whereismyfood.WSClasses;

import java.io.Serializable;

/**
 * Created by to5y on 03/08/2017.
 */

public class Branches implements Serializable {

    private String id;
    private String name;
    private String lat;
    private String lng;
    private String address;
    private String resId;
    private String markerId;

    public Branches(String id, String name, String lat, String lng, String address, String resId) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.resId = resId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }


    public String getMarkerId() {
        return markerId;
    }

    public void setMarkerId(String markerId) {
        this.markerId = markerId;
    }

}
