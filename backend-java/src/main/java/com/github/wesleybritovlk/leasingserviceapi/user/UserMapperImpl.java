package com.github.wesleybritovlk.leasingserviceapi.user;

import com.github.wesleybritovlk.leasingserviceapi.user.address.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.time.ZoneId.of;
import static java.time.ZonedDateTime.now;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    private final AddressService addressService;

    @Override
    public User toUser(UserRequest createRequest) {
        return User.builder().fullName(createRequest.name()).cpf(createRequest.cpf()).dateOfBirth(createRequest.dateOfBirth()).email(createRequest.email()).mobile(createRequest.mobile()).address(addressService.create(createRequest.address())).imageUrl(createRequest.image()).createdAt(now(of("America/Sao_Paulo"))).updatedAt(now(of("America/Sao_Paulo"))).build();
    }

    @Override
    public User toUser(User findUser, UserRequest updateRequest) {
        return User.builder().id(findUser.getId()).fullName(updateRequest.name()).cpf(findUser.getCpf()).dateOfBirth(updateRequest.dateOfBirth()).email(updateRequest.email()).mobile(updateRequest.mobile()).address(addressService.update(findUser.getAddress(), updateRequest.address())).imageUrl(updateRequest.image()).leasingCart(findUser.getLeasingCart()).createdAt(findUser.getCreatedAt()).updatedAt(now(of("America/Sao_Paulo"))).build();
    }

    @Override
    public UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getCpf(), user.getDateOfBirth(), user.getEmail(), user.getMobile(), addressService.findByAddress(user.getAddress()), user.getImageUrl());
    }
}