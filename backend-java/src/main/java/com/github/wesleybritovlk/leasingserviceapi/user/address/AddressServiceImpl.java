package com.github.wesleybritovlk.leasingserviceapi.user.address;

import com.github.wesleybritovlk.leasingserviceapi.util.viacep.ViaCepService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static com.github.wesleybritovlk.leasingserviceapi.user.address.AddressPolitic.*;
import static java.lang.System.currentTimeMillis;
import static org.slf4j.LoggerFactory.getLogger;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository repository;
    private final AddressMapper mapper;
    private final ViaCepService cepService;

    private static final Logger LOGGER = getLogger(AddressService.class);

    public Address create(AddressRequest requestCreate) {
        var startTime = currentTimeMillis();
        var cepResponse = cepService.findCepById(requestCreate.postalCode());
        validateCepService(requestCreate.street(), cepResponse.cep());
        var address = mapper.toAddress(requestCreate, cepResponse);
        var createdAddress = repository.save(address);
        LOGGER.info("DB Create : Persisted address postal_code: {} in {}ms", createdAddress.getPostalCode(), currentTimeMillis() - startTime);
        return createdAddress;
    }

    public Address update(Address findAddress, AddressRequest requestCreate) {
        var startTime = currentTimeMillis();
        var cepResponse = cepService.findCepById(requestCreate.postalCode());
        validateCepService(requestCreate.street(), cepResponse.cep());
        LOGGER.info("DB FindByAddress : Returned address postal_code: {} in {}ms", findAddress.getPostalCode(), currentTimeMillis() - startTime);
        var address = mapper.toAddress(findAddress, requestCreate, cepResponse);
        var updatedAddress = repository.save(address);
        LOGGER.info("DB Update : Updated address postal_code: {} in {}ms", updatedAddress.getPostalCode(), currentTimeMillis() - startTime);
        return updatedAddress;
    }

    public AddressResponse findByAddress(Address address) {
        var startTime = currentTimeMillis();
        var response = mapper.toResponse(address);
        LOGGER.info("DB FindByAddress : Returned address postal_code: {} in {}ms", response.postalCode(), currentTimeMillis() - startTime);
        return response;
    }
}