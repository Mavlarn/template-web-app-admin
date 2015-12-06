package com.datatalk.xyztemp.weixin.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.datatalk.xyztemp.domain.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WxMPMessageTemplate.
 */
@Entity
@Table(name = "WX_MP_MESSAGE_TEMPLATE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WxMPMessageTemplate extends BaseEntity {

    @NotNull
    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @NotNull
    @Column(name = "template_id", nullable = false)
    private String templateId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private WeixinTemplateType type;

    @Column(name = "description")
    private String description;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public WeixinTemplateType getType() {
        return type;
    }

    public void setType(WeixinTemplateType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "WxMPMessageTemplate{" +
                "id=" + id +
                ", serviceId='" + serviceId + "'" +
                ", templateId='" + templateId + "'" +
                ", type='" + type + "'" +
                ", description='" + description + "'" +
                '}';
    }
}
