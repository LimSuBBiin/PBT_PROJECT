package org.execute.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private String issuer;
    private int expiration;
    public String getSecret() {
        return secret;
    }

    public int getExpiration() {
        return expiration;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }
}
