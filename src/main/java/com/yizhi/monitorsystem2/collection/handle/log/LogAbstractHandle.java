package com.yizhi.monitorsystem2.collection.handle.log;

import java.io.IOException;
import java.io.BufferedReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.yizhi.monitorsystem2.collection.util.SSHUtil;
import com.yizhi.monitorsystem2.collection.util.TimeAndLogUtil;
import com.yizhi.monitorsystem2.collection.entity.LogTraceEntity;
import com.yizhi.monitorsystem2.collection.exception.BaseException;
import com.yizhi.monitorsystem2.collection.repository.LogTraceRepository;

@Service
public abstract class LogAbstractHandle {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static LogAbstractHandle proxy;

    @Autowired
    private LogTraceRepository logTraceRepository;

    private SSHUtil sshUtil;
    protected String host;
    protected int currentServerType;
    protected int lastRow;
    protected int newRow;
    protected long currentHourTimestamp;

    @PostConstruct
    public void init() {
        proxy = this;
    }

    public void setSshUtil(SSHUtil sshUtil) {
        this.sshUtil = sshUtil;
        this.host = sshUtil.getHost();
    }

    public void setCurrentServerType(int currentServerType) {
        this.currentServerType = currentServerType;
    }

    protected abstract boolean parseCurrentLine(String currentLine);

    protected abstract void saveLogEntity();

    public void handle() {
        try {
            currentHourTimestamp = TimeAndLogUtil.getCurrentHourTimestamp();
        } catch (BaseException e) {
            return;
        }

        LogTraceEntity logTraceEntity;
        try {
            logTraceEntity = proxy.logTraceRepository.findByHostAndServerType(host, currentServerType);
            lastRow = logTraceEntity.getLastRow();
            if (logTraceEntity.getLastTimestamp() == currentHourTimestamp && lastRow != 0) {
                return;
            }
        } catch (Exception e) {
            lastRow = 0;
        }

        String logFilePath;
        try {
            logFilePath = TimeAndLogUtil.getLogFilePath(currentServerType);
            System.out.println(logFilePath);
        } catch (BaseException e) {
            return;
        }

        BufferedReader bufferedReader = sshUtil.readFile(logFilePath, lastRow);
        try {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                System.out.println(currentLine);
                if (!parseCurrentLine(currentLine)) {
                    break;
                }
                lastRow++;
            }
        } catch (IOException e) {
            logger.error(e.toString(), e);
            return;
        }

        collectLog();
    }

    private void collectLog() {
        saveLogEntity();

        if (TimeAndLogUtil.isMidNight(currentHourTimestamp)) {
            newRow = 0;
        }

        LogTraceEntity logTraceEntity = new LogTraceEntity();
        logTraceEntity.setHost(host);
        logTraceEntity.setServerType(currentServerType);
        logTraceEntity.setLastRow(newRow);
        logTraceEntity.setLastTimestamp(currentHourTimestamp);
        proxy.logTraceRepository.save(logTraceEntity);
    }
}
