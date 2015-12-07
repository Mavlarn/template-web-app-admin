package com.datatalk.xyztemp.service;

import com.datatalk.xyztemp.domain.Authority;
import com.datatalk.xyztemp.domain.User;
import com.datatalk.xyztemp.repository.AuthorityRepository;
import com.datatalk.xyztemp.repository.UserRepository;
import com.datatalk.xyztemp.security.AuthoritiesConstants;
import com.datatalk.xyztemp.security.SecurityUtils;
import com.datatalk.xyztemp.service.util.RandomUtil;
import com.datatalk.xyztemp.web.rest.dto.UserDTO;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
        return Optional.empty();
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByDynamicKey(key)
            .filter(user -> {
                ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
                return user.getDynamicKeyDate().isAfter(oneDayAgo);
           })
           .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setDynamicKey(null);
                user.setDynamicKeyDate(null);
                userRepository.save(user);
                return user;
           });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setDynamicKey(RandomUtil.generateResetKey());
                user.setDynamicKeyDate(ZonedDateTime.now());
                userRepository.save(user);
                return user;
            });
    }

    public User createUser(UserDTO userDTO) {
        User newUser = userDTO.toUser();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());

        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(generateUniqueKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        newUser.setCreatedBy(SecurityUtils.getCurrentUserId());
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public void updateUserInformation(UserDTO userDTO) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            u.setName(userDTO.getName());
            u.setRealName(userDTO.getRealName());
            u.setEmail(userDTO.getEmail());
            u.setMobile(userDTO.getMobile());
            userRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }

    public void deleteUser(Long id) {
        Optional.ofNullable(userRepository.findOne(id)).ifPresent(u -> {
            u.setDeleted(true);
            userRepository.save(u);
            log.debug("Deleted User: {}", u);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for User: {}", u);
        });
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneDetailByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneDetailById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return getUserWithAuthorities(SecurityUtils.getCurrentUserId());
    }
    public void clearWxInfo(User user) {
        user.setOpenId(null);
        user.setUseServiceId(null);
    }

    public String getUserPicFileName(User user, String fileName) {
        int idx = fileName.lastIndexOf("?");
        if (idx > 0) {
            fileName = fileName.substring(0, idx);
        }
        String pre = "head-";
        fileName = pre + user.getLogin() + "-" + fileName;
        return fileName;
    }

    public String generateUniqueKey() {
        String activationKey = RandomUtil.generateActivationKey();
        int retry = 0;
        Optional<User> userWithKey = userRepository.findOneByActivationKey(activationKey);
        while (userWithKey.isPresent() && retry < 3) {
            log.info("Generated key exist, regenerate...");
            activationKey = RandomUtil.generateActivationKey();
            userWithKey = userRepository.findOneByActivationKey(activationKey);
            retry++;
        }
        Preconditions.checkState(!userWithKey.isPresent(), "Generated activation key 3 times and duplicated.");
        return activationKey;
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        ZonedDateTime now = ZonedDateTime.now();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
        }
    }
}
