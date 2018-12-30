package com.yizhi.monitorsystem2.collection.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "log_trace")
public class LogTraceEntity {
    @Id
    private String id;

    @NotEmpty
    private String host;

    @Field("server_type")
    private int serverType;

    private int lastRow;

    @Field("last_timestamp")
    private long lastTimestamp;

    @CreatedDate
    @Field("create_timestamp")
    private long createTimestamp;
}
