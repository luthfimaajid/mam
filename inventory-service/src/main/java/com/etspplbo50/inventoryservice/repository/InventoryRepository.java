package com.etspplbo50.inventoryservice.repository;

import com.etspplbo50.inventoryservice.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InventoryRepository extends MongoRepository<Inventory, String> {
    List<Inventory> findAllByCafeId(String cafeId);
}
