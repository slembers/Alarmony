package com.slembers.alarmony.global.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;




@NoArgsConstructor(access = AccessLevel.PRIVATE)
public  final class AuthConstants {
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "Bearer";

    public static final  String ACCESS_TOKEN = "AccessToken";
    public static final  String REFRESH_TOKEN = "RefreshToken";

    public static final String REDIS_REFRESH_TOKEN_HEADER = "Refresh:";

}
