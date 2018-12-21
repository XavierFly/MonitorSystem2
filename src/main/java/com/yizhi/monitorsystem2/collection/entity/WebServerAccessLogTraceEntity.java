package com.yizhi.monitorsystem2.collection.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "web_server_access_log_trace")
public class WebServerAccessLogTraceEntity {
    @Id
    private String id;

    private int source;

    private long last_timestamp;

    private int row;

    private long create_timestamp;
}
