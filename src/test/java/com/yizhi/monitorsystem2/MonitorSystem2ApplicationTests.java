package com.yizhi.monitorsystem2;

import com.yizhi.monitorsystem2.collection.handle.log.WebServerAccessLogHandle;
import com.yizhi.monitorsystem2.collection.properties.TimeProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MonitorSystem2ApplicationTests {
    @Autowired
    private TimeProperties timeProperties;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test() {
        WebServerAccessLogHandle webServerAccessLogHandle = new WebServerAccessLogHandle();

        String filepath = "F:\\access.log";
        try {
            BufferedReader read = new BufferedReader(new FileReader(filepath));
            String line;
            while ((line = read.readLine()) != null) {
                webServerAccessLogHandle.parseCurrentLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

