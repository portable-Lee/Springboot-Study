package com.example.study.model.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentType {

    CARD(0, "카드", "카드 결제"),
    CASH(0, "현금", "현금 결제");

    private Integer id;
    private String title;
    private String description;

}
