package com.yizhi.monitorsystem2;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.yizhi.monitorsystem2.collection.entity.WebServerAccessLogEntity;
import com.yizhi.monitorsystem2.collection.repository.WebServerAccessLogRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitorSystem2ApplicationTests {
    @Autowired
    private WebServerAccessLogRepository webServerAccessLogRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test() {
        WebServerAccessLogEntity webServerAccessLogEntity = new WebServerAccessLogEntity();
        webServerAccessLogEntity.setTimestamp(111);
        webServerAccessLogEntity.setUrl("123");
        webServerAccessLogEntity.setStatus_code(200);
        webServerAccessLogEntity.setSource(1);
        webServerAccessLogEntity.setEnd_timestamp(222);
        webServerAccessLogEntity.setCreate_timestamp(333);

        webServerAccessLogRepository.save(webServerAccessLogEntity);

        System.out.println(webServerAccessLogRepository.findAll());
    }
}

