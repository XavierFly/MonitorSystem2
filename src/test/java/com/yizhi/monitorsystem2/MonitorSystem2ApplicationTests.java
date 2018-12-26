package com.yizhi.monitorsystem2;

import com.yizhi.monitorsystem2.collection.properties.SSHProperties;
import com.yizhi.monitorsystem2.collection.properties.TimeProperties;
import com.yizhi.monitorsystem2.collection.properties.WebServerProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitorSystem2ApplicationTests {
    @Autowired
    private static TimeProperties timeProperties;

    @Autowired
    private WebServerProperties webServerProperties;

    @Autowired
    private SSHProperties sshProperties;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test() {
    }
}

