package com.etspplbo50.inventoryservice.repository;

import com.etspplbo50.inventoryservice.model.Inventory;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends MongoRepository<Inventory, String> {
    List<Inventory> findAllByCafeId(String cafeId);

    List<Inventory> findAllByCafeIdAndMenuIdIn(String cafeId, List<String> menuId);
}
