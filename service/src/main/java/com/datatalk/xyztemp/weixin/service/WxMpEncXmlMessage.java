package com.datatalk.xyztemp.weixin.service;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import me.chanjar.weixin.common.util.xml.XStreamCDataConverter;
import me.chanjar.weixin.mp.util.xml.XStreamTransformer;

/**
 * 用于获取加密的xml信息中的ToUserName.
 * Author: mavlarn
 * Date: 15/6/16
 */
@XStreamAlias("xml")
public class WxMpEncXmlMessage implements Serializable {

    @XStreamAlias("ToUserName")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String toUserName;

    @XStreamAlias("Encrypt")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String encrypt;

    public static WxMpEncXmlMessage fromEncriptedXML(String xml) {
        return XStreamTransformer.fromXml(WxMpEncXmlMessage.class, xml);
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getEncrypt() {
        return encrypt;
    }

    public void setEncrypt(String encrypt) {
        this.encrypt = encrypt;
    }
}
