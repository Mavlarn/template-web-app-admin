package com.datatalk.xyztemp.weixin.repository;


import com.datatalk.xyztemp.weixin.domain.WxServiceMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the WeixinMenu entity.
 */
public interface WxMenuRepository extends JpaRepository<WxServiceMenu, Long> {

    WxServiceMenu findOneByKeyCodeAndServiceId(String keyCode, Long serviceId);

    WxServiceMenu findOneById(Long serviceId);
    List<WxServiceMenu> findAllByServiceId(Long serviceId);
    void deleteAllByServiceId(Long serviceId);
}
