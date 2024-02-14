package com.github.wesleybritovlk.leasingserviceapi.util.viacep;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep", url = "viacep.com.br/ws")
public interface ViaCepService {
    @GetMapping("/{cep}/json")
    ViaCepResponse findCepById(@PathVariable("cep") String cep);
}