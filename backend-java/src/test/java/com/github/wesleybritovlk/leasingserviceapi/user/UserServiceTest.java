package com.github.wesleybritovlk.leasingserviceapi.user;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCart;
import com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCartService;
import com.github.wesleybritovlk.leasingserviceapi.user.address.Address;
import com.github.wesleybritovlk.leasingserviceapi.user.address.AddressRequest;
import com.github.wesleybritovlk.leasingserviceapi.user.address.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService service;
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @Mock
    private LeasingCartService leasingCartService;
    @Mock
    private AddressService addressService;
    private User user;
    private User user2;

    @BeforeEach
    void setUp() {
        service = new UserServiceImpl(repository, mapper, leasingCartService);
        Address address = Address.builder().postalCode("99999-999").street("Street").houseNumber("123").complement("Complement").district("Neighborhood").city("City").state("UF").build();
        LeasingCart leasingCart = LeasingCart.builder().id(UUID.randomUUID()).totalPrice(BigDecimal.ZERO).firstExpirationDate(ZonedDateTime.now()).lastExpirationDate(ZonedDateTime.now()).itemsLeasing(Set.of()).build();
        user = User.builder().id(UUID.randomUUID()).fullName("User Test").cpf("00000000000").dateOfBirth(LocalDate.of(2000, 1, 1)).email("example@example.com").mobile("999999999").address(address).leasingCart(leasingCart).build();
        user2 = User.builder().id(user.getId()).fullName("User Test 1").cpf("00000000001").dateOfBirth(LocalDate.of(2001, 2, 2)).email("exampl1@exampl1.com").mobile("999999991").address(user.getAddress()).build();
    }

    private AddressRequest getAddressRequest(Address address) {
        return new AddressRequest(address.getPostalCode(), address.getStreet(), address.getHouseNumber(), address.getComplement(), address.getDistrict(), address.getCity(), address.getState());
    }

    private UserRequest getUserRequest(User user) {
        return new UserRequest(user.getFullName(), user.getCpf(), user.getDateOfBirth(), user.getEmail(), user.getMobile(), getAddressRequest(user.getAddress()), user.getImageUrl());
    }

    private UserResponse getUserResponse(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getCpf(), user.getDateOfBirth(), user.getEmail(), user.getMobile(), addressService.findByAddress(user.getAddress()), user.getImageUrl());
    }

    @Test
    void itShouldCreateUserByUserRequest() {
        // given
        UserRequest userRequest = getUserRequest(user);
        // when
        when(mapper.toUser(any(UserRequest.class))).thenReturn(user);
        when(repository.save(any(User.class))).thenReturn(user);
        User createdUser = service.create(userRequest);
        // then
        verify(mapper, Mockito.times(1)).toUser(any(UserRequest.class));
        verify(repository, Mockito.times(1)).save(any(User.class));
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getFullName()).isEqualTo(user.getFullName());
        assertThat(createdUser.getCpf()).isEqualTo(user.getCpf());
        assertThat(createdUser.getDateOfBirth()).isEqualTo(user.getDateOfBirth());
        assertThat(createdUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(createdUser.getMobile()).isEqualTo(user.getMobile());
        assertThat(createdUser.getAddress()).isEqualTo(user.getAddress());
    }

    @Test
    void itShouldGetUserResponseById() {
        // given
        UserResponse userResponse = getUserResponse(user);
        // when
        UUID expectedId = user.getId();
        when(repository.findById(expectedId)).thenReturn(Optional.ofNullable(user));
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);
        UserResponse findUser = service.findById(expectedId);
        // then
        verify(repository, Mockito.times(1)).findById(expectedId);
        verify(addressService, Mockito.times(1)).findByAddress(any(Address.class));
        verify(mapper, Mockito.times(1)).toResponse(any(User.class));
        assertThat(findUser).isNotNull();
        assertThat(findUser.id()).isNotNull();
        assertThat(findUser.id()).isEqualTo(expectedId);
        assertThat(findUser.fullName()).isEqualTo(user.getFullName());
        assertThat(findUser.cpf()).isEqualTo(user.getCpf());
        assertThat(findUser.dateOfBirth()).isEqualTo(user.getDateOfBirth());
        assertThat(findUser.email()).isEqualTo(user.getEmail());
        assertThat(findUser.mobile()).isEqualTo(user.getMobile());
        assertThatThrownBy(() -> service.findById(UUID.randomUUID()))
                .isInstanceOf(ResponseStatusException.class).hasMessageContaining("User not found, please check the id");
    }

    @Test
    void itShouldGetAllUsersResponseWithPagination() {
        // given
        UserResponse userResponse = getUserResponse(user);
        UserResponse userResponse2 = getUserResponse(user2);
        List<User> userRespons = List.of(user, user2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(userRespons, pageable, userRespons.size());
        // when
        when(repository.findAll(any(Pageable.class))).thenReturn(userPage);
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse).thenReturn(userResponse2);
        Page<UserResponse> findUsers = service.findAll(pageable);
        // then
        verify(repository, Mockito.times(1)).findAll(any(Pageable.class));
        verify(mapper, Mockito.times(2)).toResponse(any(User.class));
        assertThat(findUsers).isNotNull();
        assertThat(findUsers.getTotalElements()).isEqualTo(userRespons.size());
        assertThat(findUsers.getTotalPages()).isEqualTo(1);
        assertThat(findUsers.getContent().get(0)).isNotNull();
        assertThat(findUsers.getContent().get(0)).isEqualTo(userResponse);
        assertThat(findUsers.getContent().get(0).id()).isEqualTo(user.getId());
        assertThat(findUsers.getContent().get(1)).isNotNull();
        assertThat(findUsers.getContent().get(1)).isEqualTo(userResponse2);
        assertThat(findUsers.getContent().get(1).id()).isEqualTo(user2.getId());
    }

    @Test
    void itShouldGetUserResponseByCpf() {
        // given
        UserResponse userResponse = getUserResponse(user);
        List<User> userRespons = List.of(user);
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(userRespons, pageable, userRespons.size());
        // when
        String expectedCpf = "00000000000";
        when(repository.searchByCpf(expectedCpf, pageable)).thenReturn(userPage);
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse);
        Page<UserResponse> getAllByCpf = service.findAllByCpf(expectedCpf, pageable);
        // then
        verify(repository, Mockito.times(1)).searchByCpf(expectedCpf, pageable);
        verify(mapper, Mockito.times(1)).toResponse(any(User.class));
        assertThat(getAllByCpf).isNotNull();
        assertThat(getAllByCpf.getTotalPages()).isEqualTo(1);
        assertThat(getAllByCpf.getContent().get(0)).isNotNull();
        assertThat(getAllByCpf.getContent().get(0).id()).isEqualTo(user.getId());
        assertThat(getAllByCpf.getContent().get(0).fullName()).isEqualTo(user.getFullName());
        assertThat(getAllByCpf.getContent().get(0).cpf()).isEqualTo(expectedCpf);
    }

    @Test
    void itShouldGetAllUsersResponseByNameAndDateOfBirthWithPagination() {
        // given
        UserResponse userResponse2 = getUserResponse(user2);
        List<User> userRespons = List.of(user, user2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(userRespons, pageable, userRespons.size());
        // when
        String expectedName = "User Test 1";
        LocalDate expectedDateOfBirth = LocalDate.of(2001, 2, 2);
        String expectedName2 = "User Test 1";
        LocalDate expectedDateOfBirth2 = LocalDate.of(2001, 2, 2);
        when(repository.searchByNameAndDateOfBirth(expectedName, expectedDateOfBirth, pageable)).thenReturn(userPage);
        when(mapper.toResponse(any(User.class))).thenReturn(userResponse2);
        Page<UserResponse> findUsers = service.findAllByNameAndDateOfBirth(expectedName, expectedDateOfBirth, pageable);
        // then
        verify(repository, Mockito.times(1)).searchByNameAndDateOfBirth(expectedName, expectedDateOfBirth, pageable);
        verify(mapper, Mockito.times(2)).toResponse(any(User.class));
        assertThat(findUsers).isNotNull();
        assertThat(findUsers.getTotalElements()).isEqualTo(userRespons.size());
        assertThat(findUsers.getTotalPages()).isEqualTo(1);
        assertThat(findUsers.getContent().get(0)).isNotNull();
        assertThat(findUsers.getContent().get(0).id()).isEqualTo(user.getId());
        assertThat(findUsers.getContent().get(0).fullName()).isEqualTo(expectedName);
        assertThat(findUsers.getContent().get(0).dateOfBirth()).isEqualTo(expectedDateOfBirth);
        assertThat(findUsers.getContent().get(1)).isNotNull();
        assertThat(findUsers.getContent().get(1).id()).isEqualTo(user2.getId());
        assertThat(findUsers.getContent().get(1).fullName()).isEqualTo(expectedName2);
        assertThat(findUsers.getContent().get(1).dateOfBirth()).isEqualTo(expectedDateOfBirth2);
    }

    @Test
    void itShouldUpdateUserByIdAndUserRequest() {
        // given
        User userToUpdate = user2;
        UserRequest userRequest = getUserRequest(userToUpdate);
        // when
        UUID expectedId = user.getId();
        when(repository.findById(expectedId)).thenReturn(Optional.ofNullable(user));
        when(mapper.toUser(any(User.class), any(UserRequest.class))).thenReturn(userToUpdate);
        when(repository.save(any(User.class))).thenReturn(userToUpdate);
        User updateUser = service.update(expectedId, userRequest);
        // then
        verify(repository, Mockito.times(1)).findById(expectedId);
        verify(mapper, Mockito.times(1)).toUser(any(User.class), any(UserRequest.class));
        verify(repository, Mockito.times(1)).save(any(User.class));
        assertThat(updateUser).isNotNull();
        assertThat(updateUser.getId()).isEqualTo(expectedId);
        assertThat(updateUser.getFullName()).isEqualTo(userRequest.fullName());
        assertThat(updateUser.getCpf()).isEqualTo(userRequest.cpf());
        assertThat(updateUser.getDateOfBirth()).isEqualTo(userRequest.dateOfBirth());
        assertThat(updateUser.getEmail()).isEqualTo(userRequest.email());
        assertThat(updateUser.getMobile()).isEqualTo(userRequest.mobile());
        assertThatThrownBy(() -> service.update(UUID.randomUUID(), userRequest)).isInstanceOf(ResponseStatusException.class).hasMessageContaining("User not found, please check the id");
    }

    @Test
    void itShouldDeleteUserById() {
        // given
        User userToBeDeleted = user;
        // when
        UUID expectedId = userToBeDeleted.getId();
        when(repository.findById(userToBeDeleted.getId())).thenReturn(Optional.of(userToBeDeleted));
        service.delete(expectedId);
        // then
        verify(repository, Mockito.times(1)).findById(expectedId);
        verify(repository, Mockito.times(1)).delete(any(User.class));
        assertThatThrownBy(() -> service.delete(UUID.randomUUID())).isInstanceOf(ResponseStatusException.class).hasMessageContaining("User not found, please check the id");
    }
}