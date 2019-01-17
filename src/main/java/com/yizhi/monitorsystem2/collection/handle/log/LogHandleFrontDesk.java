package com.yizhi.monitorsystem2.collection.handle.log;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.yizhi.monitorsystem2.collection.util.SSHUtil;
import com.yizhi.monitorsystem2.collection.entity.ServerEntity;
import com.yizhi.monitorsystem2.collection.entity.ServerTypeEntity;
import com.yizhi.monitorsystem2.collection.repository.ServerRepository;
import com.yizhi.monitorsystem2.collection.repository.ServerTypeRepository;

@Service
public class LogHandleFrontDesk {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private ServerTypeRepository serverTypeRepository;

    private ServerEntity serverEntity;

    public void setServerEntity(String serverId) {
        Optional<ServerEntity> serverEntityOptional = serverRepository.findById(serverId);
        serverEntityOptional.ifPresent(serverEntity -> this.serverEntity = serverEntity);
    }

    public void handle() {
        if (serverEntity == null) {
            return;
        }

        SSHUtil sshUtil = new SSHUtil(serverEntity);

        int[] serverTypes = serverEntity.getTypes();

        for (int currentServerType : serverTypes) {
            ServerTypeEntity serverTypeEntity = serverTypeRepository.findByTypesContains(currentServerType);
            String classPath = this.getClass().getPackage().getName() + "." + serverTypeEntity.getClassName();

            try {
                Class<?> classObject = Class.forName(classPath);
                LogAbstractHandle logAbstractHandle = (LogAbstractHandle) classObject.newInstance();
                logAbstractHandle.setSshUtil(sshUtil);
                logAbstractHandle.setCurrentServerType(currentServerType);
                logAbstractHandle.handle();
            } catch (Exception e) {
                logger.error(e.toString(), e);
            }
        }

        sshUtil.close();
    }
}
