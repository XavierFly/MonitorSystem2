package com.yizhi.monitorsystem2.collection.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.yizhi.monitorsystem2.collection.entity.ServerEntity;

public interface ServerRepository extends MongoRepository<ServerEntity, String> {
}
