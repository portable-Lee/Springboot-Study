package com.example.study.model.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderDetailStatus {

    WAITING(0, "대기", "상품 발송 대기"),
    ORDERING(1, "주문중", "상품 주문 상태"),
    CONFIRM(2, "확인", "상품 주문 확인"),
    COMPLETE(3, "완료", "상품 발송 완료");

    private Integer id;
    private String title;
    private String description;

}
