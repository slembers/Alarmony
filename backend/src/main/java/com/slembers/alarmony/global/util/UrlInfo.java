package com.slembers.alarmony.global.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlInfo implements InitializingBean {

    private final String fcmApi;

    public UrlInfo(
            @Value("${url.fcm-message-send-url}") String fcmApi
    ) {
        this.fcmApi = fcmApi;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public String getFcmApi() {
        return this.fcmApi;
    }
}
