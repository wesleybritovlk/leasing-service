package com.github.wesleybritovlk.leasingserviceapi.leasingcart;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing.ItemLeasingRepository;
import com.github.wesleybritovlk.leasingserviceapi.user.User;
import com.github.wesleybritovlk.leasingserviceapi.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeasingCartServiceTest {
    private LeasingCartService service;
    @Mock
    private LeasingCartRepository repository;
    @Mock
    private LeasingCartMapper mapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemLeasingRepository itemLeasingRepository;
    private LeasingCart leasingCart;
    private LeasingCartRequest leasingCartRequest;
    private User user;
    private LeasingCartResponse leasingCartResponse;

    @BeforeEach
    void setUp() {
        service = new LeasingCartServiceImpl(repository, mapper, userRepository, itemLeasingRepository);
        user = User.builder().id(UUID.randomUUID()).fullName("User Test").cpf("00000000000").dateOfBirth(LocalDate.of(2000, 1, 1)).email("example@example.com").mobile("999999999").build();
        leasingCart = LeasingCart.builder().id(UUID.randomUUID()).totalPrice(BigDecimal.ZERO).firstExpirationDate(ZonedDateTime.now()).lastExpirationDate(ZonedDateTime.now()).itemsLeasing(Set.of()).user(user).build();
        leasingCartRequest = new LeasingCartRequest(user.getId());
        leasingCartResponse = new LeasingCartResponse(leasingCart.getId(), leasingCart.getUser().getId(), leasingCart.getUser().getFullName(), leasingCart.getTotalPrice(), leasingCart.getFirstExpirationDate(), leasingCart.getLastExpirationDate(), null);
    }

    @Test
    void itShouldCreateLeasingByLeasingRequest() {
        // when
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(mapper.toLeasing(any(User.class))).thenReturn(leasingCart);
        when(repository.save(any(LeasingCart.class))).thenReturn(leasingCart);
        LeasingCart createdLeasingCart = service.create(leasingCartRequest);
        // then
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(mapper, times(1)).toLeasing(any(User.class));
        verify(repository, times(1)).save(any(LeasingCart.class));
        assertThat(createdLeasingCart).isNotNull();
        assertThat(createdLeasingCart.getId()).isNotNull();
        assertThat(createdLeasingCart.getTotalPrice()).isEqualTo(BigDecimal.ZERO);
        assertThat(createdLeasingCart.getFirstExpirationDate()).isNotNull();
        assertThat(createdLeasingCart.getLastExpirationDate()).isNotNull();
        assertThat(createdLeasingCart.getUser()).isNotNull();
        assertThat(createdLeasingCart.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void itShouldGetLeasingResponseById() {
        // when
        when(repository.findById(any(UUID.class))).thenReturn(Optional.of(leasingCart));
        when(mapper.toResponse(any(LeasingCart.class))).thenReturn(this.leasingCartResponse);
        LeasingCartResponse leasingCartResponse = service.findById(leasingCart.getId());
        // then
        verify(repository, times(1)).findById(any(UUID.class));
        verify(mapper, times(1)).toResponse(any(LeasingCart.class));
        assertThat(leasingCartResponse).isNotNull();
        assertThat(leasingCartResponse.id()).isEqualTo(leasingCart.getId());
    }

    @Test
    void itShouldGetAllLeasingCartsResponseWithPagination() {
        // given
        LeasingCart leasingCart2 = LeasingCart.builder().id(UUID.randomUUID()).totalPrice(BigDecimal.ZERO).firstExpirationDate(ZonedDateTime.now()).lastExpirationDate(ZonedDateTime.now()).itemsLeasing(Set.of()).user(user).build();
        LeasingCartResponse leasingCartResponse2 = new LeasingCartResponse(leasingCart2.getId(), leasingCart2.getUser().getId(), leasingCart2.getUser().getFullName(), leasingCart2.getTotalPrice(), leasingCart2.getFirstExpirationDate(), leasingCart2.getLastExpirationDate(), null);
        List<LeasingCart> leasingCartCartsRespons = List.of(leasingCart, leasingCart2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<LeasingCart> leasingResponsePage = new PageImpl<>(leasingCartCartsRespons);
        // when
        when(repository.findAll(any(Pageable.class))).thenReturn(leasingResponsePage);
        when(mapper.toResponse(any(LeasingCart.class))).thenReturn(leasingCartResponse).thenReturn(leasingCartResponse2);
        Page<LeasingCartResponse> findLeasingCarts = service.findAll(pageable);
        // then
        verify(repository, times(1)).findAll(any(Pageable.class));
        verify(mapper, times(2)).toResponse(any(LeasingCart.class));
        assertThat(findLeasingCarts).isNotNull();
        assertThat(findLeasingCarts.getTotalElements()).isEqualTo(leasingCartCartsRespons.size());
        assertThat(findLeasingCarts.getTotalPages()).isEqualTo(1);
        assertThat(findLeasingCarts.getContent().get(0)).isNotNull();
        assertThat(findLeasingCarts.getContent().get(0)).isEqualTo(leasingCartResponse);
        assertThat(findLeasingCarts.getContent().get(0).id()).isEqualTo(leasingCart.getId());
        assertThat(findLeasingCarts.getContent().get(1)).isNotNull();
        assertThat(findLeasingCarts.getContent().get(1)).isEqualTo(leasingCartResponse2);
        assertThat(findLeasingCarts.getContent().get(1).id()).isEqualTo(leasingCart2.getId());
    }

    @Test
    void itShouldUpdateLeasingByIdAndLeasingRequest() {
        // given
        User user2 = User.builder().id(UUID.randomUUID()).fullName("User Test").cpf("00000000002").dateOfBirth(LocalDate.of(2002, 2, 2)).email("example2@example.com").mobile("119999998").build();
        LeasingCartRequest leasingCartRequestUpdate = new LeasingCartRequest(user2.getId());
        LeasingCart leasingCart2 = LeasingCart.builder().id(leasingCart.getId()).firstExpirationDate(ZonedDateTime.now()).lastExpirationDate(ZonedDateTime.now()).itemsLeasing(Set.of()).user(user2).build();
        // when
        UUID expectedLeasingId = leasingCart2.getId();
        when(repository.findById(expectedLeasingId)).thenReturn(Optional.of(leasingCart));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user2));
        when(mapper.toLeasing(any(LeasingCart.class), any(User.class))).thenReturn(leasingCart2);
        when(repository.save(any(LeasingCart.class))).thenReturn(leasingCart2);
        LeasingCart updatedLeasingCart = service.update(expectedLeasingId, leasingCartRequestUpdate);
        // then
        verify(repository, times(1)).findById(expectedLeasingId);
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(mapper, times(1)).toLeasing(any(LeasingCart.class), any(User.class));
        verify(repository, times(1)).save(any(LeasingCart.class));
        assertThat(updatedLeasingCart).isNotNull();
        assertThat(updatedLeasingCart.getId()).isEqualTo(expectedLeasingId);
        assertThat(updatedLeasingCart.getTotalPrice()).isEqualTo(BigDecimal.ZERO);
        assertThat(updatedLeasingCart.getFirstExpirationDate()).isNotNull();
        assertThat(updatedLeasingCart.getLastExpirationDate()).isNotNull();
        assertThat(updatedLeasingCart.getUser()).isNotNull();
        assertThat(updatedLeasingCart.getUser().getId()).isEqualTo(user2.getId());
        assertThat(updatedLeasingCart.getUser().getFullName()).isEqualTo(user2.getFullName());
        assertThatThrownBy(() -> service.update(UUID.randomUUID(), leasingCartRequest)).isInstanceOf(ResponseStatusException.class).hasMessageContaining("Leasing not found, please check the id");

    }

    @Test
    void itShouldDeleteLeasingById() {
        // given
        LeasingCart leasingCartToBeDeleted = leasingCart;
        // when
        UUID expectedLeasingId = leasingCartToBeDeleted.getId();
        when(repository.findById(expectedLeasingId)).thenReturn(Optional.of(leasingCartToBeDeleted));
        service.delete(expectedLeasingId);
        // then
        verify(repository, times(1)).findById(any(UUID.class));
        verify(repository, times(1)).delete(any(LeasingCart.class));
        assertThatThrownBy(() -> service.delete(UUID.randomUUID())).isInstanceOf(ResponseStatusException.class).hasMessageContaining("Leasing not found, please check the id");
    }
}