package com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCart;
import com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCartRepository;
import com.github.wesleybritovlk.leasingserviceapi.product.Product;
import com.github.wesleybritovlk.leasingserviceapi.product.ProductRepository;
import com.github.wesleybritovlk.leasingserviceapi.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.TreeSet;
import java.util.UUID;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.valueOf;
import static java.time.LocalDate.of;
import static java.util.Comparator.comparing;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemLeasingServiceImplTest {
    private ItemLeasingService service;
    @Mock
    private ItemLeasingRepository repository;
    @Mock
    private ItemLeasingMapper mapper;
    @Mock
    private LeasingCartRepository leasingCartRepository;
    @Mock
    private ProductRepository productRepository;

    private ItemLeasing itemLeasing;
    private ItemLeasingRequestCreate itemLeasingRequestCreate;
    private LeasingCart leasingCart;
    private Product product;

    @BeforeEach
    void setUp() {
        service = new ItemLeasingServiceImpl(repository, mapper, leasingCartRepository, productRepository);
        User user = User.builder().id(randomUUID()).fullName("User Test").cpf("00000000000").dateOfBirth(of(2000, 1, 1)).email("example@example.com").mobile("999999999").build();
        leasingCart = LeasingCart.builder().id(randomUUID()).totalPrice(BigDecimal.ZERO).firstExpirationDate(ZonedDateTime.now()).lastExpirationDate(ZonedDateTime.now()).itemsLeasing(new TreeSet<>(comparing(item -> item.getProduct().getTitle()))).user(user).build();
        product = Product.builder().id(randomUUID()).title("Product Test").description("Product Description").quantityInStock(valueOf(10)).price(BigDecimal.valueOf(10.0)).validityNumberOfDays(10).itemsLeasing(new TreeSet<>(comparing(ItemLeasing::getCreatedAt))).build();
        itemLeasing = ItemLeasing.builder().id(randomUUID()).quantityToLeased(ONE).subtotalPrice(BigDecimal.valueOf(10.0)).expirationDate(ZonedDateTime.now()).leasingCart(leasingCart).product(product).build();
        itemLeasingRequestCreate = new ItemLeasingRequestCreate(leasingCart.getId(), product.getId(), valueOf(5));
    }

    @Test
    void itShouldCreateItemLeasingByItemLeasingRequestCreate() {
        // when
        when(leasingCartRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(leasingCart));
        when(productRepository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(product));
        when(mapper.toItemLeasing(any(ItemLeasingRequestCreate.class), any(LeasingCart.class), any(Product.class))).thenReturn(itemLeasing);
        when(repository.save(any(ItemLeasing.class))).thenReturn(itemLeasing);
        ItemLeasing createdItemLeasing = service.create(itemLeasingRequestCreate);
        // then
        verify(leasingCartRepository, times(1)).findById(any(UUID.class));
        verify(productRepository, times(1)).findById(any(UUID.class));
        verify(mapper, times(1)).toItemLeasing(any(ItemLeasingRequestCreate.class), any(LeasingCart.class), any(Product.class));
        verify(repository, times(1)).save(any(ItemLeasing.class));
        assertThat(createdItemLeasing).isNotNull();
        assertThat(createdItemLeasing.getId()).isNotNull();
        assertThat(createdItemLeasing.getQuantityToLeased()).isEqualTo(ONE);
        assertThat(createdItemLeasing.getLeasingCart()).isEqualTo(itemLeasing.getLeasingCart());
        assertThat(createdItemLeasing.getProduct()).isEqualTo(itemLeasing.getProduct());
    }
}