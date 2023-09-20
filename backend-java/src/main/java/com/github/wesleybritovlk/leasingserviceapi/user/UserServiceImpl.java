package com.github.wesleybritovlk.leasingserviceapi.user;

import com.github.wesleybritovlk.leasingserviceapi.leasingcart.LeasingCartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final LeasingCartService leasingCartService;

    private static final Logger LOGGER = getLogger(UserService.class);

    private User findUser(UUID id) {
        var user = repository.findById(id).orElseThrow(() -> {
            var message = "User not found, please check the id";
            return new ResponseStatusException(NOT_FOUND, message);
        });
        LOGGER.info("M findUser, User={}", user);
        return user;
    }

    @Transactional
    @Override
    public User create(UserRequest creationRequest) {
        var user = mapper.toUser(creationRequest);
        LOGGER.info("M create, User={}", user);
        return repository.save(user);
    }

    public UserResponse findById(UUID id) {
        var userResponse = mapper.toResponse(findUser(id));
        LOGGER.info("M getUser, User={}", userResponse);
        return userResponse;
    }

    public Page<UserResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    public Page<UserResponse> findAllByCpf(String cpf, Pageable pageable) {
        return repository.searchByCpf(cpf, pageable).map(mapper::toResponse);
    }

    public Page<UserResponse> findAllByNameAndDateOfBirth(String name, LocalDate dateOfBirth, Pageable pageable) {
        return repository.searchByNameAndDateOfBirth(name, dateOfBirth, pageable).map(mapper::toResponse);
    }

    @Transactional
    @Override
    public User update(UUID id, UserRequest updateRequest) {
        var findUser = findUser(id);
        var user = mapper.toUser(findUser, updateRequest);
        LOGGER.info("M update, User={}", user);
        return repository.save(user);
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        var findUser = findUser(id);
        leasingCartService.delete(findUser.getLeasingCart().getId());
        LOGGER.info("M deleteLeasing, User={Leasing={}}", findUser.getLeasingCart());
        repository.delete(findUser);
        LOGGER.info("M delete, User={}", findUser);
    }
}