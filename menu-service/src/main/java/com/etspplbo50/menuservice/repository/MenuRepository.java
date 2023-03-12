package com.etspplbo50.menuservice.repository;

import com.etspplbo50.menuservice.model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuRepository extends MongoRepository<Menu, String> {

}
