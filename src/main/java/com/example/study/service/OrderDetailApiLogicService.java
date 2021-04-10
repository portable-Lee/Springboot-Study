package com.example.study.service;

import com.example.study.controller.ifs.CrudInterface;
import com.example.study.model.entity.OrderDetail;
import com.example.study.model.network.Header;
import com.example.study.model.network.Pagination;
import com.example.study.model.network.request.OrderDetailApiRequest;
import com.example.study.model.network.response.OrderDetailApiResponse;
import com.example.study.repository.ItemRepository;
import com.example.study.repository.OrderDetailRepository;
import com.example.study.repository.OrderGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailApiLogicService extends BaseService<OrderDetailApiRequest, OrderDetailApiResponse, OrderDetail> {

    @Autowired
    private OrderGroupRepository orderGroupRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Header<OrderDetailApiResponse> create(Header<OrderDetailApiRequest> request) {

        OrderDetailApiRequest body = request.getData();

        OrderDetail orderDetail = OrderDetail.builder()
                                             .status(body.getStatus())
                                             .quantity(body.getQuantity())
                                             .totalPrice(body.getTotalPrice())
                                             .orderGroup(orderGroupRepository.getOne(body.getOrderGroupId()))
                                             .item(itemRepository.getOne(body.getItemId()))
                                             .build();

        OrderDetail newOrderDetail = baseRepository.save(orderDetail);

        return Header.OK(response(newOrderDetail));
    }

    @Override
    public Header<OrderDetailApiResponse> read(Long id) {

        return baseRepository.findById(id)
                                    .map(orderDetail -> response(orderDetail))
                                    .map(Header::OK)
                                    .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<OrderDetailApiResponse> update(Header<OrderDetailApiRequest> request) {

        OrderDetailApiRequest body = request.getData();

        return baseRepository.findById(body.getId())
                                    .map(orderDetail -> {
                                        orderDetail.setStatus(body.getStatus())
                                                   .setArrivalDate(body.getArrivalDate())
                                                   .setQuantity(body.getQuantity())
                                                   .setTotalPrice(body.getTotalPrice())
                                                   .setOrderGroup(orderGroupRepository.getOne(body.getOrderGroupId()))
                                                   .setItem(itemRepository.getOne(body.getItemId()));

                                        return orderDetail;
                                    })
                                    .map(changeOrderDetail -> baseRepository.save(changeOrderDetail))
                                    .map(newOrderDetail -> response(newOrderDetail))
                                    .map(Header::OK)
                                    .orElseGet(() -> Header.ERROR("데이터 없음"));

    }

    @Override
    public Header delete(Long id) {

        return baseRepository.findById(id)
                                    .map(orderDetail -> {
                                        baseRepository.delete(orderDetail);

                                        return Header.OK();
                                    })
                                    .orElseGet(() -> Header.ERROR("데이터 없음"));

    }

    @Override
    public Header<List<OrderDetailApiResponse>> search(Pageable pageable) {

        Page<OrderDetail> orderDetails = baseRepository.findAll(pageable);

        List<OrderDetailApiResponse> orderDetailApiResponseList = orderDetails.stream().map(this::response).collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                                          .totalPages(orderDetails.getTotalPages())
                                          .totalElements(orderDetails.getTotalElements())
                                          .currentPage(orderDetails.getNumber())
                                          .currentElements(orderDetails.getNumberOfElements())
                                          .build();

        return Header.OK(orderDetailApiResponseList, pagination);
    }

    private OrderDetailApiResponse response(OrderDetail orderDetail) {

        OrderDetailApiResponse body = OrderDetailApiResponse.builder()
                                                            .id(orderDetail.getId())
                                                            .status(orderDetail.getStatus())
                                                            .arrivalDate(orderDetail.getArrivalDate())
                                                            .quantity(orderDetail.getQuantity())
                                                            .totalPrice(orderDetail.getTotalPrice())
                                                            .orderGroupId(orderDetail.getOrderGroup().getId())
                                                            .itemId(orderDetail.getItem().getId())
                                                            .build();

        return body;

    }

}
