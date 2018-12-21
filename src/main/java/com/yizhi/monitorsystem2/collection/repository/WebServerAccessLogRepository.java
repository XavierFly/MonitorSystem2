package com.yizhi.monitorsystem2.collection.repository;

import com.yizhi.monitorsystem2.collection.entity.WebServerAccessLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WebServerAccessLogRepository extends MongoRepository<WebServerAccessLogEntity, String> {
}
