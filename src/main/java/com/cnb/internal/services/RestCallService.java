package com.cnb.internal.services;
import com.cnb.internal.dtos.ItemAttributeElementsDto;
import com.cnb.internal.dtos.ItemDto;
import com.cnb.internal.dtos.ItemListResponseDto;
import com.cnb.internal.dtos.ItemTypeDescriptionDto;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

import static com.cnb.internal.config.Constants.*;

@Service
@SuppressWarnings("unchecked")
public class RestCallService {
    @Autowired
    HttpSession session;
    RestTemplate restTemplate = new RestTemplate();

    public ItemListResponseDto getAllItems(String sortBy, String sortDirection, Integer limit, Integer skip){
        //Todo: get items limited and sorted
        String url = BASE_URL+ITEM_LIST;
        if (sortBy != null)
            url = url+"?sortBy="+sortBy;
        if (sortDirection != null)
            url = url+"?sortDirection="+sortDirection;
        if (limit != null && limit > 0)
            url = url+"?limit="+limit;
        if (skip != null && skip > 0 )
            url = url+"?skip="+skip;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(session.getAttribute("token").toString());
        HttpEntity<List<ItemDto>> request = new HttpEntity<>(headers);
        ResponseEntity<ItemListResponseDto> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request,ItemListResponseDto.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
        }
        if (response != null) {
            return response.getBody();
        }
        return new ItemListResponseDto();
    }

    public List<ItemAttributeElementsDto> getAllItemAttributes() {
        String url = BASE_URL+ITEM_ATTRIBUTE_LIST;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(session.getAttribute("token").toString());
        HttpEntity<ArrayList<ItemAttributeElementsDto>> request = new HttpEntity<>(headers);
        ResponseEntity<ArrayList> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request,ArrayList.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
        }
        if (response != null) {
            return response.getBody();
        }
        return new ArrayList<>();
    }

    public ItemDto saveItem(ItemDto itemDto) {
        String url = BASE_URL+ITEM;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(session.getAttribute("token").toString());
        HttpEntity<ItemDto> request = new HttpEntity<>(itemDto,headers);

        ResponseEntity<ItemDto> response = null;
        try {
            if (itemDto.getId() == null){
                response = restTemplate.exchange(url, HttpMethod.POST, request,ItemDto.class);
            }
            else{
                response = restTemplate.exchange(url, HttpMethod.PUT, request,ItemDto.class);
            }
        }catch (HttpClientErrorException e){
            e.printStackTrace();
        }
        if (response != null) {
            return response.getBody();
        }
        return new ItemDto();
    }

    public ItemDto updateItem(ItemDto itemDto) {
        String url = BASE_URL+ITEM;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(session.getAttribute("token").toString());
        HttpEntity<ItemDto> request = new HttpEntity<>(itemDto,headers);

        ResponseEntity<ItemDto> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.PUT, request,ItemDto.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
        }
        if (response != null) {
            return response.getBody();
        }
        return new ItemDto();
    }

    public String uploadMediaToS3(String writeUrl, MultipartFile file) {
        try {
            FileEntity fileEntity = new FileEntity(convertMultiPartToFile(file));
            CloseableHttpClient httpClient = HttpClients.createSystem();
            HttpPut httpPut = new HttpPut(writeUrl);
            httpPut.setHeader("Content-Type", file.getContentType());
            httpPut.setEntity(fileEntity);
            HttpResponse response = httpClient.execute(httpPut);
            System.out.println("Response: "+ response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
         return "";
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    public List<ItemTypeDescriptionDto> getAllItemTypes() {
        String url = BASE_URL+ITEM_TYPE_LIST;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(session.getAttribute("token").toString());
        HttpEntity<ArrayList<ItemAttributeElementsDto>> request = new HttpEntity<>(headers);
        ResponseEntity<ArrayList> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request,ArrayList.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
        }
        if (response != null) {
            return response.getBody();
        }
        return new ArrayList<>();
    }

    public ItemTypeDescriptionDto getItemSubTypesByType(String typeId) {
        String url = BASE_URL+ITEM_TYPE_BY_TYPE+typeId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(session.getAttribute("token").toString());
        HttpEntity<ArrayList<ItemAttributeElementsDto>> request = new HttpEntity<>(headers);
        ResponseEntity<ItemTypeDescriptionDto> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request,ItemTypeDescriptionDto.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
        }
        if (response != null) {
            return response.getBody();
        }
        return new ItemTypeDescriptionDto();
    }

    public ItemDto getItemDetailsById(Long id) {
        String url = BASE_URL+ITEM+"/"+id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(session.getAttribute("token").toString());
        HttpEntity<ItemDto> request = new HttpEntity<>(headers);
        ResponseEntity<ItemDto> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.GET, request,ItemDto.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
        }
        if (response != null) {
            return response.getBody();
        }
        return new ItemDto();
    }

    public String deleteMediaObject(String s3Key) {
        String url = BASE_URL+DELETE_ITEM_MEDIA+"/"+s3Key;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(session.getAttribute("token").toString());
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
        }
        if (response != null && response.getStatusCodeValue() == 200) {
            return "success";
        }
        return "failure";
    }

    public String createAttribute(String attributeId) {
        String url = BASE_URL+ADD_ATTRIBUTE+attributeId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(session.getAttribute("token").toString());
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
        }
        if (response != null && response.getStatusCodeValue() == 201) {
            return "success";
        }
        return "failure";
    }

    public String deleteAttribute(String attributeId) {
        String url = BASE_URL+DELETE_ATTRIBUTE+attributeId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(session.getAttribute("token").toString());
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
        }
        if (response != null && response.getStatusCodeValue() == 200) {
            return "success";
        }
        return "failure";
    }

    public String createType(ItemTypeDescriptionDto itemTypeDescriptionDto) {
        if (itemTypeDescriptionDto.getSubtypes() == null || itemTypeDescriptionDto.getSubtypes().size() == 0){
            ArrayList<String> arrayList = new ArrayList<>();arrayList.add("NA");
            itemTypeDescriptionDto.setSubtypes(arrayList);
        }
        String url = BASE_URL+ADD_TYPE;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(session.getAttribute("token").toString());
        HttpEntity<ItemTypeDescriptionDto> request = new HttpEntity<>(itemTypeDescriptionDto,headers);

        ResponseEntity<ItemTypeDescriptionDto> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, request,ItemTypeDescriptionDto.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
        }
        if (response != null) {
            return "success";
        }
        return "failure";
    }

    public String deleteType(String type) {
        String url = BASE_URL+DELETE_TYPE+type;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(session.getAttribute("token").toString());
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
        }
        if (response != null && response.getStatusCodeValue() == 200) {
            return "success";
        }
        return "failure";
    }

    public String deleteItem(Long id) {
        String url = BASE_URL+ITEM+"/"+id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(session.getAttribute("token").toString());
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
        }catch (HttpClientErrorException e){
            e.printStackTrace();
        }
        if (response != null && response.getStatusCodeValue() == 200) {
            return "success";
        }
        return "failure";
    }
}
