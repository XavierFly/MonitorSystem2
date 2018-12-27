package com.yizhi.monitorsystem2.collection.properties;

import com.yizhi.monitorsystem2.collection.util.TimeUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "time")
public class TimeProperties {
    private String commonTimeFormat;

    private String hourTimeFormat;

    @PostConstruct
    public void init(){
        TimeUtil.setConfigInfo(this);
    }
}
