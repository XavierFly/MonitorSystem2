package com.yizhi.monitorsystem2.collection.handle.log;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.yizhi.monitorsystem2.collection.util.TimeAndLogUtil;
import com.yizhi.monitorsystem2.collection.exception.BaseException;
import com.yizhi.monitorsystem2.collection.entity.WebServerAccessLogEntity;
import com.yizhi.monitorsystem2.collection.repository.WebServerAccessLogRepository;

@Service
public class WebServerAccessLogHandle extends LogAbstractHandle {
    private static WebServerAccessLogHandle proxy;

    @Autowired
    private WebServerAccessLogRepository webServerAccessLogRepository;

    private List<String> currentLineList;
    private List<WebServerAccessLogEntity> webServerAccessLogEntityList = new ArrayList<>();

    @PostConstruct
    public void init() {
        super.init();
        proxy = this;
    }

    @Override
    public boolean parseCurrentLine(String currentLine) {
        currentLineList = new ArrayList<>();
        return splitCurrentLine(currentLine) && parseCurrentLineSplitArray();
    }

    @Override
    protected void saveLogEntity() {
        proxy.webServerAccessLogRepository.saveAll(webServerAccessLogEntityList);
    }

    private boolean splitCurrentLine(String currentLine) {
        String[] lineSplitArray = currentLine.split(" ");

        StringBuilder lineSplitStringBuilder = new StringBuilder();
        boolean isDoubleQuotesContent = false;
        for (String lineSplitItem : lineSplitArray) {
            int lineSplitItemLength = lineSplitItem.length();

            if (currentLineList.size() == 4) {
                break;
            }

            if (lineSplitItem.length() == 0 ||
                    lineSplitItem.equals("-") ||
                    lineSplitItem.endsWith("]")) {
                continue;
            }

            if (lineSplitItem.startsWith("[")) {
                lineSplitStringBuilder.append(lineSplitItem, 1, lineSplitItemLength);
                currentLineList.add(lineSplitStringBuilder.toString());
                lineSplitStringBuilder = new StringBuilder();
                continue;
            }

            if (lineSplitItem.contains("\"")) {
                if (lineSplitItem.startsWith("\"")) {
                    if (lineSplitItem.endsWith("\"") || Pattern.matches("^\".+\"[1-9]\\d{0,2}$", lineSplitItem)) {
                        try {
                            currentLineList.add(lineSplitItem.substring(0, lineSplitItemLength));
                        } catch (StringIndexOutOfBoundsException e) {
                            logger.error(e.toString(), e);
                            return false;
                        }
                    } else {
                        lineSplitStringBuilder.append(lineSplitItem, 0, lineSplitItemLength);
                        isDoubleQuotesContent = true;
                    }
                } else {
                    lineSplitStringBuilder.append(" ").append(lineSplitItem, 0, lineSplitItemLength);
                    currentLineList.add(lineSplitStringBuilder.toString());
                    lineSplitStringBuilder = new StringBuilder();
                    isDoubleQuotesContent = false;
                }
            } else {
                if (isDoubleQuotesContent) {
                    lineSplitStringBuilder.append(" ").append(lineSplitItem);
                } else {
                    currentLineList.add(lineSplitItem);
                    lineSplitStringBuilder = new StringBuilder();
                }
            }
        }

        return currentLineList.size() == 4;
    }

    private boolean parseCurrentLineSplitArray() {
        String ip = currentLineList.get(0);

        long timestamp;
        try {
            timestamp = Long.parseLong(TimeAndLogUtil.parseSystemLogTime(currentLineList.get(1)));
        } catch (BaseException e) {
            return false;
        }

        String url = currentLineList.get(2);
        if (url.startsWith("\"POST") || url.startsWith("\"GET")) {
            url = url.substring(url.indexOf(" ") + 1, url.lastIndexOf(" "));
        } else {
            url = url.substring(0, url.length() - 1);
        }

        int statusCode;
        String statusCodeString = currentLineList.get(3);
        if (statusCodeString.length() == 3) {
            statusCode = Integer.parseInt(statusCodeString);
        } else {
            statusCode = Integer.valueOf(statusCodeString.substring(statusCodeString.length() - 3, statusCodeString.length()));
        }

        if (timestamp < currentHourTimestamp) {
            WebServerAccessLogEntity webServerAccessLogEntity = new WebServerAccessLogEntity();
            webServerAccessLogEntity.setTimestamp(timestamp);
            webServerAccessLogEntity.setIp(ip);
            webServerAccessLogEntity.setUrl(url);
            webServerAccessLogEntity.setStatusCode(statusCode);
            webServerAccessLogEntity.setServerType(currentServerType);
            webServerAccessLogEntity.setEndTimestamp(currentHourTimestamp);
            webServerAccessLogEntity.setHost(host);
            webServerAccessLogEntityList.add(webServerAccessLogEntity);
        } else {
            newRow = lastRow;
            return false;
        }

        return true;
    }
}
