package aba3.lucid.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "global.var")
public class GlobalConfig {

    private String baseUrl;
    private String imagePath;
    private String imageDir;

}
