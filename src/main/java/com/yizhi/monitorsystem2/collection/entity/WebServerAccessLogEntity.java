package com.yizhi.monitorsystem2.collection.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@ToString
@Document(collection = "web_server_access_log")
public class WebServerAccessLogEntity {
    @Id
    private String id;

    private long timestamp;

    private String ip;

    private String url;

    @Field("status_code")
    private int statusCode;

    @Field("server_type")
    private int serverType;

    @NotEmpty
    private String host;

    @Field("end_timestamp")
    private long endTimestamp;

    @CreatedDate
    @Field("create_timestamp")
    private long createTimestamp;
}
