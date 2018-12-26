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
@ConfigurationProperties(prefix = "time")
public class TimeProperties {
    private String commonTimeFormat;

    private String hourTimeFormat;
}
