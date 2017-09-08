package com.amit.a20170803.whereismyfood.WSClasses;

import java.io.Serializable;

/**
 * Created by to5y on 03/08/2017.
 */

public class Menus implements Serializable {

    private String id;
    private String name;
    private String price;
    private String type;
    private String desc;
    private String branchId;
    private int qty;
    private boolean checked;


    public Menus(String id, String name, String price, String type, String desc, String branchId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
        this.desc = desc;
        this.branchId = branchId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }


    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
