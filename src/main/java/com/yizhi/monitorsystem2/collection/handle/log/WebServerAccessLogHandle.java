package com.yizhi.monitorsystem2.collection.handle.log;

import com.yizhi.monitorsystem2.collection.entity.WebServerAccessLogEntity;
import com.yizhi.monitorsystem2.collection.exception.BaseException;
import com.yizhi.monitorsystem2.collection.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.yizhi.monitorsystem2.collection.entity.ServerEntity;
import com.yizhi.monitorsystem2.collection.repository.WebServerAccessLogRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebServerAccessLogHandle extends LogAbstractHandle {
    @Autowired
    private WebServerAccessLogRepository webServerAccessLogRepository;

    protected WebServerAccessLogHandle(ServerEntity serverEntity, int currentServerType) {
        super(serverEntity, currentServerType);
    }

    private

    @Override
    public void handle() {
        if (!initVariable()) {
            close();
            return;
        }

        List<WebServerAccessLogEntity> webServerAccessLogEntityList = new ArrayList<>();
        List<String> currentLineList;

        String currentLine;
        try {
            currentLine = bufferedReader.readLine();
        } catch (IOException e) {
            logger.error(e.toString(), e);
            close();
            return;
        }

        int row = 0;
        while (currentLine != null) {
            currentLineList = new ArrayList<>();
            String[] lineSplitArray = currentLine.split(" ");

            int putTimes = 0;
            StringBuilder tmpString = new StringBuilder();
            for (int index = 0; index < lineSplitArray.length; index++) {
                if (lineSplitArray[index].equals("-") ||
                        lineSplitArray[index].equals("\"-\"") ||
                        lineSplitArray[index].length() == 0) {
                    continue;
                }

                if (index == 1 || index == 2 || index == 4) {
                    continue;
                }

                if (index == 3) {
                    tmpString.append(lineSplitArray[index], 1, lineSplitArray[index].length());
                    currentLineList.add(tmpString.toString());
                    tmpString = new StringBuilder();
                    continue;
                }

                if (lineSplitArray[index].contains("\"")) {
                    try {
                        if (lineSplitArray[index].charAt(0) == '"' && lineSplitArray[index].substring(1, lineSplitArray[index].length()).contains("\"") && putTimes == 0) {
                            currentLineList.add(lineSplitArray[index]);
                        } else {
                            if (putTimes == 0) {
                                putTimes++;
                                tmpString.append(lineSplitArray[index], 1, lineSplitArray[index].length());
                            } else {
                                tmpString.append(" ").append(lineSplitArray[index], 0, lineSplitArray[index].length());
                                currentLineList.add(tmpString.toString());
                                putTimes = 0;
                                tmpString = new StringBuilder();
                            }
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        log.error(e.toString() + ", line: " + row);
                        close();
                        return;
                    }
                } else {
                    if (putTimes > 0) {
                        putTimes++;
                        tmpString.append(" ").append(lineSplitArray[index]);
                    } else {
                        currentLineList.add(lineSplitArray[index]);
                    }
                }
            }

            String ip = currentLineList.get(0);
            String timestamp;
            try {
                timestamp = TimeUtil.parseSystemLogTime(currentLineList.get(1));
            } catch (BaseException e) {
                log.error(e.toString() + ", line: " + row);
                close();
                return;
            }
            String apiString = currentLineList.get(2);
            String url;
            try {
                if (apiString.contains(" ")) {
                    if ((apiString.contains("POST") && apiString.indexOf("POST") == 0) || (apiString.contains("GET") && apiString.indexOf("GET") == 0)) {
                        int startIndex = apiString.indexOf(" ") + 1;
                        int endIndex = apiString.lastIndexOf(" ");
                        url = apiString.substring(startIndex, endIndex);
                    } else {
                        url = apiString.substring(0, apiString.length() - 1);
                    }
                } else {
                    url = apiString.substring(1, apiString.length() - 1);
                }
            } catch (StringIndexOutOfBoundsException e) {
                log.error(e.toString() + ", line: " + row);
                close();
                return;
            }

            int statusCode;
            try {
                String statusCodeString = currentLineList.get(3);
                statusCode = Integer.valueOf(statusCodeString.substring(statusCodeString.length() - 3, statusCodeString.length()));
            } catch (StringIndexOutOfBoundsException e) {
                log.error(e.toString() + ", line: " + row);
                close();
                return;
            }

            long logTimestamp = Long.valueOf(timestamp);
            if (logTimestamp >= startTimestamp && logTimestamp < endTimestamp) {
                SystemLogCollectionEntity systemLogCollectionEntity = new SystemLogCollectionEntity();
                systemLogCollectionEntity.setTimestamp(logTimestamp);
                systemLogCollectionEntity.setIp(ip);
                systemLogCollectionEntity.setUrl(url);
                systemLogCollectionEntity.setStatusCode(statusCode);
                systemLogCollectionEntity.setSource(NGINX_SIGN);
                systemLogCollectionEntity.setEndTimestamp(currentHourTimestamp);
                systemLogCollectionEntity.setHost(host);
                systemLogCollectionEntityList.add(systemLogCollectionEntity);
            } else if (logTimestamp >= endTimestamp) {
                log.info("Nginx: " + row);
                log.info("Nginx: " + currentLine);
                log.info("Nginx: " + logTimestamp);
                newRow = row;
                break;
            }

            if (testIndex == 0) {
                log.info("Nginx: " + row);
                log.info("Nginx: " + currentLine);
                log.info("Nginx: " + logTimestamp);
            }

            try {
                currentLine = bufferedReader.readLine();
            } catch (IOException e) {
                log.error(e.toString());
                close();
                return;
            }

            testIndex++;
            row++;
        }

        if (TimeUtil.timestampToTime(endTimestamp).contains("00:00:00")) {
            newRow = 0;
        }

        systemLogDao.insertSystemLog(systemLogCollectionEntityList);
        lastLogCollectionInfoDao.updateLastLogCollectionInfo(currentHourTimestamp, newRow, NGINX_SIGN, host);
        close();

        log.info("Nginx log collected");

        long testTime2 = System.currentTimeMillis();
        log.info("Nginx: Time passed: " + (testTime2 - testTime1));
    }
}
