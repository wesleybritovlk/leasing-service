package com.github.wesleybritovlk.leasingserviceapi.user;

import com.github.wesleybritovlk.leasingserviceapi.user.address.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestEntityManager
class
UserRepositoryTest {
    private final UserRepository underTest;
    private User user;
    private User user2;

    @Autowired
    UserRepositoryTest(UserRepository underTest) {
        this.underTest = underTest;
    }

    @BeforeEach
    void setUp() {
        Address address = Address.builder().postalCode("99999-999").street("Street").houseNumber("123").complement("Complement").district("Neighborhood").city("City").state("UF").build();
        user = User.builder().fullName("User Test").cpf("00000000000").dateOfBirth(LocalDate.of(2000, 1, 1)).email("example@example.com").mobile("999999999").address(address).build();
        Address address2 = Address.builder().postalCode("99999-909").street("Street").houseNumber("123").complement("Complement").district("Neighborhood").city("City").state("UF").build();
        user2 = User.builder().fullName("Userzin").cpf("00000000001").dateOfBirth(LocalDate.of(2001, 12, 20)).email("aaaaaa@XXXXXX.XXX").mobile("988888888").address(address2).build();
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldSaveUser() {
        // when
        String expectedName = "Userzin";
        User save = underTest.save(user2);
        // then
        assertThat(save.getFullName()).isEqualTo(expectedName);
        assertThat(save.getId()).isNotNull();
        assertThat(save.getAddress()).isEqualTo(user2.getAddress());
    }

    @Test
    void itShouldFindUserById() {
        // given
        underTest.save(user);
        // when
        UUID expectedId = user.getId();
        Optional<User> expectedUser = Optional.of(user);
        Optional<User> findUser = underTest.findById(expectedId);
        // then
        assertThat(findUser.get().getId()).isEqualTo(expectedId);
        assertThat(findUser).isEqualTo(expectedUser);
        assertThat(findUser).isNotEqualTo(user);
        assertThat(findUser).isNotNull();
    }

    @Test
    void itShouldFindAllUsers() {
        // given
        underTest.save(user);
        underTest.save(user2);
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Integer expectedPageNumber = 0;
        Integer expectedPageSize = 10;
        Page<User> pageFindAll = underTest.findAll(pageable);
        // then
        assertThat(pageFindAll).isNotNull();
        assertThat(pageFindAll.getNumber()).isEqualTo(expectedPageNumber);
        assertThat(pageFindAll.getSize()).isEqualTo(expectedPageSize);
    }

    @Test
    void itShouldSearchUserByCpf() {
        // given
        underTest.save(user);
        Pageable pageable = PageRequest.of(0, 10);
        // when
        String expectedCpf = "00000000000";
        Integer expectedPageNumber = 0;
        Integer expectedPageSize = 10;
        Page<User> searchByCpf = underTest.searchByCpf(expectedCpf, pageable);
        // then
        assertThat(searchByCpf).isNotNull();
        assertThat(searchByCpf.getNumber()).isEqualTo(expectedPageNumber);
        assertThat(searchByCpf.getSize()).isEqualTo(expectedPageSize);
        assertThat(searchByCpf.getContent().get(0).getCpf()).isEqualTo(expectedCpf);
        assertThat(searchByCpf.getContent().get(0).getCpf()).isEqualTo(user.getCpf());
    }

    @Test
    void itShouldSearchUsersByNameAndDateOfBirth() {
        // given
        underTest.save(user);
        User save = underTest.save(user2);
        Pageable pageable = PageRequest.of(0, 10);
        // when
        String expectedName = "Userzin";
        LocalDate expectedDateOfBirth = LocalDate.of(2001, 12, 20);
        Integer expectedPageNumber = 0;
        Integer expectedPageSize = 10;
        Page<User> searchByNameAndDateOfBirth = underTest.searchByNameAndDateOfBirth(expectedName, expectedDateOfBirth, pageable);
        // then
        assertThat(searchByNameAndDateOfBirth).isNotNull();
        assertThat(searchByNameAndDateOfBirth.getNumber()).isEqualTo(expectedPageNumber);
        assertThat(searchByNameAndDateOfBirth.getSize()).isEqualTo(expectedPageSize);
        assertThat(searchByNameAndDateOfBirth.getContent().get(0).getFullName().contains(expectedName)).isTrue();
        assertThat(searchByNameAndDateOfBirth.getContent().get(0).getDateOfBirth()).isEqualTo(expectedDateOfBirth);
        assertThat(searchByNameAndDateOfBirth.getContent().get(0).getFullName()).isEqualTo(save.getFullName());
        assertThat(searchByNameAndDateOfBirth.getContent().get(0).getDateOfBirth()).isEqualTo(save.getDateOfBirth());
        assertThat(searchByNameAndDateOfBirth.getContent().get(0).getFullName()).isNotEqualTo(user.getFullName());
        assertThat(searchByNameAndDateOfBirth.getContent().get(0).getDateOfBirth()).isNotEqualTo(user.getDateOfBirth());
    }

    @Test
    void itShouldUpdateUser() {
        // given
        User create = underTest.save(user);
        // when
        String notExpectedName = "User";
        User update = underTest.save(User.builder().id(create.getId()).fullName("Update User").cpf("00000000000").dateOfBirth(LocalDate.of(2000, 1, 1)).email("update@XXXXXX.XXX").mobile("999999999").address(user.getAddress()).build());
        // then
        assertThat(update.getId()).isEqualTo(create.getId());
        assertThat(update.getId()).isNotNull();
        assertThat(update.getFullName()).isNotEqualTo(notExpectedName);
        assertThat(update).isNotNull();
    }

    @Test
    void itShouldDeleteUser() {
        // given
        underTest.save(user);
        // when
        underTest.delete(user);
        Optional<User> userDeleted = underTest.findById(user.getId());
        // then
        assertThat(userDeleted).isNotPresent();
        assertThat(userDeleted).isNotEqualTo(user);
    }
}