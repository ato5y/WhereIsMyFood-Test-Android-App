package com.amit.a20170803.whereismyfood.WSClasses;

import java.util.ArrayList;

/**
 * Created by to5y on 03/08/2017.
 */

public class Restaurant {
    private String id;
    private String name;
    private String imgUrl;

    public Restaurant(String id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
