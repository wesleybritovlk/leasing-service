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

import static java.lang.System.currentTimeMillis;
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
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found, please check the id"));
    }

    @Transactional
    @Override
    public User create(UserRequest creationRequest) {
        var startTime = currentTimeMillis();
        var user = mapper.toUser(creationRequest);
        var create = repository.save(user);
        LOGGER.info("DB Create : Persisted user ID: {} in {}ms", create.getId(), currentTimeMillis() - startTime);
        return create;
    }

    public UserResponse findById(UUID id) {
        var startTime = currentTimeMillis();
        var findById = mapper.toResponse(findUser(id));
        LOGGER.info("DB FindById : Returned user ID: {} in {}ms", findById.id(), currentTimeMillis() - startTime);
        return findById;
    }

    public Page<UserResponse> findAll(Pageable pageable) {
        var startTime = currentTimeMillis();
        var findAll = repository.findAll(pageable).map(mapper::toResponse);
        LOGGER.info("DB FindAll : Returned {} users in {}ms", findAll.getTotalElements(), currentTimeMillis() - startTime);
        return findAll;
    }

    public Page<UserResponse> findAllByCpf(String cpf, Pageable pageable) {
        var startTime = currentTimeMillis();
        var findAllByCpf = repository.searchByCpf(cpf, pageable).map(mapper::toResponse);
        LOGGER.info("DB FindAllByCpf : Returned {} users in {}ms", findAllByCpf.getTotalElements(), currentTimeMillis() - startTime);
        return findAllByCpf;
    }

    public Page<UserResponse> findAllByNameAndDateOfBirth(String name, LocalDate dateOfBirth, Pageable pageable) {
        var startTime = currentTimeMillis();
        var findAllByNameAndDateOfBirth = repository.searchByNameAndDateOfBirth(name, dateOfBirth, pageable).map(mapper::toResponse);
        LOGGER.info("DB FindAllByNameAndDateOfBirth : Returned {} users in {}ms", findAllByNameAndDateOfBirth.getTotalElements(), currentTimeMillis() - startTime);
        return findAllByNameAndDateOfBirth;
    }

    @Transactional
    @Override
    public User update(UUID id, UserRequest updateRequest) {
        var startTime = currentTimeMillis();
        var findUser = findUser(id);
        var user = mapper.toUser(findUser, updateRequest);
        var update = repository.save(user);
        LOGGER.info("DB Update : Updated user ID: {} in {}ms", update.getId(), currentTimeMillis() - startTime);
        return update;
    }

    @Transactional
    @Override
    public void delete(UUID id) {
        var startTime = currentTimeMillis();
        var user = findUser(id);
        leasingCartService.delete(user.getLeasingCart().getId());
        LOGGER.info("DB Delete : Deleted leasing_cart ID: {} in {}ms", user.getLeasingCart().getId(), currentTimeMillis() - startTime);
        repository.delete(user);
        LOGGER.info("DB Delete : Deleted user ID: {} in {}ms", user.getId(), currentTimeMillis() - startTime);
    }
}