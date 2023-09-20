package com.github.wesleybritovlk.leasingserviceapi.leasingcart.itemleasing;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCart;
import com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCartRepository;
import com.github.wesleybritovlk.leasingserviceapi.product.Product;
import com.github.wesleybritovlk.leasingserviceapi.product.ProductRepository;
import com.github.wesleybritovlk.leasingserviceapi.user.User;
import com.github.wesleybritovlk.leasingserviceapi.user.UserRepository;
import com.github.wesleybritovlk.leasingserviceapi.user.address.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.TreeSet;
import java.util.UUID;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.valueOf;
import static java.util.Comparator.comparing;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestEntityManager
class ItemLeasingRepositoryTest {
    ItemLeasingRepository underTest;
    UserRepository userRepository;
    LeasingCartRepository leasingCartRepository;
    ProductRepository productRepository;
    ItemLeasing itemLeasing;

    @Autowired
    ItemLeasingRepositoryTest(ItemLeasingRepository underTest, UserRepository userRepository, LeasingCartRepository leasingCartRepository, ProductRepository productRepository) {
        this.underTest = underTest;
        this.userRepository = userRepository;
        this.leasingCartRepository = leasingCartRepository;
        this.productRepository = productRepository;
    }

    @BeforeEach
    void setUp() {
        Address address = Address.builder().postalCode("99999-999").street("Street").houseNumber("123").complement("Complement").district("Neighborhood").city("City").state("UF").build();
        User user = User.builder().fullName("User Test").cpf("00000000000").dateOfBirth(LocalDate.of(2000, 1, 1)).email("example@example.com").mobile("999999999").address(address).build();
        LeasingCart leasingCart = LeasingCart.builder().totalPrice(BigDecimal.ZERO).firstExpirationDate(ZonedDateTime.now()).lastExpirationDate(ZonedDateTime.now()).itemsLeasing(new TreeSet<>(comparing(item -> item.getProduct().getTitle()))).user(user).build();
        Product product = Product.builder().title("Product Test").description("Product Description").quantityInStock(valueOf(10)).price(BigDecimal.valueOf(10.0)).validityNumberOfDays(10).itemsLeasing(new TreeSet<>(comparing(ItemLeasing::getCreatedAt))).build();
        userRepository.save(user);
        leasingCartRepository.save(leasingCart);
        productRepository.save(product);
        itemLeasing = ItemLeasing.builder().quantityToLeased(ONE).subtotalPrice(BigDecimal.valueOf(10.0)).expirationDate(ZonedDateTime.now()).leasingCart(leasingCart).product(product).build();
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldSaveItemLeasing() {
        // when
        ItemLeasing save = underTest.save(itemLeasing);
        // then
        assertThat(save.getQuantityToLeased()).isEqualTo(ONE);
        assertThat(save.getLeasingCart()).isEqualTo(itemLeasing.getLeasingCart());
        assertThat(save.getProduct()).isEqualTo(itemLeasing.getProduct());
    }

    @Test
    void itShouldFindItemLeasingById() {
        // given
        underTest.save(itemLeasing);
        // when
        UUID expectedId = itemLeasing.getId();
        Optional<ItemLeasing> expectedItemLeasing = Optional.of(itemLeasing);
        Optional<ItemLeasing> findItemLeasing = underTest.findById(expectedId);
        // then
        assertThat(findItemLeasing.get().getId()).isEqualTo(expectedId);
        assertThat(findItemLeasing).isEqualTo(expectedItemLeasing);
        assertThat(findItemLeasing.get().getQuantityToLeased()).isEqualTo(ONE);
        assertThat(findItemLeasing.get().getLeasingCart()).isEqualTo(expectedItemLeasing.get().getLeasingCart());
        assertThat(findItemLeasing.get().getProduct()).isEqualTo(expectedItemLeasing.get().getProduct());
        assertThat(findItemLeasing).isNotNull();
    }
}