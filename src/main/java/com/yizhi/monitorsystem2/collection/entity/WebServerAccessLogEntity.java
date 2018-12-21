package com.yizhi.monitorsystem2.collection.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "web_server_access_log")
public class WebServerAccessLogEntity {
    @Id
    private String id;

    private long timestamp;

    private String url;

    private int status_code;

    private int source;

    private long end_timestamp;

    private long create_timestamp;
}
