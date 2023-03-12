package com.etspplbo50.inventoryservice.service;

import com.etspplbo50.inventoryservice.dto.InventoryRequest;
import com.etspplbo50.inventoryservice.dto.InventoryResponse;
import com.etspplbo50.inventoryservice.model.Inventory;
import com.etspplbo50.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public InventoryResponse createInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = Inventory.builder()
                .cafeId(inventoryRequest.getCafeId())
                .menuId(inventoryRequest.getMenuId())
                .isAvailable(false)
                .build();

        inventoryRepository.save(inventory);

        return mapModelToResponse(inventory);
    }

    public List<InventoryResponse> getAllInventory() {
        List<Inventory> inventoryList = inventoryRepository.findAll();

        return inventoryList.stream().map(inventory -> mapModelToResponse(inventory)).toList();
    }

//    public List<InventoryResponse> getInventoryByCafe(String cafeId) {
//        List<Inventory> inventoryList = inventoryRepository.findAllByCafeId(cafeId);
//
//        return inventoryList.stream().map(inventory -> mapModelToResponse(inventory)).toList();
//    }

    public InventoryResponse getInventory(String id) {
        Optional<Inventory> inventory = inventoryRepository.findById(id);

        if (inventory.isPresent()) {
            return mapModelToResponse(inventory.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory not found");
        }
    }

    public InventoryResponse updateInventory(String id, InventoryRequest inventoryRequest) {
        Optional<Inventory> inventory = inventoryRepository.findById(id);

        if (inventory.isPresent()) {
            Inventory newInventory = inventory.get();

            newInventory.setCafeId(inventoryRequest.getCafeId());
            newInventory.setMenuId(inventoryRequest.getMenuId());
            newInventory.setIsAvailable(inventoryRequest.getIsAvailable());

            inventoryRepository.save(newInventory);
            return mapModelToResponse(newInventory);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory not found");
        }
    }

    public String deleteInventory(String id) {
        Optional<Inventory> inventory = inventoryRepository.findById(id);

        if (inventory.isPresent()) {
            inventoryRepository.deleteById(id);
            return "Inventory deleted";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Inventory not found");
        }
    }

    private InventoryResponse mapModelToResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getId())
                .cafeId(inventory.getCafeId())
                .menuId(inventory.getMenuId())
                .isAvailable(inventory.getIsAvailable())
                .build();
    }
}
