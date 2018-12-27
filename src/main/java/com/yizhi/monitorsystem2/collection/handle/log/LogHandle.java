package com.yizhi.monitorsystem2.collection.handle.log;

import java.util.Optional;

import com.yizhi.monitorsystem2.collection.util.SSHUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yizhi.monitorsystem2.collection.entity.ServerEntity;
import com.yizhi.monitorsystem2.collection.repository.ServerRepository;

public class LogHandle {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ServerRepository serverRepository;

    private ServerEntity serverEntity;

    protected LogHandle(String serverId) {
        Optional<ServerEntity> serverEntityOptional = serverRepository.findById(serverId);
        serverEntityOptional.ifPresent(serverEntity -> this.serverEntity = serverEntity);
    }

    private void handle() {
        SSHUtil sshUtil = new SSHUtil(serverEntity);

        int[] serverTypes = serverEntity.getTypes();

        for (int currentServerType : serverTypes) {
            // 暂时只有 web access 日志，之后再增加相应的分析类。
            // 暂时使用 switch，之后改为工厂模式。
            switch (currentServerType) {
                case 1:
                case 2:
                    new WebServerAccessLogHandle(sshUtil, currentServerType).handle();
                    break;
            }
        }

        sshUtil.close();
    }
}
