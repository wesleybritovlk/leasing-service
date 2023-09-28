package com.github.wesleybritovlk.leasingserviceapi.user;

import com.github.wesleybritovlk.leasingserviceapi.user.address.Address;
import com.github.wesleybritovlk.leasingserviceapi.user.address.AddressRequest;
import com.github.wesleybritovlk.leasingserviceapi.user.address.AddressResponse;
import com.github.wesleybritovlk.leasingserviceapi.user.address.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    private UserMapper mapper;
    @Mock
    private AddressService addressService;
    private User user;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        mapper = new UserMapperImpl(addressService);
        Address address = Address.builder().postalCode("0111222").street("Street Name").houseNumber("000").complement("N/A").district("Garden Name").city("City Name").state("UF").build();
        user = User.builder().id(UUID.randomUUID()).fullName("User Name").cpf("00011122233").dateOfBirth(LocalDate.of(2000, 1, 1)).email("example@example.com").mobile("00988887777").address(address).build();
        AddressRequest addressRequest = new AddressRequest("0111222", "Street Name", "000", "N/A", "Garden Name", "City Name", "UF");
        userRequest = new UserRequest("New User Name", "00011122233", LocalDate.of(2000, 2, 1), "newexample@example.com", "00988887776", addressRequest, "");
    }

    private static AddressResponse getAddressResponse(Address address) {
        return new AddressResponse(address.getPostalCode(), address.getStreet(), address.getHouseNumber(), address.getComplement(), address.getDistrict(), address.getCity(), address.getState());
    }

    @Test
    void itShouldBuildTheToUserWithCreationRequest() {
        // given
        UserRequest creationRequest = userRequest;
        // when
        when(addressService.create(any(AddressRequest.class))).thenReturn(user.getAddress());
        User toUserCreated = mapper.toUser(creationRequest);
        // then
        verify(addressService, times(1)).create(any(AddressRequest.class));
        assertThat(toUserCreated).isNotNull();
        assertThat(toUserCreated.getFullName()).isEqualTo(creationRequest.fullName());
        assertThat(toUserCreated.getCpf()).isEqualTo(creationRequest.cpf());
        assertThat(toUserCreated.getDateOfBirth()).isEqualTo(creationRequest.dateOfBirth());
        assertThat(toUserCreated.getEmail()).isEqualTo(creationRequest.email());
        assertThat(toUserCreated.getMobile()).isEqualTo(creationRequest.mobile());
        assertThat(toUserCreated.getAddress().getPostalCode()).isEqualTo(creationRequest.address().postalCode());
    }

    @Test
    void itShouldBuildTheToUserWithFindUserRepositoryAndUpdateRequest() {
        // given
        User findUser = user;
        UserRequest updateRequest = userRequest;
        // when
        when(addressService.update(any(Address.class), any(AddressRequest.class))).thenReturn(user.getAddress());
        User toUserUpdated = mapper.toUser(findUser, updateRequest);
        // then
        verify(addressService, times(1)).update(any(Address.class), any(AddressRequest.class));
        assertThat(toUserUpdated).isNotNull();
        assertThat(toUserUpdated.getFullName()).isEqualTo(updateRequest.fullName());
        assertThat(toUserUpdated.getCpf()).isEqualTo(updateRequest.cpf());
        assertThat(toUserUpdated.getDateOfBirth()).isEqualTo(updateRequest.dateOfBirth());
        assertThat(toUserUpdated.getEmail()).isEqualTo(updateRequest.email());
        assertThat(toUserUpdated.getMobile()).isEqualTo(updateRequest.mobile());
        assertThat(toUserUpdated.getAddress().getPostalCode()).isEqualTo(updateRequest.address().postalCode());
    }

    @Test
    void itShouldReturnUserResponseFromUser() {
        // when
        when(addressService.findByAddress(any(Address.class))).thenReturn(getAddressResponse(user.getAddress()));
        UserResponse userResponse = mapper.toResponse(user);
        // then
        verify(addressService, times(1)).findByAddress(any(Address.class));
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.id()).isEqualTo(user.getId());
        assertThat(userResponse.fullName()).isEqualTo(user.getFullName());
        assertThat(userResponse.cpf()).isEqualTo(user.getCpf());
        assertThat(userResponse.dateOfBirth()).isEqualTo(user.getDateOfBirth());
        assertThat(userResponse.email()).isEqualTo(user.getEmail());
        assertThat(userResponse.mobile()).isEqualTo(user.getMobile());
        assertThat(userResponse.address().postalCode()).isEqualTo(user.getAddress().getPostalCode());
    }
}