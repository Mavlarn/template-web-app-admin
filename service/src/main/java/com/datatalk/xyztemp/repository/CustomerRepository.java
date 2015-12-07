package com.datatalk.xyztemp.repository;

import com.datatalk.xyztemp.domain.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Customer entity.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findAllByType(String type, Pageable pageable);
    long countByType(String type);

    Customer findOneByMobile(String mobile);
}
