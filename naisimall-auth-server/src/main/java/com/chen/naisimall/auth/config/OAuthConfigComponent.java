package com.chen.naisimall.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "oauth.config")
@Component
@Data
public class OAuthConfigComponent {

    private String grant_type;
    private String client_id;
    private String redirect_uri;
    private String client_secret;

}
