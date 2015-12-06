package com.datatalk.xyztemp.weixin.repository;


import com.datatalk.xyztemp.weixin.domain.WeixinTemplateType;
import com.datatalk.xyztemp.weixin.domain.WxMPMessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the WxMPMessageTemplate entity.
 */
public interface WxMPMessageTemplateRepository extends JpaRepository<WxMPMessageTemplate, Long> {

    WxMPMessageTemplate findOneByServiceIdAndType(Long serviceId, WeixinTemplateType type);
}
