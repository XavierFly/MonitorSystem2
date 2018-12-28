package com.yizhi.monitorsystem2.collection.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.yizhi.monitorsystem2.collection.util.TimeAndLogUtil;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "log")
public class LogProperties {
    private String nginxLogFormat;

    private String nginxLogPath;

    private String nginxLogSeparator;

    private String tomcatLogPath;

    private String tomcatLogFormat;

    private String tomcatLogSeparator;

    @PostConstruct
    public void init(){
        TimeAndLogUtil.setTimeUtilProperties(this);
    }
}
