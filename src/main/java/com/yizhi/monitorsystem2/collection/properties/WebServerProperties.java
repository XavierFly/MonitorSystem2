package com.yizhi.monitorsystem2.collection.properties;

import org.springframework.stereotype.Component;

@Component
public class WebServerProperties {
    private int nginxSign;

    private String nginxName;

    private int tomcatSign;

    private String tomcatName;

    public int getNginxSign() {
        return nginxSign;
    }

    public void setNginxSign(int nginxSign) {
        this.nginxSign = nginxSign;
    }

    public String getNginxName() {
        return nginxName;
    }

    public void setNginxName(String nginxName) {
        this.nginxName = nginxName;
    }

    public int getTomcatSign() {
        return tomcatSign;
    }

    public void setTomcatSign(int tomcatSign) {
        this.tomcatSign = tomcatSign;
    }

    public String getTomcatName() {
        return tomcatName;
    }

    public void setTomcatName(String tomcatName) {
        this.tomcatName = tomcatName;
    }
}
