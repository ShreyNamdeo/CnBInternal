package com.cnb.internal.controllers;
import com.cnb.internal.dtos.ItemAttributeElementsDto;
import com.cnb.internal.dtos.ItemDto;
import com.cnb.internal.dtos.ItemTypeDescriptionDto;
import com.cnb.internal.services.RestCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InternalCnbToolViewController {
    @Autowired
    RestCallService restCallService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model, HttpSession httpSession){
        //Todo: remove this when working with authentication
        //httpSession.setAttribute("token","ZXlKaGRYUm9iM0pwZW1GMGFXOXVJam9pT0RJd09EQTBNekEzTm40aFFDTnBiblJsY201aGJINGhRQ01pTENKaGJHY2lPaUpJVXpJMU5pSjkuZTMwLmVNS2o5VmFobmxlS29mWDE0elRfOUx2WnIxZDBONXFBUFl3VG1obkNsSU06IA==");

        model.addAttribute("token",httpSession.getAttribute("token"));
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/item", method = RequestMethod.GET)
    public String addItem(Model model, HttpSession httpSession, @RequestParam(value = "item", required = false) Long id){
        ItemDto itemDto = new ItemDto();
        if (id != null){
            itemDto = restCallService.getItemDetailsById(id);
            httpSession.setAttribute("itemDto",itemDto);
        }
        model.addAttribute("itemDto",itemDto);
        List<ItemTypeDescriptionDto> allItemTypes = restCallService.getAllItemTypes();
        if (allItemTypes != null)
            model.addAttribute("itemTypeList", allItemTypes);
        else
            model.addAttribute("itemTypeList", new ArrayList<String>());
        //model.addAttribute("token",httpSession.getAttribute("token"));
        return "itemForm";
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET)
    public String itemsList(){return "items";}

    @RequestMapping(value = "/vendors", method = RequestMethod.GET)
    public String vendorsList(){return "vendors";}

    @PostMapping("/item/attributes")
    public String addItemAttributes(Model model, @ModelAttribute ItemDto itemDto, HttpSession session){
        if (itemDto.getId() != null){
            itemDto.getType().setItemId(itemDto.getId());
            ItemDto sessionItem = (ItemDto)session.getAttribute("itemDto");
            if (sessionItem.getId().equals(itemDto.getId())){
                itemDto.setItemAttributes(sessionItem.getItemAttributes());
            }
        }
        itemDto = restCallService.saveItem(itemDto);
        session.setAttribute("itemDto",itemDto);
        List<ItemAttributeElementsDto> allAttributes = restCallService.getAllItemAttributes();
        if (allAttributes != null)
            model.addAttribute("itemAttributesList", allAttributes);
        else
            model.addAttribute("itemAttributesList", new ArrayList<String>());

        if (itemDto.getItemAttributes() != null)
            model.addAttribute("itemAttributesValueList", itemDto.getItemAttributes());
        else
            model.addAttribute("itemAttributesValueList", new ArrayList<>());

        return "itemAttributesForm";
    }

    @GetMapping("/item/media")
    public String addItemMedia(Model model, HttpSession session){
        ItemDto item = (ItemDto) session.getAttribute("itemDto");
        if (item != null){
            item = restCallService.getItemDetailsById(item.getId());
            session.setAttribute("itemDto",item);
            model.addAttribute("images",item.getImages());
        }
        return "itemMediaForm";
    }

    @PostMapping("/item/media/upload")
    public String addItemMediaUpload(Model model, HttpSession session, @RequestParam("files")MultipartFile[] files){
        ItemDto item = (ItemDto) session.getAttribute("itemDto");
        //ItemDto item = new ItemDto();
        //item.setId(48L);
        if (item != null){
            item = restCallService.getItemDetailsById(item.getId());
            session.setAttribute("itemDto",item);
            if (files.length>0){
                item.setNumberOfImages(item.getNumberOfImages() + files.length);
                int previousImgCounter = item.getImages().size();
                item = restCallService.updateItem(item);
                for (int i=0;i<files.length;i++){
                    restCallService.uploadMediaToS3(item.getImages().get(previousImgCounter+i).getWriteUrl(),files[i]);
                }
            }
        }
        return "redirect:/item/media";
    }


    @PostMapping("/add/item/result")
    public String addItemResult(Model model){
        return "formAddSuccess";
    }

    @GetMapping("/item/section/modify")
    public String itemSectionsModify(Model model){
        List<ItemTypeDescriptionDto> allItemTypes = restCallService.getAllItemTypes();
        if (allItemTypes != null)
            model.addAttribute("itemTypeList", allItemTypes);
        else
            model.addAttribute("itemTypeList", new ArrayList<String>());

        List<ItemAttributeElementsDto> allAttributes = restCallService.getAllItemAttributes();
        if (allAttributes != null)
            model.addAttribute("itemAttributesList", allAttributes);
        else
            model.addAttribute("itemAttributesList", new ArrayList<String>());
        return "modifyItemSections";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request , HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }
        return "redirect:/login?logout";
    }
}
