package com.yizhi.monitorsystem2.collection.handle.log;

import java.util.Optional;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yizhi.monitorsystem2.collection.util.SSHUtil;
import com.yizhi.monitorsystem2.collection.entity.ServerEntity;
import com.yizhi.monitorsystem2.collection.entity.ServerTypeEntity;
import com.yizhi.monitorsystem2.collection.repository.ServerRepository;
import com.yizhi.monitorsystem2.collection.repository.ServerTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class LogHandle {
    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private ServerTypeRepository serverTypeRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ServerEntity serverEntity;

    public LogHandle(String serverId) {
        Optional<ServerEntity> serverEntityOptional = serverRepository.findById(serverId);
        serverEntityOptional.ifPresent(serverEntity -> this.serverEntity = serverEntity);
    }

    public void handle() {
        SSHUtil sshUtil = new SSHUtil(serverEntity);

        int[] serverTypes = serverEntity.getTypes();

        for (int currentServerType : serverTypes) {
            ServerTypeEntity serverTypeEntity = serverTypeRepository.findByTypesContains(currentServerType);
            String classPath = this.getClass().getPackage().getName() + "." + serverTypeEntity.getClassName();
            try {
                Class<?> classObject = Class.forName(classPath);
                Constructor constructor = classObject.getDeclaredConstructor(SSHUtil.class, int.class);
                constructor.setAccessible(true);
                LogAbstractHandle logAbstractHandle = (LogAbstractHandle) constructor.newInstance(sshUtil, currentServerType);
                logAbstractHandle.handle();
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                logger.error(e.toString(), e);
            }
        }

        sshUtil.close();
    }
}
