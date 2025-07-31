package ro.mdx.meditation.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtConfigProperties {
    private String secretKey;
    private long expirationTime;
}
