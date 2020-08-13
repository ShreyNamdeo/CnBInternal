package com.cnb.internal.dtos;

import java.util.ArrayList;

public class ItemTypeDescriptionDto {
    private String type;
    private ArrayList<String> subtypes;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(ArrayList<String> subtypes) {
        this.subtypes = subtypes;
    }
}
