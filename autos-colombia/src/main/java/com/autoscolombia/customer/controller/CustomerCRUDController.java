package com.autoscolombia.customer.controller;

import com.autoscolombia.customer.controller.dto.CustomerCreationApi;
import com.autoscolombia.customer.controller.dto.CustomerViewApi;
import com.autoscolombia.customer.controller.mapper.CustomerApiMapper;
import com.autoscolombia.customer.service.CustomerDomainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("customer")
@RequiredArgsConstructor
public class CustomerCRUDController {

    private final CustomerDomainService customerDomainService;
    private final CustomerApiMapper apiMapper;

    @PostMapping
    public CustomerViewApi create(@Valid @RequestBody CustomerCreationApi creationApi) {
        return apiMapper.mapToViewApi(customerDomainService.create(apiMapper.mapToDomain(creationApi)));
    }

    @PutMapping("/{id}")
    public CustomerViewApi update(@PathVariable Long id, @Valid @RequestBody CustomerCreationApi creationApi) {
        return apiMapper.mapToViewApi(customerDomainService.update(id, apiMapper.mapToDomain(creationApi)));
    }

    @GetMapping
    public Page<CustomerViewApi> search(@RequestParam(required = false) String search,
                                        @PageableDefault(size = 20) Pageable pageable) {
        return customerDomainService.search(search, pageable).map(apiMapper::mapToViewApi);
    }

    @GetMapping("/{id}")
    public CustomerViewApi getById(@PathVariable Long id) {
        return apiMapper.mapToViewApi(customerDomainService.getById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        customerDomainService.delete(id);
    }
}
