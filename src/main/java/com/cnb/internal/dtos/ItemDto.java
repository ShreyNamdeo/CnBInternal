package com.cnb.internal.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {
    private Long id;
    private String name;
    private Float price;
    private String brand;
    private String description;
    private List<ItemAttributeDto> itemAttributes;
    private ItemTypeDto type;
    private int numberOfImages;
    private List<ImageDto> images;

    public ItemDto(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ItemAttributeDto> getItemAttributes() {
        return itemAttributes;
    }

    public void setItemAttributes(List<ItemAttributeDto> itemAttributes) {
        this.itemAttributes = itemAttributes;
    }

    public ItemTypeDto getType() {
        return type;
    }

    public void setType(ItemTypeDto type) {
        this.type = type;
    }

    public int getNumberOfImages() {
        return numberOfImages;
    }

    public void setNumberOfImages(int numberOfImages) {
        this.numberOfImages = numberOfImages;
    }

    public List<ImageDto> getImages() {
        return images;
    }

    public void setImages(List<ImageDto> images) {
        this.images = images;
    }
}