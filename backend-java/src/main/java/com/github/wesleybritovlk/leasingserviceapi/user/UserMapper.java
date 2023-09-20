package com.github.wesleybritovlk.leasingserviceapi.user;

public interface UserMapper {
    User toUser(UserRequest createRequest);

    User toUser(User findUser, UserRequest updateRequest);

    UserResponse toResponse(User user);
}
