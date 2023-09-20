package com.github.wesleybritovlk.leasingserviceapi.user.address;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestEntityManager
class AddressRepositoryTest {
    private final AddressRepository underTest;
    private Address address;
    private Address address2;

    @Autowired
    AddressRepositoryTest(AddressRepository underTest) {
        this.underTest = underTest;
    }

    @BeforeEach
    void setUp() {
        address = Address.builder().postalCode("99999-999").street("Street").houseNumber("123").complement("Complement").district("Neighborhood").city("City").state("UF").build();
        address2 = Address.builder().postalCode("99999-999").street("Rua dos bobos").houseNumber("Update123").complement("UpdateComp0lement").district("UpdateNeighborhood").city("Update City").state("UF").build();
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldSaveAddress() {
        // when
        String exceptedStreet = "Rua dos bobos";
        Address save = underTest.save(address2);
        // then
        assertThat(save.getStreet()).isEqualTo(exceptedStreet);
        assertThat(save.getPostalCode()).isNotEmpty();
        assertThat(save.getStreet()).isNotNull();
    }

    @Test
    void itShouldFindAddressById() {
        // given
        underTest.save(address);
        // when
        String expectedZip = address.getPostalCode();
        Optional<Address> expectedAddress = Optional.of(address);
        Optional<Address> findAddress = underTest.findById(expectedZip);
        // then
        assertThat(findAddress).isEqualTo(expectedAddress);
        assertThat(findAddress.get().getPostalCode()).isEqualTo(expectedZip);
        assertThat(findAddress).isNotEqualTo(address);
        assertThat(findAddress).isNotNull();
    }

    @Test
    void itShouldUpdateAddress() {
        // given
        Address create = underTest.save(address);
        // when
        String notExpectedStreet = "Street";
        Address update = underTest.save(address2);
        // then
        assertThat(update.getPostalCode()).isEqualTo(create.getPostalCode());
        assertThat(update.getPostalCode()).isNotNull();
        assertThat(update.getStreet()).isNotEqualTo(notExpectedStreet);
        assertThat(update).isNotNull();
    }

    @Test
    void itShouldDeleteAddress() {
        // given
        underTest.save(address);
        // when
        underTest.delete(address);
        Optional<Address> addressDeleted = underTest.findById(address.getPostalCode());
        // then
        assertThat(addressDeleted).isNotPresent();
        assertThat(addressDeleted).isNotEqualTo(address);
    }
}