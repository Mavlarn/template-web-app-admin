package com.datatalk.xyztemp.service;

import javax.inject.Inject;

import com.codahale.metrics.annotation.Timed;
//import com.montnets.mwgate.common.ISms;
//import com.montnets.mwgate.common.StaticValue;
//import com.montnets.mwgate.common.ValidateParamTool;
//import com.montnets.mwgate.httppost.CHttpPost;
//import org.apache.http.client.HttpClient;
//import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Class description.
 * Author: mavlarn
 * Date: 15/4/29
 */
@Service
public class SMSService {

    private static final Logger LOG = LoggerFactory.getLogger(SMSService.class);

    @Inject
    private Environment environment;

    private boolean isSend = false;

    public void setIsSend(boolean isSend) {
        this.isSend = isSend;
    }

    public boolean isSend() {
        return isSend;
    }

    @Timed
    public void sendSms(String content, String phones) {

//        String strUserId = environment.getProperty("app.sms.userId");
//        String strPwd = environment.getProperty("app.sms.password");
//        StaticValue.ip = environment.getProperty("app.sms.serverAddress");;
//        StaticValue.port = environment.getProperty("app.sms.serverPort");
//        String strUserMsgId = "0"; // 用户定义流水号
//        String strSubPort = "*"; // 扩展子号
//        boolean bKeepAlive = false;
//
//        HttpClient httpClient = new DefaultHttpClient();
//        ISms sms = new CHttpPost();
//        StringBuffer strPtMsgId = new StringBuffer("");
//        if (!ValidateParamTool.validateMessage(content)) {
//            LOG.error("Invalid content.");
//            return;
//        }
//        //短信息发送接口（相同内容群发，可自定义流水号）POST请求,httpClient为空，则是短连接,httpClient不为空，则是长连接。
//
//        if (isSend) {
//            int result = sms.SendSms(strPtMsgId, strUserId, strPwd, phones, content, strSubPort, strUserMsgId,
//                    bKeepAlive, httpClient);
//            if (result == 0) {
//                System.out.println("发送成功：" + strPtMsgId.toString());
//            } else {
//                System.out.println("发送失败：" + strPtMsgId.toString());
//            }
//        }


    }
}
