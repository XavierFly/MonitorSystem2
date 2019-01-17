package com.yizhi.monitorsystem2;

import com.yizhi.monitorsystem2.collection.entity.ServerEntity;
import com.yizhi.monitorsystem2.collection.entity.ServerTypeEntity;
import com.yizhi.monitorsystem2.collection.exception.BaseException;
import com.yizhi.monitorsystem2.collection.handle.log.LogHandleFrontDesk;
import com.yizhi.monitorsystem2.collection.properties.LogProperties;
import com.yizhi.monitorsystem2.collection.properties.SSHProperties;
import com.yizhi.monitorsystem2.collection.repository.ServerRepository;
import com.yizhi.monitorsystem2.collection.repository.ServerTypeRepository;
import com.yizhi.monitorsystem2.collection.util.SSHUtil;
import com.yizhi.monitorsystem2.collection.util.TimeAndLogUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitorSystem2ApplicationTests {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private ServerTypeRepository serverTypeRepository;

    @Autowired
    private SSHProperties sshProperties;

    @Autowired
    private LogProperties logProperties;

    @Autowired
    private LogHandleFrontDesk logHandleFrontDesk;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test1() {
        try {
            System.out.println(TimeAndLogUtil.getLogFilePath(2));
        } catch (BaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        System.out.println(this.getClass().getPackage().getName());
    }

    @Test
    public void test3() {
        ServerTypeEntity serverTypeEntity = new ServerTypeEntity();
        serverTypeEntity.setClassName("WebServerAccessLog");
        serverTypeEntity.setTypes(new int[]{1, 2});
        serverTypeRepository.save(serverTypeEntity);
    }

    @Test
    public void test4() {
        System.out.println(serverTypeRepository.findByTypesContains(3));
    }

    @Test
    public void test5() {
        // ServerEntity serverEntity = new ServerEntity();
        // serverEntity.setHost("192.168.36.133");
        // serverEntity.setPort(22);
        // serverEntity.setUser("xavierw");
        // serverEntity.setPassword("BePatient1991");
        // serverEntity.setTypes(new int[]{2});
        // serverRepository.save(serverEntity);

        System.out.println(serverRepository.findAll());
    }

    @Test
    public void test6() {
        List<ServerEntity> serverEntityList = serverRepository.findAll();
        SSHUtil sshUtil = new SSHUtil(serverEntityList.get(0));
        String logFilePath;
        try {
            logFilePath = TimeAndLogUtil.getLogFilePath(2);
        } catch (BaseException e) {
            return;
        }
        BufferedReader bufferedReader = sshUtil.readFile(logFilePath, 0);
        try {
            System.out.println(bufferedReader.readLine());
        } catch (IOException e) {
            logger.error(e.toString(), e);
        }
    }

    @Test
    public void test7() {
        System.out.println(sshProperties.getStrictHostKeyChecking());
        System.out.println(sshProperties.getCharset());
        System.out.println(logProperties.getNginxLogSeparator());
        System.out.println(logProperties.getTomcatLogSeparator());
    }

    @Test
    public void test8() {
        logHandleFrontDesk.setServerEntity("5c27166ac757d327f84276b7");
        logHandleFrontDesk.handle();
    }
}

