package com.yizhi.monitorsystem2.collection.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "server")
public class ServerEntity {
    @Id
    private String id;

    @NotEmpty
    private String host;

    private int port;

    @NotEmpty
    private String user;

    private String password;

    private int[] types;
}
