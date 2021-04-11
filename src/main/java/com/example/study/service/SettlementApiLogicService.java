package com.example.study.service;

import com.example.study.model.entity.OrderGroup;
import com.example.study.model.entity.Settlement;
import com.example.study.model.entity.User;
import com.example.study.model.network.Header;
import com.example.study.model.network.response.SettlementApiResponse;
import com.example.study.repository.SettlementRepository;
import com.example.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class SettlementApiLogicService {

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private UserRepository userRepository;

    public Header<SettlementApiResponse> orderPriceInfo(Long id) {

        // 1. user 정보 조회
        User user = userRepository.getOne(id);

        // 2. 해당 user의 누적 사용 금액 계산
        BigDecimal totalPrice = user.getOrderGroupList().stream()
                                                        .map(OrderGroup::getTotalPrice)
                                                        .collect(Collectors.toList())               // 해당 user의 order_group의 total_price들을 List로 변경
                                                        .stream()
                                                        .reduce(BigDecimal.ZERO, BigDecimal::add)   // total_price 총합 계산
                                                        .stripTrailingZeros();                      // 소수점 뒤의 0 제거

        // 3. create settlement
        Settlement settlement = Settlement.builder().userId(id).price(totalPrice).build();
        Settlement newSettlement = settlementRepository.save(settlement);

        // 4. read settlement
        return settlementRepository.findById(id)
                                   .map(this::response)
                                   .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    private Header<SettlementApiResponse> response(Settlement settlement) {

        SettlementApiResponse body = SettlementApiResponse.builder().userId(settlement.getUserId()).price(settlement.getPrice()).build();

        return Header.OK(body);

    }

}
