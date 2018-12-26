package com.yizhi.monitorsystem2.collection.repository;

import com.yizhi.monitorsystem2.collection.entity.ServerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServerRepository extends MongoRepository<ServerEntity, String> {
}
