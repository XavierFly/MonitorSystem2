package com.yizhi.monitorsystem2.collection.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "web.server")
public class WebServerProperties {
    private int nginxSign;

    private String nginxName;

    private int tomcatSign;

    private String tomcatName;
}
