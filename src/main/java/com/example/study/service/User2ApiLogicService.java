package com.example.study.service;

import com.example.study.controller.ifs.CrudInterface;
import com.example.study.model.entity.User;
import com.example.study.model.enumclass.UserStatus;
import com.example.study.model.network.Header;
import com.example.study.model.network.request.UserApiRequest;
import com.example.study.model.network.response.UserApiResponse;
import com.example.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
public class User2ApiLogicService implements CrudInterface<UserApiRequest, UserApiResponse> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request) {

        UserApiRequest body = request.getData();

        if (!(userRepository.findByEmail(body.getEmail()).isPresent())) {

            User user = User.builder()
                            .account(body.getAccount())
                            .password(body.getPassword())
                            .status(UserStatus.REGISTERED)
                            .phoneNumber(body.getPhoneNumber())
                            .email(body.getEmail())
                            .registeredAt(LocalDateTime.now())
                            .build();
            User newUser = userRepository.save(user);

            return response(newUser);
        } else {
            return Header.ERROR("400 Bad Request");
        }

    }

    @Override
    public Header<UserApiResponse> read(Long id) {
        return null;
    }

    @Override
    public Header<UserApiResponse> update(Header<UserApiRequest> request) {
        return null;
    }

    @Override
    public Header delete(Long id) {
        return null;
    }

    private Header<UserApiResponse> response(User user) {

        UserApiResponse body = UserApiResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .registeredAt(user.getRegisteredAt())
                .unregisteredAt(user.getUnregisteredAt())
                .build();

        return Header.OK(body);

    }

}
