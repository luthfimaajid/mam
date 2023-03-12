package com.etspplbo50.inventoryservice.Repository;

import com.etspplbo50.inventoryservice.Model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InventoryRepository extends MongoRepository<Inventory, String> {
    List<Inventory> findAllByCafeId(String cafeId);
}
