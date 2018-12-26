package com.yizhi.monitorsystem2.collection.util;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.springframework.beans.factory.annotation.Autowired;

import com.yizhi.monitorsystem2.collection.entity.ServerEntity;
import com.yizhi.monitorsystem2.collection.properties.SSHProperties;

public class SSHUtil {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SSHProperties sshProperties;

    private Session session;
    private Channel channel;
    private BufferedReader bufferedReader;

    public SSHUtil(ServerEntity serverEntity) {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(serverEntity.getUser(), serverEntity.getHost(), serverEntity.getPort());

            if (serverEntity.getPassword() != null && serverEntity.getPassword().length() > 0) {
                session.setPassword(serverEntity.getPassword());
            }

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", sshProperties.getStrictHostKeyChecking());
            session.setConfig(config);

            session.connect();
        } catch (JSchException e) {
            logger.error(e.getMessage(), e);
            close();
        }
    }

    public BufferedReader readFile(String filePath, int row) {
        try {
            channel = session.openChannel(sshProperties.getOpenChannel());
            ((ChannelExec) channel).setCommand("cat " + filePath + " | tail -n +" + row);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            channel.connect();
            InputStream inputStream = channel.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName(sshProperties.getCharset())));
            return bufferedReader;
        } catch (JSchException | IOException e) {
            logger.error(e.getMessage(), e);
            close();
            return null;
        }
    }

    public void close() {
        if (session != null) {
            session.disconnect();
        }

        if (channel != null) {
            channel.disconnect();
        }

        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
