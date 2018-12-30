package com.yizhi.monitorsystem2.collection.repository;

import com.yizhi.monitorsystem2.collection.entity.LogTraceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogTraceRepository extends MongoRepository<LogTraceEntity, String> {
    LogTraceEntity findByHostAndServerType(String host, int serverType);
}
