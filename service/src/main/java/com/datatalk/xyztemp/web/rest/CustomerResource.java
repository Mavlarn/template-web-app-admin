package com.datatalk.xyztemp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.datatalk.xyztemp.domain.Customer;
import com.datatalk.xyztemp.repository.CustomerRepository;
import com.datatalk.xyztemp.service.CustomerService;
import com.datatalk.xyztemp.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * REST controller for managing Customer.
 */
@RestController
@RequestMapping("/api/crm")
public class CustomerResource {

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CustomerService customerService;

    @Inject
    private UserService userService;

    @RequestMapping(value = "/customers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        customerService.create(customer);
        return ResponseEntity.ok(customer);
    }

    @RequestMapping(value = "/customers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) {
        customerService.update(customer);
        return ResponseEntity.ok(customer);
    }

    /**
     * GET  /users -> get all my customers.
     */
    @RequestMapping(value = "/customers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Page<Customer>> getCustomers(Pageable pageable) {
        Page<Customer> customers = customerRepository.findAll(pageable);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    /** Admin interface **/

}
