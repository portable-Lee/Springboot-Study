package com.example.study.model;

import lombok.Data;

@Data
public class SearchParam {

    private String account;
    private String email;
    private int page;

    // {"account" : "", "email" : "", "page" : 0}

}
