package com.etspplbo50.menuservice.Repository;

import com.etspplbo50.menuservice.Model.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MenuRepository extends MongoRepository<Menu, String> {

}
