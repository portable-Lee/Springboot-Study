package com.example.study.model.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderDetailStatus {

    WAITING(0, "대기", "상품 발송 대기"),
    COMPLETE(0, "완료", "상품 발송 완료");

    private Integer id;
    private String title;
    private String description;

}
