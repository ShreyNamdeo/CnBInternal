package com.cnb.internal.controllers;
import com.cnb.internal.dtos.ItemDto;
import com.cnb.internal.dtos.ItemListResponseDto;
import com.cnb.internal.dtos.ItemTypeDescriptionDto;
import com.cnb.internal.services.RestCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("rest")
public class AjaxRequestController {
    @Autowired
    RestCallService restCallService;

    @PostMapping("/itemAttributes")
    public String addItemAttributes(HttpSession session, @RequestBody ItemDto itemDto){
        ItemDto item = (ItemDto) session.getAttribute("itemDto");
        item.setItemAttributes(itemDto.getItemAttributes());
        session.setAttribute("itemDto",restCallService.updateItem(item));
        return "success";
    }

    @GetMapping("/item/subtypes/{type}")
    public ItemTypeDescriptionDto getAllSubTypesForItemType(@PathVariable("type") String type){
        return restCallService.getItemSubTypesByType(type);
    }

    @DeleteMapping("/item/media/delete/{s3Key}")
    public String deleteItemMedia(@PathVariable("s3Key") String s3Key){
        return restCallService.deleteMediaObject(s3Key);
    }

    @PostMapping("/create/attribute/{attributeId}")
    public String createItemAttribute(@PathVariable("attributeId") String attributeId ){
        return restCallService.createAttribute(attributeId);
    }

    @DeleteMapping("/delete/attribute/{attributeId}")
    public String deleteItemAttribute(@PathVariable("attributeId") String attributeId){
        return restCallService.deleteAttribute(attributeId);
    }
    @PostMapping("/create/type")
    public String createTypeWithSubtypes(@RequestBody ItemTypeDescriptionDto itemTypeDescriptionDto){
        return restCallService.createType(itemTypeDescriptionDto);
    }
    @DeleteMapping("/delete/type/{type}")
    public String deleteTypeAndSubtypes(@PathVariable("type") String type){
        return restCallService.deleteType(type);
    }

    @GetMapping("/item/list")
    public ItemListResponseDto getItemsList(@RequestParam(value = "sortBy", required = false) String sortBy,
                                            @RequestParam(value = "sortDirection", required = false) String sortDirection,
                                            @RequestParam(value = "limit", required = false) Integer limit,
                                            @RequestParam(value = "skip", required = false) Integer skip){
        return restCallService.getAllItems(sortBy, sortDirection, limit, skip);
    }

    @DeleteMapping("/delete/item/{id}")
    public String deleteItem(@PathVariable("id") Long id){
        return restCallService.deleteItem(id);
    }
}
