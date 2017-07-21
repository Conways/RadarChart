package com.conways.radarchart;

/**
 * Created by Conways on 2017/7/21.
 */

public class RadarData {

    private String itemName;
    private int abliity;

    public RadarData(String itemName, int abliity) {
        this.itemName = itemName;
        this.abliity = abliity;
    }

    public RadarData() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getAbliity() {
        return abliity;
    }

    public void setAbliity(int abliity) {
        this.abliity = abliity;
    }
}
