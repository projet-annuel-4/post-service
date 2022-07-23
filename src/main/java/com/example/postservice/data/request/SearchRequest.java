package com.example.postservice.data.request;

import com.example.postservice.domain.model.SearchFilter;

import java.util.List;

public class SearchRequest {

    private List<SearchFilter> filters;

    public SearchRequest(List<SearchFilter> filters) {
        this.filters = filters;
    }

    public SearchRequest() {
    }

    public List<SearchFilter> getFilters() {
        return filters;
    }
}
