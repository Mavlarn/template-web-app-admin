package com.datatalk.xyztemp.service;

import com.datatalk.xyztemp.domain.Customer;
import com.datatalk.xyztemp.repository.CustomerRepository;
import com.datatalk.xyztemp.repository.UserRepository;
import com.datatalk.xyztemp.service.util.ServiceConditionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.ZonedDateTime;

/**
 * Created by mavlarn on 15/11/18.
 */
@Service
public class CustomerService {

    @Inject
    private CustomerRepository customerRepository;
    @Inject
    private PasswordEncoder passwordEncoder;
    @Inject
    private UserService userService;
    @Inject
    private UserRepository userRepository;

    public Customer create(Customer customer) {
        checkCustomer(customer);
        ZonedDateTime now = ZonedDateTime.now();
        customer.setLogin("c" + now.toEpochSecond());
        customer.setCreatedDate(now);
        // new customer has no password
        customer.setPassword(null);
        // new customer is not active, can not login
        customer.setActivated(false);
        return customerRepository.save(customer);
    }

    private void checkCustomer(Customer customer) {
        ServiceConditionUtils.checkState(StringUtils.isNoneBlank(customer.getRealName()), "客户姓名不能为空");
        ServiceConditionUtils.checkState(StringUtils.isNoneBlank(customer.getMobile()), "客户电话不能为空");
        Customer customerWithMobile = customerRepository.findOneByMobile(customer.getMobile());
        if (customer.getId() == null) {
            ServiceConditionUtils.checkState(customerWithMobile == null, "已存在该手机号的客户");
        } else {
            ServiceConditionUtils.checkState(customerWithMobile.getId().equals(customer.getId()),
                "已存在该手机号的其他客户");
        }
    }

    public Customer update(Customer customer) {
        checkCustomer(customer);
        Customer customerInDB = customerRepository.findOne(customer.getId());
        customerInDB.setRealName(customer.getRealName());
        customerInDB.setMobile(customer.getMobile());
        customerInDB.setEmail(customer.getEmail());
        customerInDB.setDescription(customer.getDescription());
        customerInDB.setType(customer.getType());
        customerInDB.setAddress(customer.getAddress());
        return customerInDB;
    }
}
