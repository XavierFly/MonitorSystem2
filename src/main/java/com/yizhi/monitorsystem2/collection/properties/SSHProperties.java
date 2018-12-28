package com.yizhi.monitorsystem2.collection.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.yizhi.monitorsystem2.collection.util.SSHUtil;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "ssh")
public class SSHProperties {
    private String strictHostKeyChecking;

    private String openChannel;

    private String charset;

    @PostConstruct
    public void init(){
        SSHUtil.setSSHUtilProperties(this);
    }
}
