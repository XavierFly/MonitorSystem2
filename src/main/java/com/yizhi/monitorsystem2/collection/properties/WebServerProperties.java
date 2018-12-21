package com.yizhi.monitorsystem2.collection.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
@Component
public class WebServerProperties {
    private int nginxSign;

    private String nginxName;

    private int tomcatSign;

    private String tomcatName;
}
