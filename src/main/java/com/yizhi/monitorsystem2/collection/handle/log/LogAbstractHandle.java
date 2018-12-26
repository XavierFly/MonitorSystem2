package com.yizhi.monitorsystem2.collection.handle.log;

import com.yizhi.monitorsystem2.collection.exception.BaseException;
import com.yizhi.monitorsystem2.collection.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yizhi.monitorsystem2.collection.util.SSHUtil;
import com.yizhi.monitorsystem2.collection.entity.ServerEntity;
import com.yizhi.monitorsystem2.collection.entity.LogTraceEntity;
import com.yizhi.monitorsystem2.collection.repository.LogTraceRepository;

import java.io.BufferedReader;

public abstract class LogAbstractHandle {
    @Autowired
    private LogTraceRepository logTraceRepository;

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected SSHUtil sshUtil;
    protected LogTraceEntity logTraceEntity;
    protected int lastRow;
    protected long lastTimestamp;
    protected long currentHourTimestamp;
    protected BufferedReader bufferedReader;

    protected LogAbstractHandle(ServerEntity serverEntity, int currentServerType) {
        sshUtil = new SSHUtil(serverEntity);
        logTraceEntity = logTraceRepository.findByHostAndServerType(serverEntity.getHost(), currentServerType);
    }

    protected boolean initVariable() {
        try {
            currentHourTimestamp = TimeUtil.getCurrentHourTimestamp();
        } catch (BaseException e) {
            return false;
        }

        lastRow = logTraceEntity.getLastRow();
        lastTimestamp = logTraceEntity.getLastTimestamp();

        if (lastTimestamp == currentHourTimestamp && lastRow != 0) {
            logger.info("This hour has been collected");
            return false;
        }

        bufferedReader = sshUtil.readFile("", lastRow);

        return true;
    }

    protected abstract void handle();

    protected void close() {
        if (sshUtil != null) {
            sshUtil.close();
        }
    }
}
