package com.example.study.service;

import com.example.study.controller.ifs.CrudInterface;
import com.example.study.model.entity.OrderGroup;
import com.example.study.model.network.Header;
import com.example.study.model.network.Pagination;
import com.example.study.model.network.request.OrderGroupApiRequest;
import com.example.study.model.network.response.OrderGroupApiResponse;
import com.example.study.repository.OrderGroupRepository;
import com.example.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderGroupApiLogicService extends BaseService<OrderGroupApiRequest, OrderGroupApiResponse, OrderGroup> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Header<OrderGroupApiResponse> create(Header<OrderGroupApiRequest> request) {

        OrderGroupApiRequest body = request.getData();

        OrderGroup orderGroup = OrderGroup.builder()
                                          .status(body.getStatus())
                                          .orderType(body.getOrderType())
                                          .revAddress(body.getRevAddress())
                                          .revName(body.getRevName())
                                          .paymentType(body.getPaymentType())
                                          .totalPrice(body.getTotalPrice())
                                          .totalQuantity(body.getTotalQuantity())
                                          .orderAt(LocalDateTime.now())
                                          .user(userRepository.getOne(body.getUserId()))
                                          .build();
        OrderGroup newOrderGroup = baseRepository.save(orderGroup);

        return Header.OK(response(newOrderGroup));
    }

    @Override
    public Header<OrderGroupApiResponse> read(Long id) {

        return baseRepository.findById(id)
                                    .map(orderGroup -> response(orderGroup))        // .map(this::response)
                                    .map(Header::OK)
                                    .orElseGet(() -> Header.ERROR("????????? ??????"));

    }

    @Override
    public Header<OrderGroupApiResponse> update(Header<OrderGroupApiRequest> request) {

        OrderGroupApiRequest body = request.getData();

        return baseRepository.findById(body.getId())
                                    .map(orderGroup -> {
                                        orderGroup.setStatus(body.getStatus())
                                                  .setOrderType(body.getOrderType())
                                                  .setRevAddress(body.getRevAddress())
                                                  .setRevName(body.getRevName())
                                                  .setPaymentType(body.getPaymentType())
                                                  .setTotalPrice(body.getTotalPrice())
                                                  .setTotalQuantity(body.getTotalQuantity())
                                                  .setOrderAt(body.getOrderAt())
                                                  .setArrivalDate(body.getArrivalDate())
                                                  .setUser(userRepository.getOne(body.getUserId()));

                                        return orderGroup;
                                    })
                                    .map(changeOrderGroup -> baseRepository.save(changeOrderGroup))
                                    .map(newOrderGroup -> response(newOrderGroup))      // .map(this::response)
                                    .map(Header::OK)
                                    .orElseGet(() -> Header.ERROR("????????? ??????"));

    }

    @Override
    public Header delete(Long id) {

        return baseRepository.findById(id)
                                    .map(orderGroup -> {
                                        baseRepository.delete(orderGroup);

                                        return Header.OK();
                                    })
                                    .orElseGet(() -> Header.ERROR("????????? ??????"));
    }

    @Override
    public Header<List<OrderGroupApiResponse>> search(Pageable pageable) {

        Page<OrderGroup> orderGroups = baseRepository.findAll(pageable);

        List<OrderGroupApiResponse> orderGroupApiResponseList = orderGroups.stream().map(orderGroup -> response(orderGroup)).collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                                          .totalPages(orderGroups.getTotalPages())
                                          .totalElements(orderGroups.getTotalElements())
                                          .currentPage(orderGroups.getNumber())
                                          .currentElements(orderGroups.getNumberOfElements())
                                          .build();

        return Header.OK(orderGroupApiResponseList, pagination);
    }

    public OrderGroupApiResponse response(OrderGroup orderGroup) {

        OrderGroupApiResponse body = OrderGroupApiResponse.builder()
                                                          .id(orderGroup.getId())
                                                          .status(orderGroup.getStatus())
                                                          .orderType(orderGroup.getOrderType())
                                                          .revAddress(orderGroup.getRevAddress())
                                                          .revName(orderGroup.getRevName())
                                                          .paymentType(orderGroup.getPaymentType())
                                                          .totalPrice(orderGroup.getTotalPrice())
                                                          .totalQuantity(orderGroup.getTotalQuantity())
                                                          .orderAt(orderGroup.getOrderAt())
                                                          .arrivalDate(orderGroup.getArrivalDate())
                                                          .userId(orderGroup.getUser().getId())
                                                          .build();

        return body;
    }

}
