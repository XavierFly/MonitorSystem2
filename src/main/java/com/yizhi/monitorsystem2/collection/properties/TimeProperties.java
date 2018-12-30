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
@ConfigurationProperties(prefix = "time")
public class TimeProperties {
    private String commonTimeFormat;

    private String hourTimeFormat;

    private String midNight;

    private String nginxLogTimeFormat;

    private String nginxLogTimeSimpleFormat;

    private String nginxLogTimeFormatOnHour;

    private String tomcatLogTimeFormat;

    private String tomcatLogTimeSimpleFormat;

    private String tomcatLogTimeFormatOnHour;

    @PostConstruct
    public void init(){
        TimeAndLogUtil.setTimeUtilProperties(this);
    }
}
