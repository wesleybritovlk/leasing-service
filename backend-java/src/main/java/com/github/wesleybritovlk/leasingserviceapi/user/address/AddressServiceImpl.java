package com.github.wesleybritovlk.leasingserviceapi.user.address;

import com.github.wesleybritovlk.leasingserviceapi.util.viacep.ViaCepService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static com.github.wesleybritovlk.leasingserviceapi.user.address.AddressPolitic.*;
import static org.slf4j.LoggerFactory.getLogger;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository repository;
    private final AddressMapper mapper;
    private final ViaCepService cepService;

    private static final Logger LOGGER = getLogger(AddressService.class);

    public Address create(AddressRequest requestCreate) {
        var cepResponse = cepService.findCepById(requestCreate.postalCode());
        validateCepService(requestCreate.street(), cepResponse.cep());
        var address = mapper.toAddress(requestCreate, cepResponse);
        LOGGER.info("M create, Address={}", address);
        return repository.save(address);
    }

    public Address update(Address findAddress, AddressRequest requestCreate) {
        var cepResponse = cepService.findCepById(requestCreate.postalCode());
        validateCepService(requestCreate.street(), cepResponse.cep());
        LOGGER.info("M findAddress, Address={}", findAddress);
        var address = mapper.toAddress(findAddress, requestCreate, cepResponse);
        LOGGER.info("M update, Address={}", address);
        return repository.save(address);
    }

    public AddressResponse findByAddress(Address address) {
        var addressResponse = mapper.toResponse(address);
        LOGGER.info("M getAddress, Address={}", addressResponse);
        return addressResponse;
    }
}