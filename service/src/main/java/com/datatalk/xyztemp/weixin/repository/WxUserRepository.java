package com.datatalk.xyztemp.weixin.repository;

import java.util.List;


import com.datatalk.xyztemp.weixin.domain.WxUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/6/24
 */
public interface WxUserRepository extends JpaRepository<WxUser, Long> {

    WxUser findOneByUseServiceIdAndOpenId(Long useServiceId, String openId);
    void deleteByUseServiceIdAndOpenId(Long useServiceId, String openId);
    long countByUseServiceId(Long useServiceId);
    Page<WxUser> findAll(Pageable pageable);
    List<WxUser> findAllByUseServiceId(Long useServiceId);
}
