package com.yizhi.monitorsystem2.collection.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.yizhi.monitorsystem2.collection.entity.WebServerAccessLogEntity;

public interface WebServerAccessLogRepository extends MongoRepository<WebServerAccessLogEntity, String> {
}
