package com.example.study.model.enumclass;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentType {

    BANK_TRANSFER(0, "계좌 이체", "계좌 이체 결제"),
    CARD(1, "카드", "카드 결제"),
    CHECK_CARD(2, "현금", "현금 결제");

    private Integer id;
    private String title;
    private String description;

}
