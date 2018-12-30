package com.yizhi.monitorsystem2.collection.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.yizhi.monitorsystem2.collection.entity.ServerTypeEntity;

public interface ServerTypeRepository extends MongoRepository<ServerTypeEntity, String> {
    ServerTypeEntity findByTypesContains(int type);
}
