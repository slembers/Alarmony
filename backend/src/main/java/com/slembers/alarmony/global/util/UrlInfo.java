package com.slembers.alarmony.global.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UrlInfo implements InitializingBean {

    private final String fcmApi;

    private final String cloudPlatformUrl;

    public UrlInfo(
            @Value("${url.fcm-message-send-url}") String fcmApi,
            @Value("${url.cloud-platform-url}") String cloudPlatformUrl
    ) {
        this.fcmApi = fcmApi;
        this.cloudPlatformUrl = cloudPlatformUrl;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * fcm 전송용 API URL
     * @return
     */
    public String getFcmApi() {
        return this.fcmApi;
    }

    /**
     * google message service 세팅을 위한 URL
     * @return
     */
    public String getCloudPlatformUrl() { return this.cloudPlatformUrl; }
}
