package com.example.study.service;

import com.example.study.controller.ifs.CrudInterface;
import com.example.study.model.entity.Partner;
import com.example.study.model.network.Header;
import com.example.study.model.network.Pagination;
import com.example.study.model.network.request.PartnerApiRequest;
import com.example.study.model.network.response.PartnerApiResponse;
import com.example.study.repository.CategoryRepository;
import com.example.study.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartnerApiLogicService extends BaseService<PartnerApiRequest, PartnerApiResponse, Partner> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Header<PartnerApiResponse> create(Header<PartnerApiRequest> request) {

        PartnerApiRequest body = request.getData();

        Partner partner = Partner.builder()
                                 .name(body.getName())
                                 .status(body.getStatus())
                                 .address(body.getAddress())
                                 .callCenter(body.getCallCenter())
                                 .partnerNumber(body.getPartnerNumber())
                                 .businessNumber(body.getBusinessNumber())
                                 .ceoName(body.getCeoName())
                                 .registeredAt(LocalDateTime.now())
                                 .category(categoryRepository.getOne(body.getCategoryId()))
                                 .build();

        Partner newPartner = baseRepository.save(partner);

        return Header.OK(response(newPartner));
    }

    @Override
    public Header<PartnerApiResponse> read(Long id) {

        return baseRepository.findById(id)
                                .map(partner -> response(partner))
                                .map(Header::OK)
                                .orElseGet(() -> Header.ERROR("????????? ??????"));
    }

    @Override
    public Header<PartnerApiResponse> update(Header<PartnerApiRequest> request) {

        PartnerApiRequest body = request.getData();

        return baseRepository.findById(body.getId())
                                .map(partner -> {
                                    partner.setName(body.getName())
                                            .setStatus(body.getStatus())
                                            .setAddress(body.getAddress())
                                            .setCallCenter(body.getCallCenter())
                                            .setPartnerNumber(body.getPartnerNumber())
                                            .setBusinessNumber(body.getBusinessNumber())
                                            .setCeoName(body.getCeoName())
                                            .setRegisteredAt(body.getRegisteredAt())
                                            .setUnregisteredAt(body.getUnregisteredAt())
                                            .setCategory(categoryRepository.getOne(body.getCategoryId()));

                                    return partner;
                                })
                                .map(changePartner -> baseRepository.save(changePartner))
                                .map(newPartner -> response(newPartner))
                                .map(Header::OK)
                                .orElseGet(() -> Header.ERROR("????????? ??????"));

    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                                .map(partner -> {
                                    baseRepository.delete(partner);

                                    return Header.OK();
                                })
                                .orElseGet(() -> Header.ERROR("????????? ??????"));
    }

    @Override
    public Header<List<PartnerApiResponse>> search(Pageable pageable) {

        Page<Partner> partners = baseRepository.findAll(pageable);

        List<PartnerApiResponse> partnerApiResponseList = partners.stream().map(partner -> response(partner)).collect(Collectors.toList());

        Pagination pagination = Pagination.builder()
                                          .totalPages(partners.getTotalPages())
                                          .totalElements(partners.getTotalElements())
                                          .currentPage(partners.getNumber())
                                          .currentElements(partners.getNumberOfElements())
                                          .build();

        return Header.OK(partnerApiResponseList, pagination);
    }

    private PartnerApiResponse response(Partner partner) {

        PartnerApiResponse body = PartnerApiResponse.builder()
                                                    .id(partner.getId())
                                                    .name(partner.getName())
                                                    .status(partner.getStatus())
                                                    .address(partner.getAddress())
                                                    .callCenter(partner.getCallCenter())
                                                    .partnerNumber(partner.getPartnerNumber())
                                                    .businessNumber(partner.getBusinessNumber())
                                                    .ceoName(partner.getCeoName())
                                                    .registeredAt(partner.getRegisteredAt())
                                                    .unregisteredAt(partner.getUnregisteredAt())
                                                    .categoryId(partner.getCategory().getId())
                                                    .build();

        return body;
    }

}
