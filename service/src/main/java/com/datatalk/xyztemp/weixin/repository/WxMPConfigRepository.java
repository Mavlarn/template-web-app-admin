package com.datatalk.xyztemp.weixin.repository;


import com.datatalk.xyztemp.weixin.domain.WxMPConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/6/12
 */
public interface WxMPConfigRepository extends JpaRepository<WxMPConfig, Long> {

    WxMPConfig findOneByUseOpenId(String useOpenId);
    WxMPConfig findOneByDefaultServiceTrue();
    List<WxMPConfig> findAllByName(String name);

}
