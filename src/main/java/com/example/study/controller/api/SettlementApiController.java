package com.example.study.controller.api;

import com.example.study.model.network.Header;
import com.example.study.model.network.response.SettlementApiResponse;
import com.example.study.service.SettlementApiLogicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/********* table create sql ********

CREATE TABLE `study`.`settlement` (
  `user_id` BIGINT(20) NOT NULL,
  `price` DECIMAL(12,4) NULL DEFAULT 0,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_bin;

************************************/



@RestController
@RequestMapping("/settlement")
public class SettlementApiController {

    @Autowired
    private SettlementApiLogicService settlementApiLogicService;

    @GetMapping("{id}")
    public Header<SettlementApiResponse> orderPriceInfo(@PathVariable Long id) {
        return settlementApiLogicService.orderPriceInfo(id);
    }

}
