package com.cnb.internal.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemAttributeDto {
    private Long id;
    private Long itemId;
    private String itemAttributeId;
    private String value;

    public ItemAttributeDto(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemAttributeId() {
        return itemAttributeId;
    }

    public void setItemAttributeId(String itemAttributeId) {
        this.itemAttributeId = itemAttributeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

