package com.cnb.internal.dtos;

import java.util.List;

public class ItemListResponseDto {
    private List<ItemDto> list;
    private PageDto page;

    public List<ItemDto> getList() {
        return list;
    }

    public void setList(List<ItemDto> list) {
        this.list = list;
    }

    public PageDto getPage() {
        return page;
    }

    public void setPage(PageDto page) {
        this.page = page;
    }
}
