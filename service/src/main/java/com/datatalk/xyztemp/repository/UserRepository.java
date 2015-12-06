package com.datatalk.xyztemp.repository;

import com.datatalk.xyztemp.domain.User;

import java.time.ZonedDateTime;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select u from User u join fetch u.authorities ua where u.login = :login")
    Optional<User> findOneDetailByLogin(@Param("login") String login);

    @Query(value = "select u from User u join fetch u.authorities ua where u.id = :userId")
    Optional<User> findOneDetailById(@Param("userId") Long id);

    Optional<User> findOneByMobile(String mobile);
    Optional<User> findOneByEmail(String email);
    Optional<User> findOneByLogin(String login);
    Optional<User> findOneByActivationKey(String activationKey);
    Optional<User> findOneByDynamicKey(String dynamicKey);
    Optional<User> findOneByUseServiceIdAndOpenId(Long useServiceId, String openId);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

    // count list sample.
//    @Query(value = "select NEW com.datatalk.firsthouse.repository.CountDto(c.managerId, count(c))" +
//        " from Customer c, User u where c.managerId = u.id and u.parentManager = :userId group by c.managerId")
//    List<CountDto> findAllCustomerCountOfMySubOrdinates(@Param("userId") Long userId);

    @Override
    void delete(User t);

}
