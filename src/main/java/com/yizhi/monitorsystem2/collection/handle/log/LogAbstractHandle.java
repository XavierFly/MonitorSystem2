package com.yizhi.monitorsystem2.collection.handle.log;

import java.io.IOException;
import java.io.BufferedReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yizhi.monitorsystem2.collection.util.SSHUtil;
import com.yizhi.monitorsystem2.collection.util.TimeAndLogUtil;
import com.yizhi.monitorsystem2.collection.entity.LogTraceEntity;
import com.yizhi.monitorsystem2.collection.exception.BaseException;
import com.yizhi.monitorsystem2.collection.repository.LogTraceRepository;

public abstract class LogAbstractHandle {
    @Autowired
    private LogTraceRepository logTraceRepository;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private SSHUtil sshUtil;

    protected String host;
    protected int currentServerType;
    protected int lastRow;
    protected int newRow;
    protected long currentHourTimestamp;

    public LogAbstractHandle() {
    }

    protected LogAbstractHandle(SSHUtil sshUtil, int currentServerType) {
        this.sshUtil = sshUtil;
        this.host = sshUtil.getHost();
        this.currentServerType = currentServerType;
    }

    protected abstract boolean parseCurrentLine(String currentLine);

    protected abstract void saveLogEntity();

    public void handle() {
        LogTraceEntity logTraceEntity = logTraceRepository.findByHostAndServerType(host, currentServerType);
        lastRow = logTraceEntity.getLastRow();

        try {
            currentHourTimestamp = TimeAndLogUtil.getCurrentHourTimestamp();
        } catch (BaseException e) {
            return;
        }

        if (logTraceEntity.getLastTimestamp() == currentHourTimestamp && lastRow != 0) {
            return;
        }

        String logFilePath;
        try {
            logFilePath = TimeAndLogUtil.getLogFilePath(currentServerType);
        } catch (BaseException e) {
            return;
        }

        BufferedReader bufferedReader = sshUtil.readFile(logFilePath, lastRow);
        try {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
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
        logTraceRepository.save(logTraceEntity);
    }
}
